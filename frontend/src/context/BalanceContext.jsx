// src/context/BalanceContext.jsx
import React, { createContext, useContext, useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { fetchCurrentUser } from "../services/userService";
import { validatePendingRecharges } from "../services/rechargeAccountService";

const BalanceContext = createContext();

export const BalanceProvider = ({ children }) => {
  const [balance, setBalance] = useState(null);
  const location = useLocation();

  const loadBalance = async () => {
    try {
      const user = await fetchCurrentUser();
      await validatePendingRecharges(user.id);
      const newUser = await fetchCurrentUser();

      setBalance(newUser.balance);
    } catch (err) {
      console.error("Error al cargar el balance:", err);
    }
  };

  useEffect(() => {
    loadBalance();
  }, [location.pathname]);

  return (
    <BalanceContext.Provider value={{ balance, setBalance, loadBalance }}>
      {children}
    </BalanceContext.Provider>
  );
};

export const useBalance = () => {
  const context = useContext(BalanceContext);

  if (!context) {
    throw new Error("Hubo un error al usar el BalanceContext.");
  }

  return context;
};
