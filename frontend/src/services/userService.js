// src/services/userService.js
import axios from "axios";
import { saveAuthData, getToken } from "./authService";

const API_URL = "http://localhost:8080/api/investor"; // Ajustar si cambia el puerto

// Registrar nuevo usuario
export const registerUser = async (userData) => {
  try {
    const response = await axios.post(API_URL, userData); // POST /api/users
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Error en el registro");
  }
};

// Log in User
export const loginUser = async (credentials) => {
  try {
    const response = await axios.post(`${API_URL}/login`, credentials);

    const { token, user, roles } = response.data;

    if (token) {
      saveAuthData(token, user, roles);
    }

    return response.data;

  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Error en el inicio de sesión"
    );
  }
};

export const fetchCurrentUser = async () => {
  const token = localStorage.getItem('token');
  const response = await fetch(`${API_URL}/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error('No se pudo obtener el usuario');
  }

  return await response.json();
};

// Obtener todos los usuarios
export const getAllUsers = async () => {
  try {
    const response = await axios.get(API_URL); // GET /api/users
    return response.data;
  } catch (error) {
    throw new Error("Error al obtener usuarios");
  }
};

// Buscar usuario por email
export const getUserByEmail = async (email) => {
  try {
    const response = await axios.get(`${API_URL}/find`, {
      params: { email }, // GET /api/users/find?email=...
    });
    return response.data;
  } catch (error) {
    throw new Error("Usuario no encontrado");
  }
};

// Verificación simple (test endpoint opcional)
export const testConnection = async () => {
  try {
    const response = await axios.get(`${API_URL}/test`);
    return response.data;
  } catch (error) {
    throw new Error("No se pudo conectar con el backend");
  }
};

export const syncAccount = async (accountId) => {
  const token = getToken();
  if (!token) throw new Error("Usuario no autenticado");

  try {
    const response = await axios.put(
      `${API_URL}/account/sync?accountId=${accountId}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Error al sincronizar cuenta"
    );
  }
};
