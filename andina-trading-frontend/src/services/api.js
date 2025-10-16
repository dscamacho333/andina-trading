const API_BASE = '';

export const API_URLS = {
  CITIES: `${API_BASE}/api/v1/city`,
  COUNTRIES: `${API_BASE}/api/v1/country`,
  ECONOMY_SITUATIONS: `${API_BASE}/api/v1/economy-situation`,
};

export class ApiError extends Error {
  constructor(status, data, rawText) {
    const message =
      (data && data.errors && typeof data.errors === 'object' && Object.values(data.errors)[0]) ||
      (data && (data.message || data.error)) ||
      rawText ||
      `HTTP ${status}`;
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.data = data || null;
    this.rawText = rawText || null;
  }
}

class ApiService {
  async handleResponse(response) {
    if (!response.ok) {
      const errorText = await response.text();
      console.log('🔍 Raw error text:', errorText);
      let parsed = null;
      try { parsed = errorText ? JSON.parse(errorText) : null; } catch {}
      throw new ApiError(response.status, parsed, errorText);
    }

    if (response.status === 204) return null;

    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      return await response.json();
    }
    return await response.text();
  }

  async request(url, options = {}) {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...(options.headers || {}),
      },
      ...options,
    });
    return this.handleResponse(response);
  }

  async getCities(situation = '') {
    const url = situation ? `${API_URLS.CITIES}/situation/${situation}` : API_URLS.CITIES;
    const data = await this.request(url);
    return Array.isArray(data) ? data : (data?.content || []);
  }

  async createCity(cityData) {
    return this.request(API_URLS.CITIES, { method: 'POST', body: JSON.stringify(cityData) });
  }

  async updateCity(id, cityData) {
    return this.request(`${API_URLS.CITIES}/${id}`, { method: 'PUT', body: JSON.stringify(cityData) });
  }

  async deleteCity(id) {
    return this.request(`${API_URLS.CITIES}/${id}`, { method: 'DELETE' });
  }

  async getCountries() {
    const data = await this.request(API_URLS.COUNTRIES);
    return Array.isArray(data) ? data : (data?.content || []);
  }

  async createCountry(countryData) {
    return this.request(API_URLS.COUNTRIES, { method: 'POST', body: JSON.stringify(countryData) });
  }

  async updateCountry(id, countryData) {
    return this.request(`${API_URLS.COUNTRIES}/${id}`, { method: 'PUT', body: JSON.stringify(countryData) });
  }

  async deleteCountry(id) {
    return this.request(`${API_URLS.COUNTRIES}/${id}`, { method: 'DELETE' });
  }

  async getEconomySituations() {
    const data = await this.request(API_URLS.ECONOMY_SITUATIONS);
    return Array.isArray(data) ? data : (data?.content || []);
  }
}

export const apiService = new ApiService();