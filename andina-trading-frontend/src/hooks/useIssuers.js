import { useEffect, useMemo, useState } from "react";
import { issuerApi } from "../services/issuerApi";

export function useIssuers() {
  const [issuers, setIssuers] = useState([]);
  const [industries, setIndustries] = useState([]);
  const [countries, setCountries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingCombos, setLoadingCombos] = useState(true);
  const [error, setError] = useState(null);

  // filtros opcionales
  const [filters, setFilters] = useState({ countryId: "", industryId: "", sectorId: "" });

  const loadCombos = async () => {
    try {
      setLoadingCombos(true);
      const [inds, ctries] = await Promise.all([
        issuerApi.getIndustries(),
        issuerApi.getCountries(),
      ]);
      setIndustries(inds || []);
      setCountries(ctries || []);
    } catch (e) {
      setError(e.message || "Error cargando combos");
    } finally {
      setLoadingCombos(false);
    }
  };

  const loadIssuers = async () => {
    try {
      setLoading(true);
      let data = [];
      if (filters.countryId) {
        data = await issuerApi.getIssuersByCountry(filters.countryId);
      } else if (filters.industryId) {
        data = await issuerApi.getIssuersByIndustry(filters.industryId);
      } else if (filters.sectorId) {
        data = await issuerApi.getIssuersBySector(filters.sectorId);
      } else {
        data = await issuerApi.getIssuers();
      }
      setIssuers(data || []);
    } catch (e) {
      setError(e.message || "Error cargando emisores");
    } finally {
      setLoading(false);
    }
  };

  const createIssuer = async (form) => {
    // Adaptar al contrato del backend
    const payload = {
      name: form.name.trim(),
      ticker: form.ticker.trim(),
      country: { id: parseInt(form.countryId, 10) },
      industry: { id: parseInt(form.industryId, 10) },
      website: form.website.trim(),
      notes: form.notes.trim(),
    };
    const created = await issuerApi.createIssuer(payload);
    await loadIssuers();
    return created;
  };

  const updateIssuer = async (id, form) => {
    const payload = {
      name: form.name.trim(),
      ticker: form.ticker.trim(),
      country: { id: parseInt(form.countryId, 10) },
      industry: { id: parseInt(form.industryId, 10) },
      website: form.website.trim(),
      notes: form.notes.trim(),
    };
    const updated = await issuerApi.updateIssuer(id, payload);
    await loadIssuers();
    return updated;
  };

  const deleteIssuer = async (id) => {
    await issuerApi.deleteIssuer(id);
    await loadIssuers();
  };

  useEffect(() => { loadCombos(); }, []);
  useEffect(() => { loadIssuers(); }, [filters]);

  const sectorOptions = useMemo(() => {
    // Derivar sectores a partir de industries (si tu backend no da endpoint directo)
    // Si IndustryResponseDTO trae sector: {id, name}, podemos deduplicar:
    const map = new Map();
    industries.forEach((i) => {
      if (i?.sector?.id && !map.has(i.sector.id)) {
        map.set(i.sector.id, i.sector);
      }
    });
    return Array.from(map.values());
  }, [industries]);

  return {
    issuers, industries, countries, sectorOptions,
    loading, loadingCombos, error,
    filters, setFilters,
    loadIssuers, createIssuer, updateIssuer, deleteIssuer,
  };
}
