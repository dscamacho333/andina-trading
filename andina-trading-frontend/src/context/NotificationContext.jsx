import { createContext, useContext, useMemo, useState } from 'react';

const NotificationContext = createContext(null);

export function NotificationProvider({ children }) {
  const [notification, setNotification] = useState(null);

  const showNotification = (message, type = 'info') => {
    setNotification({ message, type });
  };

  const hideNotification = () => setNotification(null);

  const value = useMemo(() => ({
    notification,
    showNotification,
    hideNotification
  }), [notification]);

  return (
    <NotificationContext.Provider value={value}>
      {children}
    </NotificationContext.Provider>
  );
}

export function useNotificationContext() {
  const ctx = useContext(NotificationContext);
  if (!ctx) {
    throw new Error('useNotificationContext must be used within a NotificationProvider');
  }
  return ctx;
}
