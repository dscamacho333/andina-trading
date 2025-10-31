// src/components/Layout/AdminLogsManagement.jsx
import { useEffect, useState } from "react";
import { adminLogsApi } from "../../services/adminLogsApi";
import LogFilesTable from "../Tables/LogFilesTable";

// Reutiliza tu UI base
import { Notification } from "../UI/Notification";
import { useNotification } from "../../hooks/useNotification";
import { useErrorHandler } from "../../hooks/useErrorHandler";

// ⚠️ Mapeo de etiquetas -> valores esperados por el backend
const TYPE_OPTIONS = [
  { value: "daily-log-excel", label: "Daily log (Excel)" },
  // El backend expone 'snapshot-2min-excel'; lo mostramos como “25 minutes log”
  { value: "snapshot-2min-excel", label: "25 minutes log (Excel)" },
];

export default function AdminLogsManagement() {
  const [files, setFiles] = useState([]);

  // filtros (solo fechas y tipo). Tipo por defecto = 25 min
  const [start, setStart] = useState(""); // yyyy-MM-dd
  const [end, setEnd] = useState("");
  const [type, setType] = useState("snapshot-2min-excel");

  const [loading, setLoading] = useState(false);
  const { notification, showNotification, hideNotification } = useNotification();
  const { handleError } = useErrorHandler();

  // carga inicial con el tipo por defecto
  useEffect(() => {
    fetchList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchList = async () => {
    try {
      setLoading(true);
      const params = {
        ...(start ? { start } : {}),
        ...(end ? { end } : {}),
        ...(type ? { type } : {}),
      };
      const list = await adminLogsApi.listFiles(params);
      setFiles(Array.isArray(list) ? list : []);
      if (!Array.isArray(list) || list.length === 0) {
        showNotification("Sin resultados para los filtros aplicados", "info");
      }
    } catch (e) {
      handleError(e);
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = async () => {
    setStart("");
    setEnd("");
    setType("snapshot-2min-excel"); // vuelve al default (25 min)
    try {
      setLoading(true);
      const list = await adminLogsApi.listFiles({ type: "snapshot-2min-excel" });
      setFiles(Array.isArray(list) ? list : []);
    } catch (e) {
      handleError(e);
    } finally {
      setLoading(false);
    }
  };

  const downloadUrl = (id) => adminLogsApi.downloadUrl(id);

  return (
    <>
      {/* Notificación */}
      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={hideNotification}
        />
      )}

      {/* Título bloque (tu layout ya lo pone bonito) */}
      <h2 className="block-title">Archivos de Log</h2>
      <p className="subtitle">Filtra, visualiza y descarga los reportes generados por los servicios.</p>

      {/* Toolbar: SOLO fechas + tipo */}
      <div className="toolbar">
        <div
          className="filters-row"
          style={{
            gridTemplateColumns: "1fr 1fr 1fr auto auto",
            gap: 10,
          }}
        >
          {/* Fecha inicio */}
          <input
            type="date"
            value={start}
            onChange={(e) => setStart(e.target.value)}
            style={{
              width: "100%",
              background: "var(--card-2)",
              color: "var(--text)",
              border: "1px solid var(--border-2)",
              borderRadius: 10,
              padding: "10px 12px",
            }}
          />

          {/* Fecha fin */}
          <input
            type="date"
            value={end}
            onChange={(e) => setEnd(e.target.value)}
            style={{
              width: "100%",
              background: "var(--card-2)",
              color: "var(--text)",
              border: "1px solid var(--border-2)",
              borderRadius: 10,
              padding: "10px 12px",
            }}
          />

          {/* Tipo (Daily / 25 min) */}
          <select value={type} onChange={(e) => setType(e.target.value)}>
            {TYPE_OPTIONS.map((t) => (
              <option key={t.value} value={t.value}>
                {t.label}
              </option>
            ))}
          </select>

          {/* Botones */}
          <button type="button" className="btn-primary" onClick={fetchList}>
            Buscar
          </button>
          <button type="button" onClick={clearFilters}>
            Limpiar
          </button>
        </div>
      </div>

      {/* Tabla */}
      <LogFilesTable files={files} onDownload={downloadUrl} />

      {loading && <p className="subtitle" style={{ marginTop: 10 }}>Procesando…</p>}
    </>
  );
}