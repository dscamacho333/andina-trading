// src/components/Tables/IssuersTable.jsx
export function IssuersTable({
  issuers = [],
  onEdit,
  onDelete,
}) {
  if (!Array.isArray(issuers) || issuers.length === 0) {
    return <div className="subtitle">No hay emisores para mostrar.</div>;
  }

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th style={{ width: 60 }}>ID</th>
            <th>Nombre</th>
            <th style={{ width: 90 }}>Ticker</th>
            <th style={{ width: 160 }}>País</th>
            <th>Industria</th>
            <th>Website</th>
            <th style={{ width: 60 }}>Notas</th>
            <th style={{ width: 180 }}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {issuers.map((e) => (
            <tr key={e.id}>
              <td>{e.id}</td>
              <td><strong>{e.name}</strong></td>
              <td>{e.ticker}</td>
              <td>{e.country?.name ?? "-"}</td>
              <td>
                {e.industry?.sector?.name
                  ? `${e.industry?.sector?.name} • ${e.industry?.name}`
                  : e.industry?.name ?? "-"}
              </td>
              <td>
                {e.website ? (
                  <a href={/^https?:\/\//i.test(e.website) ? e.website : `https://${e.website}`} target="_blank" rel="noreferrer">
                    {e.website}
                  </a>
                ) : "-"}
              </td>
              <td>{e.notes ?? "-"}</td>
              <td className="cell-actions">
                <button type="button" className="btn-pill btn-edit" onClick={() => onEdit?.(e)}>
                  Editar
                </button>
                <button type="button" className="btn-pill btn-danger" onClick={() => onDelete?.(e.id, e.name)}>
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default IssuersTable;