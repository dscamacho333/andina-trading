// src/services/adminLogsApi.js
const BASE = "/api/v1/admin/logs";

async function http(method, url, params = null) {
  const full = params ? `${url}?${new URLSearchParams(params).toString()}` : url;
  const res = await fetch(full, {
    method,
    cache: "no-store",           // ⬅️ evita resultados cacheados
    headers: { "Accept": "application/json" },
  });
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || `HTTP ${res.status}`);
  }
  return res.json();
}

export const adminLogsApi = {
  listFiles: ({ start, end, type } = {}) =>
    http("GET", `${BASE}/files`, {
      ...(start ? { start } : {}),
      ...(end ? { end } : {}),
      ...(type ? { type } : {}),
    }),
  downloadUrl: (id) => `${BASE}/files/${id}`,
};