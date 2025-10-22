import { useEffect, useState } from "react";

export function CitiesTable({
  cities=[], countries=[], economySituations=[],
  loading=false, error=null,
  onEdit, onSaveEdit, onCancelEdit, onDelete,
  editingCity, editTempData, onEditTempChange,
}) {
  const [local, setLocal] = useState(editTempData || {});
  useEffect(()=>{ setLocal(editTempData || {}); }, [editTempData, editingCity]);

  if (loading) return <div className="subtitle">Cargando ciudades…</div>;
  if (error) return <div className="subtitle">Error: {String(error)}</div>;
  if (!cities.length) return <div className="subtitle">No hay ciudades.</div>;

  return (
    <table className="table-dark">
      <thead>
        <tr>
          <th style={{width:70}}>ID</th>
          <th>Ciudad</th>
          <th>País</th>
          <th style={{width:120}}>Código País</th>
          <th>Situación Económica</th>
          <th>Descripción</th>
          <th style={{width:200}}>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {cities.map(c => {
          const editing = editingCity === c.id;
          const countryName = c.country?.name ?? "-";
          const countryCode = c.country?.code ?? "-";
          const econName = c.economySituation?.name ?? "-";
          const econDesc = c.economySituation?.description ?? "-";

          return (
            <tr key={c.id}>
              <td>{c.id}</td>

              {/* Ciudad */}
              <td>
                {editing ? (
                  <input
                    value={local.name ?? c.name ?? ""}
                    onChange={(e)=>{ setLocal(s=>({ ...s, name:e.target.value })); onEditTempChange?.('name', e.target.value); }}
                  />
                ) : <strong>{c.name}</strong>}
              </td>

              {/* País */}
              <td>
                {editing ? (
                  <select
                    className="select"
                    value={local.countryId ?? c.country?.id ?? ""}
                    onChange={(e)=>{ setLocal(s=>({ ...s, countryId:e.target.value })); onEditTempChange?.('countryId', e.target.value); }}
                  >
                    <option value="">Seleccione…</option>
                    {countries.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                  </select>
                ) : countryName}
              </td>

              {/* Código País (solo lectura) */}
              <td>{countryCode}</td>

              {/* Situación */}
              <td>
                {editing ? (
                  <select
                    className="select"
                    value={local.economySituationId ?? c.economySituation?.id ?? ""}
                    onChange={(e)=>{ setLocal(s=>({ ...s, economySituationId:e.target.value })); onEditTempChange?.('economySituationId', e.target.value); }}
                  >
                    <option value="">Seleccione…</option>
                    {economySituations.map(es => <option key={es.id} value={es.id}>{es.name}</option>)}
                  </select>
                ) : econName}
              </td>

              {/* Descripción (solo lectura) */}
              <td className="cut">{econDesc}</td>

              {/* Acciones */}
              <td className="cell-actions">
                {editing ? (
                  <>
                    <button className="btn btn-ghost" type="button" onClick={()=>onSaveEdit?.(c.id)}>Guardar</button>
                    <button className="btn btn-danger" type="button" onClick={onCancelEdit}>Cancelar</button>
                  </>
                ) : (
                  <>
                    <button className="btn btn-ghost" type="button" onClick={()=>onEdit?.(c)}>Editar</button>
                    <button className="btn btn-danger" type="button" onClick={()=>onDelete?.(c.id, c.name)}>Eliminar</button>
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