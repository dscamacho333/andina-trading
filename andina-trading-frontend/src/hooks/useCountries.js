import { useState, useEffect } from 'react';
import { apiService } from '../services/api';
import { useErrorHandler } from './useErrorHandler';

export const useCountries = () => {
  const [countries, setCountries] = useState([]);
  const [loading, setLoading] = useState(true);
  const { handleError } = useErrorHandler();

  const fetchCountries = async () => {
    try {
      setLoading(true);
      const data = await apiService.getCountries();
      setCountries(data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCountries();
  }, []);

  return {
    countries,
    loading,
    refreshCountries: fetchCountries
  };
};