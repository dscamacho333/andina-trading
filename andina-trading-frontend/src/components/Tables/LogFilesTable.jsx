// src/components/Tables/LogFilesTable.jsx

// ---------- Utils ----------
function humanBytes(n) {
  if (!Number.isFinite(n)) return "-";
  const units = ["B", "KB", "MB", "GB", "TB"];
  let i = 0, num = n;
  while (num >= 1024 && i < units.length - 1) { num /= 1024; i++; }
  return `${num.toFixed(num >= 10 || i === 0 ? 0 : 1)} ${units[i]}`;
}

// Intenta convertir "algo" a Date: ISO string, epoch (ms o s),
// objeto { $date: ... }, { time: ... }, etc.
function anyToDate(x) {
  if (!x && x !== 0) return null;

  const tryNewDate = (v) => {
    const d = new Date(v);
    return Number.isNaN(d.getTime()) ? null : d;
  };

  if (x instanceof Date) return x;

  // Epoch en segundos o milisegundos
  if (typeof x === "number") {
    // si es muy pequeño, probablemente está en segundos
    return x < 10_000_000_000 ? tryNewDate(x * 1000) : tryNewDate(x);
  }

  // ISO string
  if (typeof x === "string") {
    // Reemplazar espacio por 'T' si viene "yyyy-MM-dd HH:mm:ss"
    const s = x.includes(" ") && !x.includes("T") ? x.replace(" ", "T") : x;
    return tryNewDate(s);
  }

  // { $date: ... } u otras variantes comunes
  if (typeof x === "object") {
    if ("$date" in x) return anyToDate(x.$date);
    if ("time" in x) return anyToDate(x.time);
    if ("value" in x) return anyToDate(x.value);
  }

  return null;
}

// Si no viene la fecha, tratamos de inferirla del nombre:
// ejemplo: msvc-xxx-2025-10-30_13-22.xlsx  -> 2025-10-30 13:22
function dateFromFilename(name) {
  if (typeof name !== "string") return null;
  // admite 2025-10-30_13-22 o 2025-10-30-13-22 o 2025_10_30_13_22
  const re =
    /(?<y>\d{4})[-_](?<m>\d{2})[-_](?<d>\d{2})[-_](?<H>\d{2})[-_](?<M>\d{2})/;
  const m = name.match(re);
  if (!m || !m.groups) return null;
  const { y, m: mm, d, H, M } = m.groups;
  const iso = `${y}-${mm}-${d}T${H}:${M}:00`;
  const dte = new Date(iso);
  return Number.isNaN(dte.getTime()) ? null : dte;
}

function toDateLabel(any) {
  const d =
    anyToDate(any) ||
    // intento con metadata { uploadDate }, etc.
    anyToDate(any?.uploadDate) ||
    anyToDate(any?.createdAt) ||
    anyToDate(any?.uploadedAt) ||
    null;
  if (d) return d.toLocaleString();

  // Si 'any' era un string filename, también sirve
  if (typeof any === "string") {
    const fromName = dateFromFilename(any);
    if (fromName) return fromName.toLocaleString();
  }

  return "-";
}

// ---------- Normalización de filas ----------
function extract(row) {
  const id =
    row?.id ??
    row?._id ??
    row?.fileId ??
    row?.gfsId ??
    row?.objectId ??
    row?.["$oid"] ??
    String(row);

  const filename =
    row?.filename ??
    row?.name ??
    row?.fileName ??
    row?.metadata?.filename ??
    "-";

  const length =
    row?.length ??
    row?.size ??
    row?.fileLength ??
    row?.metadata?.length;

  const uploadDateRaw =
    row?.uploadDate ??
    row?.createdAt ??
    row?.uploadedAt ??
    row?.metadata?.uploadDate ??
    row?.metadata?.createdAt ??
    row?.metadata?.uploadedAt ??
    null;

  const service =
    row?.service ?? row?.metadata?.service ?? row?.meta?.service ?? "-";

  const type =
    row?.type ?? row?.metadata?.type ?? row?.meta?.type ?? "daily-log-excel";

  // si no hay uploadDate, intentamos inferir desde filename
  const dateLabel = uploadDateRaw ? toDateLabel(uploadDateRaw) : toDateLabel(filename);

  return {
    id,
    filename,
    length: Number(length),
    dateLabel,
    service,
    type,
  };
}

// ---------- Tabla ----------
export default function LogFilesTable({ files = [], onDownload }) {
  if (!Array.isArray(files) || files.length === 0) {
    return <div className="subtitle">No hay archivos para mostrar.</div>;
  }

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th style={{ width: 60 }}>#</th>
            <th>Nombre</th>
            <th style={{ width: 160 }}>Servicio</th>
            <th style={{ width: 180 }}>Tipo</th>
            <th style={{ width: 120 }}>Tamaño</th>
            <th style={{ width: 220 }}>Fecha</th>
            <th style={{ width: 160 }}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {files.map((raw, idx) => {
            const f = extract(raw);
            return (
              <tr key={f.id ?? idx}>
                <td>{idx + 1}</td>
                <td><strong>{f.filename}</strong></td>
                <td>{f.service}</td>
                <td>{f.type}</td>
                <td>{humanBytes(f.length)}</td>
                <td>{f.dateLabel}</td>
                <td className="cell-actions">
                  <a
                    className="btn-pill btn-edit"
                    href={onDownload?.(f.id)}
                    target="_blank"
                    rel="noreferrer"
                  >
                    Ver / Descargar
                  </a>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
