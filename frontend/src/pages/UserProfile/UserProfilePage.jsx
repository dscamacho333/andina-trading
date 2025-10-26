import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { Loading } from "../../components/Loading/Loading";
import { updateUserPreferences } from "../../services/userConfigService";
import { useNotification } from "../../context/NotificationContext";
import { useRefreshUser } from "../../hooks/useRefreshUser";
import { useUser } from "../../context/UserContext"; 
import styles from "./UserProfilePage.module.css";

export const UserProfilePage = () => {
  const { refreshUserData } = useRefreshUser();
  const { user } = useUser(); // <- ahora sí lo usamos
  const [isLoading, setIsLoading] = useState(true);
  const { notify } = useNotification();

  const [preferences, setPreferences] = useState({
    dailyOrderLimit: 1,
    defaultOrderType: "MARKET",
  });

  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (user) {
      setPreferences({
        dailyOrderLimit: user.dailyOrderLimit || 1,
        defaultOrderType: user.defaultOrderType || "MARKET",
      });
      setIsLoading(false);
    }
  }, [user]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setPreferences((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    try {
      await updateUserPreferences({ ...preferences });
      notify("Preferencias actualizadas correctamente.", "success");
      refreshUserData();
    } catch (err) {
      notify("Error al actualizar preferencias. Inténtalo nuevamente.", "danger");
    } finally {
      setSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <DashboardLayout>
        <Loading message={"Cargando perfil de usuario..."} />
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className={styles.container}>
        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.section}>
            <h2 className={styles.title}>Límite diario de Operaciones</h2>
            <p className={styles.subtitle}>
              Indica cuántas operaciones quieres hacer máximo en un día
            </p>
            <div className={styles.box}>
              <div className={styles.formGroup}>
                <label className={styles.label} htmlFor="dailyOrderLimit">
                  Número máximo:
                </label>
                <select
                  name="dailyOrderLimit"
                  value={preferences.dailyOrderLimit}
                  onChange={handleChange}
                  className={styles.select}
                >
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                </select>
              </div>
            </div>
          </div>

          <div className={styles.section}>
            <h2 className={styles.title}>Orden predeterminada</h2>
            <p className={styles.subtitle}>
              Selecciona el tipo de orden que quieres ejecutar por defecto
            </p>
            <div className={styles.box}>
              <div className={styles.formGroup}>
                <label className={styles.label} htmlFor="defaultOrderType">
                  Tipo de Orden:
                </label>
                <select
                  name="defaultOrderType"
                  value={preferences.defaultOrderType}
                  onChange={handleChange}
                  className={styles.select}
                >
                  <option value="MARKET">MARKET</option>
                  <option value="LIMIT">LIMIT</option>
                  <option value="STOP">STOP</option>
                  <option value="STOP LIMIT">STOP LIMIT</option>
                </select>
              </div>
            </div>
          </div>

          <div className={styles.footer}>
            <button
              type="submit"
              className={styles.button}
              disabled={submitting}
            >
              {submitting ? "Guardando..." : "Guardar Preferencias"}
            </button>
          </div>
        </form>
      </div>
    </DashboardLayout>
  );
};