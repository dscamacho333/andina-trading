import React from "react";
import styles from "./OrderCard.module.css";
import { formatDate } from "../../utils/FormatDate";
import { cancelOrder } from "../../services/orderService";
import Swal from "sweetalert2";

const OrderCard = ({
  symbol,
  qty,
  status,
  type,
  price,
  timeInForce,
  orderId,
  orderType,
  createdAt,
  expanded = false,
  fetchOrders
}) => {
  const cancelButtonStatuses = [
    "NEW",
    "SENT",
    "ACCEPTED",
    "PENDING",
    "PARTIALLY_FILLED",
  ];

  const cancelOrderById = () => {
    cancelOrder(orderId);

      Swal.fire({
          icon: "success",
          itle: "Su orden ha sido cancelada",
          text: "Recibira una confirmación en su correo",
      });

      fetchOrders();

      expanded = false;
      status = "CANCELED";
  };

  return (
    <div className={`${styles.card} ${expanded ? styles.expanded : ""}`}>
      <div className={styles.header}>
        <h3>{symbol}</h3>
        <span className={styles.status}>{status}</span>
      </div>
        <p className={styles.text}>
            <strong>Creada:</strong> {formatDate(createdAt)}
        </p>
      <p className={styles.text}>
        <strong>Cantidad:</strong> {qty}
      </p>
      <p className={styles.text}>
        <strong>Tipo:</strong> {type}
      </p>
      <p className={styles.text}>
        <strong>Precio:</strong> {price}
      </p>
      <p className={styles.text}>
        <strong>Duración:</strong> {timeInForce}
      </p>

      {expanded && (
        <div className={styles.detailInfo}>
          <p className={styles.text}>
            <strong>Order ID:</strong> {orderId}
          </p>
          <p className={styles.text}>
            <strong>Tipo de orden:</strong> {orderType}
          </p>

          <div>
            {cancelButtonStatuses.includes(status) && (
              <button
                className={styles.cancelButton}
                onClick={() => cancelOrderById()}
              >
                Cancelar
              </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default OrderCard;
