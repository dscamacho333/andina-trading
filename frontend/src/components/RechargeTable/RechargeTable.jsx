import { formatDate } from "../../utils/FormatDate";
import styles from "./RechargeTable.module.css";

export const RechargeTable = ({ rechargeHistory, isLoading }) => {
  return (
    <div className="container">
      <h3 style={{ textAlign: "center" }}>Historial de Recargas</h3>

      {isLoading ? (
        <p>Cargando historial de recargas...</p>
      ) : (
        <div className={styles.tableWrapper}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Monto</th>
                <th>Fecha</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {rechargeHistory.length > 0 ? (
                rechargeHistory.map((recharge, index) => (
                  <tr key={index} className={styles.tableRow}>
                    <td>${recharge.amount}</td>
                    <td>{formatDate(recharge.createdAt)}</td>
                    <td>{recharge.status}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4">No hay recargas disponibles.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};
