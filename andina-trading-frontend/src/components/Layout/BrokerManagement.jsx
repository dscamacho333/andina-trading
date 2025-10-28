// src/components/Layout/BrokerManagement.jsx
import { useEffect, useMemo, useState } from "react";
import { brokerApi } from "../../services/brokerApi";
import BrokersTable from "../Tables/BrokersTable";
import BrokerForm from "../Forms/BrokerForm";

// UI reutilizada (igual que City/Country/Issuers)
import { Notification } from "../UI/Notification";
import { ConfirmationModal } from "../UI/ConfirmationModal";
import { useNotification } from "../../hooks/useNotification";
import { useErrorHandler } from "../../hooks/useErrorHandler";

export default function BrokerManagement() {
  // data
  const [brokers, setBrokers] = useState([]);
  const [countries, setCountries] = useState([]);
  const [documentTypes, setDocumentTypes] = useState([]);

  // filtro SOLO por país
  const [countryId, setCountryId] = useState("");

  // form
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);
  const isEditing = Boolean(editing);
  const [loading, setLoading] = useState(false);

  // notification / error / confirm
  const { notification, showNotification, hideNotification } = useNotification();
  const { handleError } = useErrorHandler();
  const [confirmation, setConfirmation] = useState(null);

  // cargar catálogos + lista
  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const [cs, dts, bs] = await Promise.all([
          brokerApi.getCountries(),
          brokerApi.getDocumentTypes(),
          brokerApi.getBrokers(),
        ]);
        setCountries(cs || []);
        setDocumentTypes(dts || []);
        setBrokers(bs || []);
      } catch (e) {
        handleError(e);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const refreshBrokers = async () => {
    try {
      const list = await brokerApi.getBrokers();
      setBrokers(list || []);
    } catch (e) {
      handleError(e);
    }
  };

  // Filtro en cliente SOLO por país (del tipo de documento)
  const filteredBrokers = useMemo(() => {
    return brokers.filter((b) => {
      const okCountry = countryId
        ? String(b.documentType?.country?.id) === String(countryId)
        : true;
      return okCountry;
    });
  }, [brokers, countryId]);

  // open/close
  const openCreate = () => {
    setEditing(null);
    setShowForm(true);
    setTimeout(() => window.scrollTo({ top: 0, behavior: "smooth" }), 0);
  };
  const openEdit = (b) => {
    setEditing(b);
    setShowForm(true);
    setTimeout(() => window.scrollTo({ top: 0, behavior: "smooth" }), 0);
  };
  const closeForm = () => {
    setEditing(null);
    setShowForm(false);
  };

  // submit
  const handleSubmit = async (payload) => {
    try {
      setLoading(true);
      if (isEditing) {
        await brokerApi.updateBroker(editing.id, payload);
        showNotification("Broker actualizado", "success");
      } else {
        await brokerApi.createBroker(payload);
        showNotification("Broker creado", "success");
      }
      await refreshBrokers();
      closeForm();
    } catch (e) {
      handleError(e);
    } finally {
      setLoading(false);
    }
  };

  // delete
  const handleDelete = (id, label) => {
    setConfirmation({
      title: "Confirmar eliminación",
      message: `¿Eliminar el broker "${label}"? Esta acción no se puede deshacer.`,
      onConfirm: async () => {
        try {
          setLoading(true);
          await brokerApi.deleteBroker(id);
          await refreshBrokers();
          showNotification("Broker eliminado", "success");
        } catch (e) {
          handleError(e);
        } finally {
          setLoading(false);
          setConfirmation(null);
        }
      },
    });
  };

  return (
    <>
      {/* Notificación */}
      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={hideNotification}
        />
      )}

      {/* Confirmación */}
      {confirmation && (
        <ConfirmationModal
          isOpen={true}
          title={confirmation.title}
          message={confirmation.message}
          onConfirm={confirmation.onConfirm}
          onCancel={() => setConfirmation(null)}
        />
      )}

      {/* Título bloque */}
      <h2 className="block-title">Comisionistas</h2>
      <p className="subtitle">Filtra, crea y administra comisionistas.</p>

      {/* Filtros + Nuevo (solo País) */}
      <div className="toolbar">
        <div
          className="filters-row"
          style={{ gridTemplateColumns: "1fr auto" }} // forzar 2 columnas: select + botón
        >
          <select value={countryId} onChange={(e) => setCountryId(e.target.value)}>
            <option value="">País (del tipo doc): Todos</option>
            {countries.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>

          <button type="button" className="btn-primary" onClick={openCreate}>
            Nuevo Broker
          </button>
        </div>
      </div>

      {/* Formulario */}
      {showForm && (
        <div className="card">
          <BrokerForm
            onSubmit={handleSubmit}
            onCancel={closeForm}
            countries={countries}
            documentTypes={documentTypes}
            defaultValues={editing || undefined}
          />
        </div>
      )}

      {/* Tabla */}
      <BrokersTable
        brokers={filteredBrokers}
        onEdit={openEdit}
        onDelete={handleDelete}
      />

      {loading && <p className="subtitle" style={{ marginTop: 10 }}>Procesando…</p>}
    </>
  );
}