import React from "react";
import OrderCard from "../OrderCard/OrderCard";
import FilterInput from "./../FormFields/FilterInput/FilterInput";
import styles from "./OrderGallery.module.css";
import { getUserFromToken } from "../../services/authService";
import {
  updateOrderStatusByUser,
  getOrdersByUser,
} from "../../services/orderService";
import { OrderTransactions } from "../OrderTransactions/OrderTransactions";

const OrderGallery = () => {
  const [orders, setOrders] = React.useState([]);
  const [selectedOrder, setSelectedOrder] = React.useState(null);
  const [filteredOrders, setFilteredOrders] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [statusFilter, setStatusFilter] = React.useState("");
  const [symbolFilter, setSymbolFilter] = React.useState("");
  const [typeFilter, setTypeFilter] = React.useState("");

  React.useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    const user = getUserFromToken();
    if (!user) return;

    setSelectedOrder(null);
    await updateOrderStatusByUser(user.userId);
    const fetchedOrders = await getOrdersByUser(user.userId);
    fetchedOrders.reverse();

    setOrders(fetchedOrders);
    setLoading(false);
  };

  React.useEffect(() => {
    let result = [...orders];

    if (statusFilter) {
      result = result.filter((o) => o.alpacaStatus === statusFilter);
    }
    if (symbolFilter) {
      result = result.filter((o) => o.symbol === symbolFilter);
    }

    if (typeFilter) {
      result = result.filter((o) => o.orderType === typeFilter);
    }

    setFilteredOrders(result);
  }, [orders, statusFilter, typeFilter, symbolFilter]);

  if (loading) {
    return <div className={styles.loading}>Cargando órdenes...</div>;
  }

  const uniqueStatuses = [...new Set(orders.map((o) => o.alpacaStatus))];
  const uniqueTypes = [...new Set(orders.map((o) => o.orderType))];
  const uniqueSymbols = [...new Set(orders.map((o) => o.symbol))];

  return (
    <div>
      <h2>{selectedOrder ? "Detalle de orden" : "Mis órdenes"}</h2>

      {!selectedOrder && (
        <div style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
          <FilterInput
            label="Estado"
            options={uniqueStatuses}
            selected={statusFilter}
            onChange={setStatusFilter}
          />
          <FilterInput
            label="Tipo"
            options={uniqueTypes}
            selected={typeFilter}
            onChange={setTypeFilter}
          />
          <FilterInput
            label="Símbolo"
            options={uniqueSymbols}
            selected={symbolFilter}
            onChange={setSymbolFilter}
          />
        </div>
      )}

      <div className={styles.gallery}>
        {selectedOrder ? (
          <div className={styles.detailView}>
            <button
              className={styles.backButton}
              onClick={() => setSelectedOrder(null)}
            >
              ← Volver
            </button>
            <OrderCard
              key={selectedOrder.orderId}
              symbol={selectedOrder.symbol}
              qty={selectedOrder.quantity}
              status={selectedOrder.alpacaStatus}
              type={selectedOrder.orderType}
              price={
                selectedOrder.limitPrice || selectedOrder.filledPrice || "-"
              }
              timeInForce={selectedOrder.timeInForce}
              orderId={selectedOrder.orderId}
              orderType={selectedOrder.orderType}
              orderStatus={selectedOrder.orderStatus}
              createdAt={selectedOrder.createdAt}
              expanded={true}
              fetchOrders={fetchOrders}
            />

            <OrderTransactions orderId={selectedOrder.orderId} />
          </div>
        ) : filteredOrders.length === 0 ? (
          <p>No hay órdenes que coincidan con el filtro.</p>
        ) : (
          filteredOrders.map((order) => (
            <div key={order.orderId} onClick={() => setSelectedOrder(order)}>
              <OrderCard
                symbol={order.symbol}
                qty={order.quantity}
                status={order.alpacaStatus}
                type={order.orderType}
                price={order.limitPrice || order.filledPrice || "-"}
                timeInForce={order.timeInForce}
                createdAt={order.createdAt}
              />
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default OrderGallery;
