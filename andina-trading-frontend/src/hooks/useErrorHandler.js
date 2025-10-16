import { useNotification } from './useNotification';

export const useErrorHandler = () => {
  const { showNotification } = useNotification();

  const handleError = (error) => {
    console.log('🔍 Error handler received:', error);
    if (error?.message) {
      showNotification(error.message, 'error');
    } else {
      showNotification('Ha ocurrido un error inesperado', 'error');
    }
  };

  return { handleError };
};
