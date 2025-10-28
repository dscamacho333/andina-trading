import React, { useEffect, useState } from "react";

import DashboardLayout from "../../layouts/DashboardLayout";
import { fetchCurrentUser } from "../../services/userService";
import { getOrdersSummary } from "../../services/orderService";
import { Loading } from "../../components/Loading/Loading";

import styles from "./OrdersPage.module.css";

const OrdersPage = () => {
  const [orders, setOrders] = useState([]);
  const [alpacaAccountId, setAlpacaAccountId] = useState("")
  const [statusFilter, setStatusFilter] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  const fetchOrders = async () => {
    try {
      setIsLoading(true);
      const data = await getOrdersSummary(alpacaAccountId, statusFilter);
      setOrders(data);
    } catch (error) {
      console.error("Error al obtener órdenes:", error);
      setOrders([]);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchMe = async () => {
    try {
      setIsLoading(true);
      const data = await fetchCurrentUser();
      setAlpacaAccountId(data.alpacaAccountId);
    } catch (error) {
      console.error("Error al obtener el usuario actual:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchMe();
  }, []);

  useEffect(() => {
    alpacaAccountId && fetchOrders();
  }, [statusFilter, alpacaAccountId]);

  return (
    <DashboardLayout>
      <div className={styles.container}>
        <h2 className={styles.title}>Historial de Órdenes</h2>

        <div className={styles.filterContainer}>
          <label>Filtrar por estado:</label>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className={styles.select}
          >
            <option value="">Todos</option>
            <option value="filled">Filled</option>
            <option value="new">New</option>
            <option value="canceled">Canceled</option>
          </select>
        </div>

        {isLoading ? (
          <Loading message="Cargando historial de órdenes..." />
        ) : orders.length === 0 ? (
          <p>No hay órdenes registradas.</p>
        ) : (
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Símbolo</th>
                <th>Cantidad</th>
                <th>Precio Promedio</th>
                <th>Tipo</th>
                <th>Estado</th>
                <th>Creación</th>
                <th>Ejecución</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.orderId}>
                  <td>{order.orderId}</td>
                  <td>{order.symbol}</td>
                  <td>{order.qty}</td>
                  <td>{order.filledAvgPrice}</td>
                  <td>{order.orderType}</td>
                  <td>{order.status}</td>
                  <td>{new Date(order.createdAt).toLocaleString()}</td>
                  <td>
                    {order.executedAt
                      ? new Date(order.executedAt).toLocaleString()
                      : "—"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </DashboardLayout>
  );
};

export default OrdersPage;
