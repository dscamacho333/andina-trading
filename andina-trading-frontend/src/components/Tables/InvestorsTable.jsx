// src/components/Tables/InvestorsTable.jsx

function toDateLabel(dt) {
  if (!dt) return "-";
  try {
    const s = typeof dt === "string" ? dt.replace(" ", "T") : dt;
    const d = new Date(s);
    if (Number.isNaN(d.getTime())) return dt;
    return d.toLocaleString();
  } catch {
    return dt;
  }
}

const STATUS_LABEL = {
  ACTIVE: "ACTIVO",
  INACTIVE: "INACTIVO",
  RESTRICTED: "RESTRINGIDO",
  PENDING: "PENDIENTE",
};

export default function InvestorsTable({ investors = [], onDelete }) {
  if (!Array.isArray(investors) || investors.length === 0) {
    return <div className="subtitle">No hay inversionistas para mostrar.</div>;
    }

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th style={{ width: 60 }}>ID</th>
            <th style={{ width: 260 }}>Nombre</th>
            <th>Email</th>
            <th style={{ width: 140 }}>Rol</th>
            <th style={{ width: 200 }}>Teléfono</th>
            <th style={{ width: 140 }}>Estado</th>
            <th style={{ width: 200 }}>Creado</th>
            <th style={{ width: 160 }}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {investors.map((it) => {
            const canDelete = it.accountStatus !== "INACTIVE";
            return (
              <tr key={it.id}>
                <td>{it.id}</td>
                <td><strong>{it.name ?? "-"}</strong></td>
                <td>{it.email ?? "-"}</td>
                <td>{it.role ?? "-"}</td>
                <td>{it.phone ?? "-"}</td>
                <td>{STATUS_LABEL[it.accountStatus] ?? it.accountStatus ?? "-"}</td>
                <td>{toDateLabel(it.createdAt)}</td>
                <td className="cell-actions">
                  {canDelete && (
                    <button
                      type="button"
                      className="btn-pill btn-danger"
                      onClick={() => onDelete?.(it.id, it.name || it.id)}
                    >
                      Eliminar
                    </button>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}