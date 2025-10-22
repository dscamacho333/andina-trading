// src/components/Forms/IssuerForm.jsx
import { useState, useEffect } from "react";

export function IssuerForm({
  onSubmit,
  onCancel,
  countries = [],
  industries = [],
  defaultValues = null,   // opcional: si editas, pásalo con los valores actuales
}) {
  const [form, setForm] = useState({
    name: "",
    ticker: "",
    countryId: "",
    industryId: "",
    website: "",
    notes: "",
  });

  useEffect(() => {
    if (defaultValues) {
      setForm({
        name: defaultValues.name ?? "",
        ticker: defaultValues.ticker ?? "",
        countryId: defaultValues.country?.id ?? "",
        industryId: defaultValues.industry?.id ?? "",
        website: defaultValues.website ?? "",
        notes: defaultValues.notes ?? "",
      });
    }
  }, [defaultValues]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((s) => ({ ...s, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name || !form.ticker || !form.countryId || !form.industryId || !form.website) return;

    // payload para el backend
    const payload = {
      name: form.name.trim(),
      ticker: form.ticker.trim(),
      country: { id: parseInt(form.countryId) },
      industry: { id: parseInt(form.industryId) },
      website: form.website.trim(),   // sin limite de 10 chars
      notes: form.notes.trim(),
    };
    onSubmit?.(payload);
  };

  const isEditing = Boolean(defaultValues);

  return (
    <div className="card">
      <h3 className="block-title">{isEditing ? "Editar Emisor" : "Nuevo Emisor"}</h3>
      <p className="subtitle">
        {isEditing
          ? "Actualiza los datos del emisor."
          : "Completa la información y relaciónala con país e industria."}
      </p>

      <form className="form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label>Nombre</label>
          <input
            name="name"
            placeholder="Nombre del emisor"
            value={form.name}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Ticker</label>
          <input
            name="ticker"
            placeholder="ECO, BCO, etc."
            value={form.ticker}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>País</label>
          <select
            name="countryId"
            value={form.countryId}
            onChange={handleChange}
          >
            <option value="">Seleccione…</option>
            {countries.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-row">
          <label>Industria</label>
          <select
            name="industryId"
            value={form.industryId}
            onChange={handleChange}
          >
            <option value="">Seleccione…</option>
            {industries.map((i) => (
              <option key={i.id} value={i.id}>
                {i.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-row">
          <label>Website</label>
          <input
            name="website"
            placeholder="https://www.empresa.com"
            value={form.website}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Notas</label>
          <textarea
            name="notes"
            placeholder="Opcional"
            value={form.notes}
            onChange={handleChange}
          />
        </div>

        <div className="form-actions">
          <button type="button" className="btn" onClick={onCancel}>
            Cancelar
          </button>
          <button type="submit" className="btn btn-primary">
            Guardar
          </button>
        </div>
      </form>
    </div>
  );
}

export default IssuerForm;