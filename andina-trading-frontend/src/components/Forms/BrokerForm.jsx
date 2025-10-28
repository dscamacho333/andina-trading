// src/components/Forms/BrokerForm.jsx
import { useEffect, useState } from "react";

export default function BrokerForm({
  onSubmit,
  onCancel,
  countries = [],
  documentTypes = [],
  defaultValues = null, // si editas, pásalo
}) {
  const [form, setForm] = useState({
    firstName: "",
    middleName: "",
    lastName: "",
    documentTypeId: "",
    documentNumber: "",
    email: "",
    phone: "",
  });

  useEffect(() => {
    if (defaultValues) {
      setForm({
        firstName: defaultValues.firstName ?? "",
        middleName: defaultValues.middleName ?? "",
        lastName: defaultValues.lastName ?? "",
        documentTypeId: defaultValues.documentType?.id ?? "",
        documentNumber: defaultValues.documentNumber ?? "",
        email: defaultValues.email ?? "",
        phone: defaultValues.phone ?? "",
      });
    }
  }, [defaultValues]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((s) => ({ ...s, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.firstName || !form.lastName || !form.documentTypeId || !form.documentNumber || !form.email || !form.phone) return;

    const payload = {
      firstName: form.firstName.trim(),
      middleName: form.middleName.trim(),
      lastName: form.lastName.trim(),
      documentType: { id: parseInt(form.documentTypeId) },
      documentNumber: form.documentNumber.trim(),
      email: form.email.trim(),
      phone: form.phone.trim(),
    };

    onSubmit?.(payload);
  };

  const isEditing = Boolean(defaultValues);

  return (
    <div className="card">
      <h3 className="block-title">{isEditing ? "Editar Broker" : "Nuevo Broker"}</h3>
      <p className="subtitle">
        {isEditing ? "Actualiza la información del comisionista." : "Completa la información del comisionista."}
      </p>

      <form className="form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label>Nombres</label>
          <input
            name="firstName"
            placeholder="Nombre"
            value={form.firstName}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Segundo nombre (opcional)</label>
          <input
            name="middleName"
            placeholder="Segundo nombre"
            value={form.middleName}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Apellidos</label>
          <input
            name="lastName"
            placeholder="Apellidos"
            value={form.lastName}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Tipo de documento</label>
          <select
            name="documentTypeId"
            value={form.documentTypeId}
            onChange={handleChange}
          >
            <option value="">Seleccione…</option>
            {documentTypes.map((d) => {
              const prefix = d.country?.name ? `${d.country.name} • ` : "";
              const label = `${prefix}${d.code} – ${d.name}`;
              return (
                <option key={d.id} value={d.id}>
                  {label}
                </option>
              );
            })}
          </select>
        </div>

        <div className="form-row">
          <label>Número de documento</label>
          <input
            name="documentNumber"
            placeholder="Documento"
            value={form.documentNumber}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Email</label>
          <input
            name="email"
            type="email"
            placeholder="correo@dominio.com"
            value={form.email}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label>Teléfono</label>
          <input
            name="phone"
            placeholder="+57 300 000 0000"
            value={form.phone}
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