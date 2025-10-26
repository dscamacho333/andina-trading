import React, { createContext, useContext, useState } from "react";
import { sidebarPresets } from "../config/sidebarConfig";

const SidebarContext = createContext();

export const SidebarProvider = ({ children }) => {
  const [sidebarItems, setSidebarItems] = useState(sidebarPresets.default);
  const [currentPreset, setCurrentPreset] = useState('default'); // <--- nuevo estado

  const setPreset = (presetKey) => {
    if (presetKey in sidebarPresets) {
      setSidebarItems(sidebarPresets[presetKey]);
      setCurrentPreset(presetKey); // <--- guarda el nombre del preset
    }
  };

  return (
    <SidebarContext.Provider value={{ sidebarItems, setPreset, currentPreset }}>
      {children}
    </SidebarContext.Provider>
  );
};

export const useSidebar = () => useContext(SidebarContext);