// src/services/userConfigService.js
import axios from "axios";
import { getToken } from "./authService";

const API_URL = "http://localhost:8080/api/investor"; // Ajustar si cambia el puerto

export const updateUserPreferences = async (preferences) => {
  const token = getToken();

  if (!token) throw new Error("Usuario no autenticado");

  try {
    const response = await axios.put(
      `${API_URL}/me/preferences`,
      preferences,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Error al actualizar preferencias"
    );
  }
};