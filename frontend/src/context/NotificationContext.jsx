import React, { createContext, useContext, useState, useCallback } from "react";
import AppNotification from "../components/AppNotification/AppNotification";

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
  const [message, setMessage] = useState(null);
  const [type, setType] = useState("danger");

  const notify = useCallback((msg, type = "danger") => {
    setMessage(msg);
    setType(type);

    setTimeout(() => {
      setMessage(null);
    }, 4000);
  }, []);

  const closeNotification = () => setMessage(null);

  return (
    <NotificationContext.Provider value={{ notify }}>
      {children}
      <AppNotification message={message} type={type} onClose={closeNotification} />
    </NotificationContext.Provider>
  );
};

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error("useNotification debe usarse dentro de NotificationProvider");
  }
  return context;
};