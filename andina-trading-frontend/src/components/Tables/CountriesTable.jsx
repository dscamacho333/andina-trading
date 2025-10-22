import { useState, useEffect } from "react";

export function CountriesTable({
  countries=[],
  onEdit, onSaveEdit, onCancelEdit, onDelete,
  editingCountry, editTempData, onEditTempChange,
}) {
  const [local, setLocal] = useState(editTempData || {});
  useEffect(()=>{ setLocal(editTempData || {}); }, [editTempData, editingCountry]);

  if (!countries.length) return <div className="subtitle">No hay países.</div>;

  return (
    <table className="table-dark">
      <thead>
        <tr>
          <th style={{width:80}}>ID</th>
          <th style={{width:140}}>Código</th>
          <th>Nombre</th>
          <th style={{width:200}}>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {countries.map(p => {
          const editing = editingCountry === p.id;
          return (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>
                {editing ? (
                  <input
                    value={local.code ?? p.code ?? ""}
                    onChange={(e)=>{ setLocal(s=>({ ...s, code:e.target.value })); onEditTempChange?.('code', e.target.value); }}
                  />
                ) : <span>{p.code}</span>}
              </td>
              <td>
                {editing ? (
                  <input
                    value={local.name ?? p.name ?? ""}
                    onChange={(e)=>{ setLocal(s=>({ ...s, name:e.target.value })); onEditTempChange?.('name', e.target.value); }}
                  />
                ) : <strong>{p.name}</strong>}
              </td>
              <td className="cell-actions">
                {editing ? (
                  <>
                    <button className="btn btn-ghost" type="button" onClick={()=>onSaveEdit?.(p.id)}>Guardar</button>
                    <button className="btn btn-danger" type="button" onClick={onCancelEdit}>Cancelar</button>
                  </>
                ) : (
                  <>
                    <button className="btn btn-ghost" type="button" onClick={()=>onEdit?.(p)}>Editar</button>
                    <button className="btn btn-danger" type="button" onClick={()=>onDelete?.(p.id, p.name)}>Eliminar</button>
                  </>
                )}
              </td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}