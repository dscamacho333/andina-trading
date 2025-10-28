// src/services/brokerApi.js
const BASE = "/api/v1";

async function http(method, url, body) {
  const res = await fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export const brokerApi = {
  // Brokers
  getBrokers: () => http("GET", `${BASE}/broker`),
  getBrokersByCountry: (countryId) =>
    http("GET", `${BASE}/broker/country/${countryId}`),
  createBroker: (payload) => http("POST", `${BASE}/broker`, payload),
  updateBroker: (id, payload) => http("PUT", `${BASE}/broker/${id}`, payload),
  deleteBroker: (id) => http("DELETE", `${BASE}/broker/${id}`),

  // Catálogos
  getCountries: () => http("GET", `${BASE}/country`),
  getDocumentTypes: () => http("GET", `${BASE}/document-type`),
};