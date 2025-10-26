import React, { createContext, useState, useEffect, useContext } from "react";
import { getToken } from "../services/authService";
import { fetchCurrentUser } from "../services/userService";

// Crear el contexto
export const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadUser = async () => {
      const token = getToken();
      if (token) {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
          setUser(JSON.parse(storedUser)); 
        } else {
          try {
            const currentUser = await fetchCurrentUser();
            setUser(currentUser);
            localStorage.setItem("user", JSON.stringify(currentUser)); 
          } catch (err) {
            console.error("Error cargando usuario:", err);
          }
        }
      }
      setLoading(false);
    };

    loadUser();
  }, []);

  useEffect(() => {
    if (user) {
      localStorage.setItem("user", JSON.stringify(user));
    }
  }, [user]);

  return (
    <UserContext.Provider value={{ user, setUser, loading }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);

  if (!context) {
    throw new Error("Hubo un error al usar el UserContext.");
  }

  return context;
};
