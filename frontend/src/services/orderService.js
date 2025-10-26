import axios from "axios";
import { getUserFromToken } from "../services/authService";

const API_URL = "http://localhost:8080";

export const fetchStocks = async () => {
  const response = await fetch(`${API_URL}/api/stocks`);
  const data = await response.json();
  return data;
};

export const createBuyOrder = async (
  stock,
  quantity,
  orderType,
  timeInForce,
  limitPrice,
  stopPrice
) => {
  const user = getUserFromToken();

  const payload = {
    user,
    stock,
    orderType,
    timeInForce,
    quantity,
    limitPrice,
    stopPrice,
  };

  const response = await axios.post(`${API_URL}/api/orders/buy`, payload);
  return response.data;
};

export const updateOrderStatusByUser = async (userId) => {
  const response = await axios.get(
    `${API_URL}/api/orders/update-pending/${userId}`
  );
  if (response.status !== 200) {
    throw new Error("Error al obtener el estado de la orden");
  }
};

export const getOrdersSummary = async (accountId, status) => {
  try {
    const response = await axios.get(`${API_URL}/api/orders/summary`, {
      params: {
        accountId,
        ...(status ? { status } : {}),
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error al obtener los detalles de la orden", error);
    throw error;
  }
};

export const getOrdersByUser = async (userId) => {
  try {
    const response = await axios.get(`${API_URL}/api/orders/user/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener órdenes por usuario", error);
    throw error;
  }
};

export const getFilteredOrdersByUser = async (
  userId,
  sentToAlpacaAt = null
) => {
  try {
    const params = { userId };
    if (sentToAlpacaAt) params.sentToAlpacaAt = sentToAlpacaAt;

    const response = await axios.get(`${API_URL}/api/orders/filtered`, {
      params,
    });
    return response.data; // Lista de órdenes
  } catch (error) {
    console.error("Error al obtener órdenes filtradas", error);
    throw error;
  }
};

export const cancelOrder = async (orderId) => {
  try {
    await axios.delete(`${API_URL}/api/orders/cancel/${orderId}`);
  } catch (error) {
    console.error("Error al cancelar una orden", error);
  }
};

export const getTransactionsByOrder = async (orderId) => {
  try {
    const response = await axios.get(
      `${API_URL}/api/transactions/order/${orderId}`
    );
    return response.data; // Lista de transacciones
  } catch (error) {
    console.error("Error al obtener transacciones por orden", error);
    throw error;
  }
};
