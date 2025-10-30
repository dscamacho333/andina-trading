import React, { useEffect, useRef, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import OrderGallery from "../../components/OrderGallery/OrderGallery";
import styles from "./PortfolioPage.module.css";
import { Loading } from "../../components/Loading/Loading";
import { PositionTable } from "../../components/PositionTable/PositionTable";
import ReactECharts from "echarts-for-react";

/* ================= Helpers ================= */

const isoToMs = (iso) => new Date(iso).getTime(); // ms UNIX

// Canal ascendente (soporte: mínimos; resistencia: paralela por máximos)
function computeAscendingChannel(rows /* [{t,o,h,l,c,v}] */) {
  if (!rows || rows.length < 10) return null;

  const swingsLow = [];
  const swingsHigh = [];
  for (let i = 2; i < rows.length - 2; i++) {
    const b = rows[i];
    if (
      b.l < rows[i - 1].l && b.l < rows[i - 2].l &&
      b.l <= rows[i + 1].l && b.l <= rows[i + 2].l
    ) swingsLow.push({ t: b.t, y: b.l });

    if (
      b.h > rows[i - 1].h && b.h > rows[i - 2].h &&
      b.h >= rows[i + 1].h && b.h >= rows[i + 2].h
    ) swingsHigh.push({ t: b.t, y: b.h });
  }
  if (swingsLow.length < 2 || swingsHigh.length < 1) return null;

  const L1 = swingsLow[0], L2 = swingsLow[swingsLow.length - 1];
  const m = (L2.y - L1.y) / ((L2.t - L1.t) || 1);
  const bLow = L1.y - m * L1.t;
  if (m <= 0) return null; // sólo canal ascendente

  let offset = 0;
  for (const p of swingsHigh) {
    const yLowAtT = m * p.t + bLow;
    offset = Math.max(offset, p.y - yLowAtT);
  }
  const bHigh = bLow + offset;
  return { m, bLow, bHigh };
}

// Normaliza lo que devuelve TU backend (Alpaca via API) a filas con timestamp en ms
function normalizeRows(raw = []) {
  const rows = raw
    .map(b => ({
      t: isoToMs(b.time), // ms unix
      o: b.open, h: b.high, l: b.low, c: b.close, v: b.volume ?? 0
    }))
    .sort((a,b) => a.t - b.t);
  return rows;
}

/* ========== Mini-chart “Otros” — dinámico con tu backend ========== */

function AscendingChannelMini({
  symbol = "AAPL",
  timeframe = "1Day",
  lastDays = 30,
  feed = "iex",
  baseUrl = "",     // "" si usas proxy CRA; "http://localhost:8080" si vas directo con CORS
  height = 280,
  pollMs = 10000,   // 0 para desactivar polling
}) {
  const [error, setError] = useState("");
  const [option, setOption] = useState(null);
  const [chartKey, setChartKey] = useState(0); // fuerza remount en cada update
  const timerRef = useRef(null);

  const buildOption = (rows) => {
    const kline = rows.map(r => [r.t, r.o, r.c, r.l, r.h]); // [time, open, close, low, high]
    const ch = computeAscendingChannel(rows);
    const support = [];
    const resistance = [];
    if (ch) {
      for (const r of rows) {
        support.push([r.t, ch.m * r.t + ch.bLow]);
        resistance.push([r.t, ch.m * r.t + ch.bHigh]);
      }
    }

    return {
      backgroundColor: "transparent",
      animation: false,
      grid: { left: 8, right: 8, top: 10, bottom: 16, containLabel: false },
      xAxis: {
        type: "time",
        axisLine: { lineStyle: { color: "#2c2f3a" } },
        axisLabel: { color: "#9aa0a6" },
        splitLine: { show: false },
      },
      yAxis: {
        scale: true,
        axisLine: { lineStyle: { color: "#2c2f3a" } },
        axisLabel: { color: "#9aa0a6" },
        splitLine: { lineStyle: { color: "#1a1f2e" } },
      },
      tooltip: {
        trigger: "axis",
        axisPointer: { type: "cross" },
        backgroundColor: "#0b0b15",
        borderColor: "#2c2f3a",
        textStyle: { color: "#e5e7eb" },
      },
      series: [
        {
          name: symbol,
          type: "candlestick",
          data: kline,
          itemStyle: {
            color: "#26a69a", borderColor: "#26a69a",
            color0: "#ef5350", borderColor0: "#ef5350",
          },
        },
        ...(ch
          ? [
              {
                name: "Support",
                type: "line",
                data: support,
                showSymbol: false,
                lineStyle: { width: 1.5 },
                emphasis: { disabled: true },
                z: 2,
              },
              {
                name: "Resistance",
                type: "line",
                data: resistance,
                showSymbol: false,
                lineStyle: { width: 1.5 },
                emphasis: { disabled: true },
                z: 2,
              },
            ]
          : []),
      ],
    };
  };

  const fetchAndRender = async () => {
    try {
      setError("");

      const url = new URL(`/api/stocks/bars`, baseUrl || window.location.origin);
      url.searchParams.set("symbols", symbol);
      url.searchParams.set("timeframe", timeframe);
      url.searchParams.set("feed", feed);
      url.searchParams.set("lastDays", String(lastDays));

      const res = await fetch(url.toString());
      const ct = res.headers.get("content-type") || "";

      if (!res.ok) {
        const text = await res.text();
        throw new Error(`HTTP ${res.status} – ${text.slice(0, 120)}…`);
      }
      if (!ct.includes("application/json")) {
        const text = await res.text();
        throw new Error(`Respuesta no-JSON (${ct}). Inicio: ${text.slice(0, 120)}…`);
      }

      const json = await res.json();
      const raw = json?.[symbol];
      if (!Array.isArray(raw) || raw.length === 0) throw new Error(`Sin datos para ${symbol}`);

      const rows = normalizeRows(raw);
      const newOption = buildOption(rows);

      setOption(newOption);
      setChartKey(k => k + 1); // remount para evitar estados viejos de tooltip
    } catch (e) {
      setError(e.message || "Error cargando datos");
      console.error("fetchAndRender error:", e);
    }
  };

  useEffect(() => {
    fetchAndRender();
    if (pollMs > 0) timerRef.current = setInterval(fetchAndRender, pollMs);
    return () => clearInterval(timerRef.current);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [symbol, timeframe, lastDays, feed, baseUrl, pollMs]);

  return (
    <div style={{ position: "relative", width: "100%", height }}>
      {option && (
        <ReactECharts
          key={chartKey}
          option={option}
          style={{ width: "100%", height: "100%" }}
          notMerge
          lazyUpdate
          opts={{ renderer: "canvas" }}
        />
      )}
      {!option && !error && (
        <div style={{ position: "absolute", inset: 0, display: "flex", alignItems: "center", justifyContent: "center", color: "#9aa0a6" }}>
          Cargando gráfico…
        </div>
      )}
      {error && (
        <div style={{ position: "absolute", inset: 0, display: "flex", alignItems: "center", justifyContent: "center", color: "salmon", textAlign: "center", padding: 8 }}>
          {error}
        </div>
      )}
    </div>
  );
}

/* ========== Carga de símbolos del usuario ========== */
/**
 * Intenta, en orden:
 *  1) GET /api/portfolio/symbols -> ["AAPL","MSFT",...]
 *  2) GET /api/portfolio/positions -> [{symbol: "AAPL", ...}, ...]
 *  3) GET /api/stocks -> [{symbol: "AAPL", ...}, ...]   (fallback)
 */
async function loadUserSymbols(baseUrl = "") {
  const tryJson = async (path, pickSymbols) => {
    const url = new URL(path, baseUrl || window.location.origin);
    const r = await fetch(url.toString());
    if (!r.ok) return null;
    const ct = r.headers.get("content-type") || "";
    if (!ct.includes("application/json")) return null;
    const j = await r.json();
    const syms = pickSymbols(j);
    if (!Array.isArray(syms) || syms.length === 0) return null;
    // normaliza y dedup
    const set = new Set(syms.map(s => String(s || "").trim().toUpperCase()).filter(Boolean));
    return Array.from(set).sort();
  };

  // 1) /api/portfolio/symbols
  const s1 = await tryJson("/api/portfolio/symbols", j => j);
  if (s1) return s1;

  // 2) /api/portfolio/positions
  const s2 = await tryJson("/api/portfolio/positions", j => (j || []).map(p => p.symbol));
  if (s2) return s2;

  // 3) /api/stocks (fallback)
  const s3 = await tryJson("/api/stocks", j => (j || []).map(s => s.symbol));
  if (s3) return s3;

  return []; // nada
}

/* ================= Página ================= */

const PortfolioPage = () => {
  const [isLoading, setIsLoading] = useState(true);

  // Símbolos del usuario (dinámicos)
  const [symbols, setSymbols] = useState([]);
  const [symbolsError, setSymbolsError] = useState("");

  // Parámetros del gráfico
  const [symbol, setSymbol] = useState(""); // se seteará al primer símbolo cargado
  const [timeframe] = useState("1Day");
  const [lastDays] = useState(30);
  const [feed] = useState("iex");
  const baseUrl = ""; // proxy CRA → http://localhost:8080

  // Carga la lista de símbolos del usuario
  useEffect(() => {
    let aborted = false;
    (async () => {
      try {
        setSymbolsError("");
        const list = await loadUserSymbols(baseUrl);
        if (aborted) return;
        if (!list || list.length === 0) {
          setSymbols([]);
          setSymbolsError("No se encontraron símbolos del usuario.");
          return;
        }
        setSymbols(list);
        // si el símbolo seleccionado no está, toma el primero
        setSymbol(prev => (list.includes(prev) ? prev : list[0]));
      } catch (e) {
        if (!aborted) setSymbolsError("Error cargando símbolos del usuario.");
      }
    })();
    return () => { aborted = true; };
  }, [baseUrl]);

  // Simula skeleton inicial
  useEffect(() => {
    const timer = setTimeout(() => setIsLoading(false), 800);
    return () => clearTimeout(timer);
  }, []);

  return (
    <DashboardLayout>
      {isLoading ? (
        <Loading message="Cargando portafolio..." />
      ) : (
        <div className={styles.container}>
          <span className={styles.span}>Última actualización: Hace 1 min</span>
          <h2 className={styles.title}>Mi portafolio</h2>

          <PositionTable />

          {/* === CUADRO "Otros" === */}
          <div className={styles.box}>
            <h3>Historico</h3>

            <div style={{ position: "relative", width: "100%", height: 280 }}>
              {/* Selector de símbolo (dinámico) */}
              <select
                value={symbol}
                onChange={(e) => setSymbol(e.target.value)}
                disabled={!symbols.length}
                style={{
                  position: "absolute",
                  top: 8,
                  right: 8,
                  zIndex: 4,
                  background: "rgba(11,11,21,.9)",
                  color: "#e5e7eb",
                  border: "1px solid #2c2f3a",
                  borderRadius: 8,
                  padding: "4px 8px",
                  fontSize: 12,
                }}
              >
                {symbols.map((s) => (
                  <option key={s} value={s}>{s}</option>
                ))}
              </select>

              {/* Mensaje si no hay símbolos */}
              {!symbols.length && symbolsError && (
                <div style={{ position: "absolute", inset: 0, display: "flex", alignItems: "center", justifyContent: "center", color: "salmon", textAlign: "center", padding: 8 }}>
                  {symbolsError}
                </div>
              )}

              {/* El chart ocupa todo el contenedor (280px alto) */}
              {symbol && (
                <AscendingChannelMini
                  symbol={symbol}
                  timeframe={timeframe}
                  lastDays={lastDays}
                  feed={feed}
                  baseUrl={baseUrl}   // "" usa proxy CRA
                  height={280}
                  pollMs={10000}      // 0 para desactivar polling
                />
              )}
            </div>
          </div>

          <div className={`${styles.box} ${styles.ordersWrapper}`}>
            <OrderGallery />
          </div>
        </div>
      )}
    </DashboardLayout>
  );
};

export default PortfolioPage;
