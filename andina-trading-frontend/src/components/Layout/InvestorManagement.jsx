// src/components/Layout/InvestorManagement.jsx
import { useEffect, useMemo, useState } from "react";
import { investorApi } from "../../services/investorApi";
import InvestorsTable from "../Tables/InvestorsTable";

// Reutiliza tu UI base
import { Notification } from "../UI/Notification";
import { ConfirmationModal } from "../UI/ConfirmationModal";
import { useNotification } from "../../hooks/useNotification";
import { useErrorHandler } from "../../hooks/useErrorHandler";

// Estados válidos del backend
const STATUS_OPTIONS = ["ACTIVE", "INACTIVE", "RESTRICTED", "PENDING"];
const STATUS_LABEL = {
  ACTIVE: "ACTIVO",
  INACTIVE: "INACTIVO",
  RESTRICTED: "RESTRINGIDO",
  PENDING: "PENDIENTE",
};

export default function InvestorManagement() {
  const [investors, setInvestors] = useState([]);

  // ✅ Por defecto ACTIVE
  const [status, setStatus] = useState("ACTIVE");
  const [searchId, setSearchId] = useState("");

  const [loading, setLoading] = useState(false);
  const { notification, showNotification, hideNotification } = useNotification();
  const { handleError } = useErrorHandler();
  const [confirmation, setConfirmation] = useState(null);

  // Cargar cuando cambia el estado (y también en el primer render con ACTIVE)
  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const list = await investorApi.getByStatus(status);
        setInvestors(Array.isArray(list) ? list : []);
      } catch (e) {
        handleError(e);
      } finally {
        setLoading(false);
      }
    })();
  }, [status]);

  const handleSearchById = async () => {
    if (!searchId) return;
    try {
      setLoading(true);
      const inv = await investorApi.getById(searchId);
      setInvestors(inv ? [inv] : []);
      showNotification("Búsqueda completada", "success");
    } catch (e) {
      handleError(e);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setSearchId("");
    setStatus("ACTIVE");           // ✅ vuelve a ACTIVE
    // No hace falta setInvestors([]); el useEffect recarga ACTIVE
  };

  const handleDelete = (id, label) => {
    setConfirmation({
      title: "Confirmar eliminación",
      message: `¿Eliminar al inversionista "${label}"? Esta acción no se puede deshacer.`,
      onConfirm: async () => {
        try {
          setLoading(true);
          await investorApi.deleteById(id);
          const list = await investorApi.getByStatus(status); // refresca el estado actual
          setInvestors(Array.isArray(list) ? list : []);
          showNotification("Inversionista eliminado", "success");
        } catch (e) {
          handleError(e);
        } finally {
          setLoading(false);
          setConfirmation(null);
        }
      },
    });
  };

  const data = useMemo(() => investors, [investors]);

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

      {/* Confirm */}
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
      <h2 className="block-title">Inversionistas</h2>
      <p className="subtitle">Consulta por estado de cuenta, busca por ID y elimina.</p>

      {/* Toolbar */}
      <div className="toolbar">
        <div className="filters-row" style={{ gridTemplateColumns: "1fr 1fr auto auto" }}>
          {/* Filtro por estado (default ACTIVE) */}
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            {STATUS_OPTIONS.map((s) => (
              <option key={s} value={s}>
                {STATUS_LABEL[s] ?? s}
              </option>
            ))}
          </select>

          {/* Buscar por ID */}
          <div>
            <input
              placeholder="Buscar por ID"
              value={searchId}
              onChange={(e) => setSearchId(e.target.value)}
              style={{
                width: "100%",
                background: "var(--card-2)",
                color: "var(--text)",
                border: "1px solid var(--border-2)",
                borderRadius: 10,
                padding: "10px 12px",
              }}
            />
          </div>

          <button type="button" className="btn-primary" onClick={handleSearchById}>
            Buscar
          </button>

          <button type="button" onClick={handleClear}>
            Limpiar
          </button>
        </div>
      </div>

      {/* Tabla */}
      <InvestorsTable investors={data} onDelete={handleDelete} />

      {loading && <p className="subtitle" style={{ marginTop: 10 }}>Procesando…</p>}
    </>
  );
}