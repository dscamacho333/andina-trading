// src/components/Layout/IssuerManagement.jsx
import { useEffect, useMemo, useState } from "react";
import { issuerApi } from "../../services/issuerApi";
import IssuersTable from "../Tables/IssuersTable";
import IssuerForm from "../Forms/IssuerForm";

// UI (los mismos que usas en City/Country)
import { Notification } from "../UI/Notification";
import { ConfirmationModal } from "../UI/ConfirmationModal";
import { useNotification } from "../../hooks/useNotification";
import { useErrorHandler } from "../../hooks/useErrorHandler";

export default function IssuerManagement() {
  // data
  const [issuers, setIssuers] = useState([]);
  const [countries, setCountries] = useState([]);
  const [industries, setIndustries] = useState([]);

  // filtros (IDs en string para <select>)
  const [sectorId, setSectorId] = useState("");
  const [industryId, setIndustryId] = useState("");
  const [countryId, setCountryId] = useState("");

  // formulario
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null); // objeto emisor cuando se edita
  const isEditing = Boolean(editing);

  const [loading, setLoading] = useState(false);

  // notificaciones + errores + confirm
  const { notification, showNotification, hideNotification } = useNotification();
  const { handleError } = useErrorHandler();
  const [confirmation, setConfirmation] = useState(null);

  // cargar catálogos + lista
  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const [cs, ins, ems] = await Promise.all([
          issuerApi.getCountries(),
          issuerApi.getIndustries(),
          issuerApi.getIssuers(),
        ]);
        setCountries(cs || []);
        setIndustries(ins || []);
        setIssuers(ems || []);
      } catch (e) {
        handleError(e);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  // filtrar en cliente
  const filteredIssuers = useMemo(() => {
    return issuers.filter((i) => {
      const okSector = sectorId ? String(i.industry?.sector?.id) === String(sectorId) : true;
      const okIndustry = industryId ? String(i.industry?.id) === String(industryId) : true;
      const okCountry = countryId ? String(i.country?.id) === String(countryId) : true;
      return okSector && okIndustry && okCountry;
    });
  }, [issuers, sectorId, industryId, countryId]);

  const refreshIssuers = async () => {
    try {
      const list = await issuerApi.getIssuers();
      setIssuers(list || []);
    } catch (e) {
      handleError(e);
    }
  };

  // Abrir/cerrar form
  const openCreate = () => {
    setEditing(null);
    setShowForm(true);
    setTimeout(() => window.scrollTo({ top: 0, behavior: "smooth" }), 0);
  };

  const openEdit = (issuer) => {
    setEditing(issuer);
    setShowForm(true);
    setTimeout(() => window.scrollTo({ top: 0, behavior: "smooth" }), 0);
  };

  const closeForm = () => {
    setEditing(null);
    setShowForm(false);
  };

  // submit del form (create / update)
  const handleSubmit = async (payload) => {
    try {
      setLoading(true);

      if (isEditing) {
        await issuerApi.updateIssuer(editing.id, payload);
        showNotification("Emisor actualizado", "success");
      } else {
        await issuerApi.createIssuer(payload);
        showNotification("Emisor creado", "success");
      }

      await refreshIssuers();
      closeForm();
    } catch (e) {
      handleError(e);
    } finally {
      setLoading(false);
    }
  };

  // borrar con modal bonito
  const handleDelete = (id, name) => {
    setConfirmation({
      title: "Confirmar eliminación",
      message: `¿Eliminar el emisor "${name}"? Esta acción no se puede deshacer.`,
      onConfirm: async () => {
        try {
          setLoading(true);
          await issuerApi.deleteIssuer(id);
          await refreshIssuers();
          showNotification("Emisor eliminado", "success");
        } catch (e) {
          handleError(e);
        } finally {
          setLoading(false);
          setConfirmation(null);
        }
      },
    });
  };

  // sectores únicos (derivados de industries)
  const sectors = useMemo(() => {
    const map = new Map();
    for (const ind of industries) {
      const s = ind.sector;
      if (s && !map.has(s.id)) map.set(s.id, s);
    }
    return Array.from(map.values());
  }, [industries]);

  return (
    <>
      {/* Notificación global */}
      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={hideNotification}
        />
      )}

      {/* Modal confirmación */}
      {confirmation && (
        <ConfirmationModal
          isOpen={true}
          title={confirmation.title}
          message={confirmation.message}
          onConfirm={confirmation.onConfirm}
          onCancel={() => setConfirmation(null)}
        />
      )}

      {/* Título del bloque */}
      <h2 className="block-title">Emisores</h2>
      <p className="subtitle">Filtra, crea y administra emisores.</p>

      {/* Filtros + Nuevo */}
      <div className="toolbar">
        <div className="filters-row">
          {/* Sector */}
          <select value={sectorId} onChange={(e) => setSectorId(e.target.value)}>
            <option value="">Sector: Todos</option>
            {sectors.map((s) => (
              <option key={s.id} value={s.id}>{s.name}</option>
            ))}
          </select>

          {/* Industria (filtra por sector elegido) */}
          <select value={industryId} onChange={(e) => setIndustryId(e.target.value)}>
            <option value="">Industria: Todas</option>
            {industries
              .filter((i) => (sectorId ? String(i.sector?.id) === String(sectorId) : true))
              .map((i) => (
                <option key={i.id} value={i.id}>
                  {i.name}
                </option>
              ))}
          </select>

          {/* País */}
          <select value={countryId} onChange={(e) => setCountryId(e.target.value)}>
            <option value="">País: Todos</option>
            {countries.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>

          <button type="button" className="btn-primary" onClick={openCreate}>
            Nuevo Emisor
          </button>
        </div>
      </div>

      {/* Formulario (crear / editar) */}
      {showForm && (
        <div className="card">
          <IssuerForm
            onSubmit={handleSubmit}
            onCancel={closeForm}
            countries={countries}
            industries={industries}
            defaultValues={editing || undefined}
          />
        </div>
      )}

      {/* Tabla */}
      <IssuersTable
        issuers={filteredIssuers}
        onEdit={openEdit}
        onDelete={handleDelete}
      />

      {loading && <p className="subtitle" style={{ marginTop: 10 }}>Procesando…</p>}
    </>
  );
}