// hooks/useNotification.js
import { useNotificationContext } from '../context/NotificationContext';

export const useNotification = () => {
  // ¡Ahora lee/escribe en EL MISMO estado global!
  return useNotificationContext();
};
