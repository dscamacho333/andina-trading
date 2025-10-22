export function CityForm({ onSubmit, onCancel, countries=[], economySituations=[], loadingCombos=false }) {
  const submit = (e) => {
    e.preventDefault();
    const name = e.target.name.value.trim();
    const countryId = e.target.countryId.value;
    const economySituationId = e.target.economySituationId.value;
    if (!name || !countryId || !economySituationId) return;
    onSubmit?.({ name, countryId, economySituationId });
    e.target.reset();
  };

  return (
    <form className="form" onSubmit={submit}>
      <div className="form-row">
        <label>Nombre</label>
        <input name="name" placeholder="Bogotá" />
      </div>

      <div className="form-row">
        <label>País</label>
        <select name="countryId" className="select" disabled={loadingCombos} defaultValue="">
          <option value="" disabled>Seleccione…</option>
          {countries.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
        </select>
      </div>

      <div className="form-row">
        <label>Situación Económica</label>
        <select name="economySituationId" className="select" disabled={loadingCombos} defaultValue="">
          <option value="" disabled>Seleccione…</option>
          {economySituations.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
        </select>
      </div>

      <div className="form-actions">
        <button type="button" className="btn btn-ghost" onClick={onCancel}>Cancelar</button>
        <button type="submit" className="btn btn-primary">Guardar</button>
      </div>
    </form>
  );
}