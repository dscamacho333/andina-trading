import { useEffect, useState } from "react";
import { getTransactionsByOrder } from "../../services/orderService";
import { formatDate } from "../../utils/FormatDate";
import styles from "./OrderTransactions.module.css";

export const OrderTransactions = ({ orderId }) => {
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const data = await getTransactionsByOrder(orderId);
        setTransactions(data);
      } catch (error) {
        console.error("Error al obtener transacciones", error);
      }
    };

    fetchTransactions();
  }, [orderId]);

  return (
    <div>
      <h1>Transacciones de Órdenes</h1>
      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Monto</th>
              <th>Tipo</th>
              <th>Descripción</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {transactions?.length > 0 ? (
              transactions.map((item, index) => (
                <tr key={index} className={styles.tableRow}>
                  <td>{formatDate(item.createdAt)}</td>
                  <td>${item.amount}</td>
                  <td>{item.type}</td>
                  <td>{item.description}</td>
                  <td>{item.status}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5">No hay datos en el portafolio.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};
