// src/services/accountService.js

import axios from 'axios';

const API_URL = 'http://localhost:8080/api/accounts'; // Ajusta esta URL si usas proxy o variable de entorno

/**
 * Envía la solicitud de creación de cuenta a Alpaca vía backend
 * @param {Object} accountData 
 */
export const createAccount = async (accountData) => {
  try {
    const response = await axios.post(`${API_URL}/create`, accountData);
    return response.data;
  } catch (error) {
    // Extrae mensaje legible si viene del backend
    const message = error?.response?.data?.message || error.message || 'Error desconocido';
    throw new Error(message);
  }
};
