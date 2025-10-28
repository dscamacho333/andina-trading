import { getUserFromToken } from "./authService";
import axios from "axios";

export const sellPosition = async (data) => {
  const user = getUserFromToken();


  const API_URL = 'http://localhost:8080/api/orders/sell';

  const payload = {
    user: user,
    orderType: data.orderType,
    quantity: data.quantity,
    timeInForce: data.timeInForce,
    limitPrice: data.limitPrice,
    stopPrice: data.stopPrice,
    requiresSignature: false,
    stock: { symbol: data.position.symbol },
  };

  const response = await axios.post(API_URL, payload);
  return response.data;
};
