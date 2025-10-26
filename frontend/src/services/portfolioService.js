// src/services/portfolioService.js
import api from './api';

export const getPortfolio = async (accountId) => {
  const response = await api.get(`/portfolios/${accountId}`);
  return response.data;
};

export const getPortfolioPositions = async (accountId) => {
  const response = await api.get(`/portfolios/${accountId}/positions`);
  return response.data;
};