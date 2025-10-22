export function CountryForm({ onSubmit, onCancel }) {
  const submit = (e) => {
    e.preventDefault();
    const code = e.target.code.value.trim();
    const name = e.target.name.value.trim();
    if (!code || !name) return;
    onSubmit?.({ code, name });
    e.target.reset();
  };

  return (
    <form className="form" onSubmit={submit}>
      <div className="form-row">
        <label>Código</label>
        <input name="code" placeholder="CO" />
      </div>

      <div className="form-row">
        <label>Nombre</label>
        <input name="name" placeholder="Colombia" />
      </div>

      <div className="form-actions">
        <button type="button" className="btn btn-ghost" onClick={onCancel}>Cancelar</button>
        <button type="submit" className="btn btn-primary">Guardar</button>
      </div>
    </form>
  );
}