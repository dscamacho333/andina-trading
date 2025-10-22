// src/services/issuerApi.js
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
  // DELETE devuelve 204 sin cuerpo
  if (res.status === 204) return null;
  return res.json();
}

export const issuerApi = {
  // Emisores
  getIssuers: () => http("GET", `${BASE}/issuer`),
  getIssuersByCountry: (countryId) =>
    http("GET", `${BASE}/issuer/country/${countryId}`),
  getIssuersByIndustry: (industryId) =>
    http("GET", `${BASE}/issuer/industry/${industryId}`),
  getIssuersBySector: (sectorId) =>
    http("GET", `${BASE}/issuer/sector/${sectorId}`),

  createIssuer: (payload) => http("POST", `${BASE}/issuer`, payload),
  updateIssuer: (id, payload) => http("PUT", `${BASE}/issuer/${id}`, payload),
  deleteIssuer: (id) => http("DELETE", `${BASE}/issuer/${id}`),

  // Catálogos
  getCountries: () => http("GET", `${BASE}/country`),
  getIndustries: () => http("GET", `${BASE}/industry`),
};