import { useUser } from "../context/UserContext";
import { fetchCurrentUser } from "../services/userService";

export const useRefreshUser = () => {
  const { setUser } = useUser();

const refreshUserData = async () => {
  try {
    const updatedUser = await fetchCurrentUser();
    localStorage.setItem("user", JSON.stringify(updatedUser));
    setUser(updatedUser);
  } catch (error) {
    console.error("Error al refrescar los datos del usuario:", error);
  }
};


  return { refreshUserData };
};