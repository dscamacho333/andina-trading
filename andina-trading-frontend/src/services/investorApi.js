// src/services/investorApi.js
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

export const investorApi = {
  getById: (id) => http("GET", `${BASE}/investor/${id}`),
  getByStatus: (accountStatus) => http("GET", `${BASE}/investor/status/${accountStatus}`),
  deleteById: (id) => http("DELETE", `${BASE}/investor/${id}`),
};