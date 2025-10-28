// src/components/Tables/BrokersTable.jsx

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

export default function BrokersTable({
  brokers = [],
  onEdit,
  onDelete,
}) {
  if (!Array.isArray(brokers) || brokers.length === 0) {
    return <div className="subtitle">No hay brokers para mostrar.</div>;
  }

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th style={{ width: 60 }}>ID</th>
            <th style={{ width: 180 }}>Nombre</th>
            <th style={{ width: 180 }}>Apellido</th>
            <th style={{ width: 170 }}>Tipo doc.</th>
            <th style={{ width: 160 }}>N.º documento</th>
            <th>Email</th>
            <th style={{ width: 190 }}>Teléfono</th> {/* más ancho */}
            <th style={{ width: 180 }}>Creado</th>
            <th style={{ width: 180 }}>Actualizado</th>
            <th style={{ width: 180 }}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {brokers.map((b) => {
            const name = [b.firstName, b.middleName].filter(Boolean).join(" ");
            const last = b.lastName || "-";
            const docType = b.documentType
              ? `${b.documentType?.code ?? ""}${b.documentType?.name ? " – " + b.documentType.name : ""}`
              : "-";
            return (
              <tr key={b.id}>
                <td>{b.id}</td>
                <td><strong>{name || "-"}</strong></td>
                <td>{last}</td>
                <td>{docType}</td>
                <td>{b.documentNumber ?? "-"}</td>
                <td>{b.email ?? "-"}</td>
                <td>{b.phone ?? "-"}</td>
                <td>{toDateLabel(b.createAt)}</td>
                <td>{toDateLabel(b.updatedAt)}</td>
                <td className="cell-actions">
                  <button
                    type="button"
                    className="btn-pill btn-edit"
                    onClick={() => onEdit?.(b)}
                  >
                    Editar
                  </button>
                  <button
                    type="button"
                    className="btn-pill btn-danger"
                    onClick={() => onDelete?.(b.id, name || b.id)}
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}