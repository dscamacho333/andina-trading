import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../services/api';
import { useNotification } from '../hooks/useNotification';
import { useErrorHandler } from '../hooks/useErrorHandler';

import { Notification } from '../components/UI/Notification';
import { ConfirmationModal } from '../components/UI/ConfirmationModal';
import { CityForm } from '../components/Forms/CityForm';
import { CountryForm } from '../components/Forms/CountryForm';
import { CitiesTable } from '../components/Tables/CitiesTable';
import { CountriesTable } from '../components/Tables/CountriesTable';

export default function CityCountryManagement() {
  const [ciudades, setCiudades] = useState([]);
  const [paises, setPaises] = useState([]);
  const [situacionesEconomicas, setSituacionesEconomicas] = useState([]);

  const [showPaisForm, setShowPaisForm] = useState(false);
  const [showCiudadForm, setShowCiudadForm] = useState(false);
  const [filtroSituacion, setFiltroSituacion] = useState('');

  const [editandoCiudad, setEditandoCiudad] = useState(null);
  const [editandoPais, setEditandoPais] = useState(null);
  const [ciudadEditTemp, setCiudadEditTemp] = useState(null);
  const [paisEditTemp, setPaisEditTemp] = useState(null);

  const { notification, showNotification, hideNotification } = useNotification();
  const { handleError } = useErrorHandler();
  const [confirmation, setConfirmation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [loadingCombos, setLoadingCombos] = useState(true);

  const cargarCiudades = async (situation = filtroSituacion) => {
    try { setLoading(true); setCiudades(await apiService.getCities(situation)); }
    catch (e) { handleError(e); }
    finally { setLoading(false); }
  };
  const cargarPaises = async () => {
    try { setPaises(await apiService.getCountries()); }
    catch (e) { handleError(e); }
  };
  const cargarCombos = async () => {
    try { setLoadingCombos(true); const [economy] = await Promise.all([apiService.getEconomySituations()]); setSituacionesEconomicas(economy); }
    catch (e) { handleError(e); }
    finally { setLoadingCombos(false); }
  };

  const handleCreateCountry = async (countryData) => {
    try { await apiService.createCountry(countryData); setShowPaisForm(false); await cargarPaises(); showNotification('País creado', 'success'); }
    catch (e) { handleError(e); }
  };
  const handleUpdateCountry = async (id) => {
    try { await apiService.updateCountry(id, paisEditTemp); await cargarPaises(); setEditandoPais(null); setPaisEditTemp(null); showNotification('País actualizado','success'); }
    catch (e) { handleError(e); }
  };
  const handleDeleteCountry = (id, nombre) => {
    setConfirmation({
      title:'Confirmar eliminación',
      message:`¿Eliminar el país "${nombre}"?`,
      onConfirm: async () => {
        try { await apiService.deleteCountry(id); await cargarPaises(); showNotification('País eliminado','success'); }
        catch (e) { handleError(e); }
        finally { setConfirmation(null); }
      }
    });
  };

  const handleCreateCity = async (cityData) => {
    try {
      const payload = {
        name: cityData.name,
        country: { id: parseInt(cityData.countryId) },
        economySituation: { id: parseInt(cityData.economySituationId) }
      };
      await apiService.createCity(payload);
      setShowCiudadForm(false);
      await cargarCiudades();
      showNotification('Ciudad creada', 'success');
    } catch (e) { handleError(e); }
  };
  const handleUpdateCity = async (id) => {
    try {
      const payload = {
        name: ciudadEditTemp.name,
        country: { id: parseInt(ciudadEditTemp.countryId) },
        economySituation: { id: parseInt(ciudadEditTemp.economySituationId) }
      };
      await apiService.updateCity(id, payload);
      await cargarCiudades();
      setEditandoCiudad(null); setCiudadEditTemp(null);
      showNotification('Ciudad actualizada', 'success');
    } catch (e) { handleError(e); }
  };
  const handleDeleteCity = (id, nombre) => {
    setConfirmation({
      title:'Confirmar eliminación',
      message:`¿Eliminar la ciudad "${nombre}"?`,
      onConfirm: async () => {
        try { await apiService.deleteCity(id); await cargarCiudades(); showNotification('Ciudad eliminada','success'); }
        catch (e) { handleError(e); }
        finally { setConfirmation(null); }
      }
    });
  };

  const iniciarEdicionCiudad = (c) => {
    setEditandoCiudad(c.id);
    setCiudadEditTemp({
      name: c.name,
      countryId: c.country?.id,
      economySituationId: c.economySituation?.id
    });
  };
  const iniciarEdicionPais = (p) => {
    setEditandoPais(p.id);
    setPaisEditTemp({ code: p.code, name: p.name });
  };
  const handleEditTempChange = (field, value) => {
    if (editandoCiudad) setCiudadEditTemp(s => ({ ...s, [field]: value }));
    else if (editandoPais) setPaisEditTemp(s => ({ ...s, [field]: value }));
  };

  useEffect(() => { (async () => { await cargarCombos(); await cargarPaises(); await cargarCiudades(); })(); }, []);
  useEffect(() => { cargarCiudades(); }, [filtroSituacion]);

  return (
    <div className="admin-catalog">
      <div className="layout">
        <header className="header">
          <div className="brand">
            <h1>Ciudades & Países</h1>
            <small>Andina Trading</small>
          </div>
        </header>

        <main className="content">
          <div className="container">
            <div className="panel">

              <div className="section-header">
                <div className="section-title">
                  <div className="title">Catálogos</div>
                  <div className="subtitle">Administra Países y Ciudades con facilidad.</div>
                </div>
                <div className="controls">
                  <Link to="/admin/issuerManagement" className="btn btn-primary">Ir a Emisores</Link>
                </div>
              </div>

              {notification && <Notification message={notification.message} type={notification.type} onClose={hideNotification} />}
              {confirmation && (
                <ConfirmationModal
                  isOpen={true}
                  title={confirmation.title}
                  message={confirmation.message}
                  onConfirm={confirmation.onConfirm}
                  onCancel={() => setConfirmation(null)}
                />
              )}

              {/* ===== Países ===== */}
              <div className="section-header" style={{marginTop:8}}>
                <div className="section-title">
                  <div className="title">Países</div>
                  <div className="subtitle">Crea y administra países.</div>
                </div>
                <button className="btn btn-ghost btn-chips" type="button" onClick={()=>setShowPaisForm(s=>!s)}>
                  {showPaisForm ? 'Cerrar' : 'Nuevo País'}
                </button>
              </div>

              {showPaisForm && (
                <div className="card" style={{marginBottom:16}}>
                  <CountryForm onSubmit={handleCreateCountry} onCancel={()=>setShowPaisForm(false)} />
                </div>
              )}

              <div className="table-wrap" style={{marginBottom:28}}>
                <CountriesTable
                  countries={paises}
                  onEdit={iniciarEdicionPais}
                  onSaveEdit={handleUpdateCountry}
                  onCancelEdit={()=>{ setEditandoPais(null); setPaisEditTemp(null); }}
                  onDelete={handleDeleteCountry}
                  editingCountry={editandoPais}
                  editTempData={paisEditTemp}
                  onEditTempChange={handleEditTempChange}
                />
              </div>

              {/* ===== Ciudades ===== */}
              <div className="section-header">
                <div className="section-title">
                  <div className="title">Ciudades</div>
                  <div className="subtitle">Crea y administra ciudades por país y situación económica.</div>
                </div>
                <div className="controls">
                  <select
                    className="select select-compact"           /* ✅ compacto */
                    value={filtroSituacion}
                    onChange={(e)=>setFiltroSituacion(e.target.value)}
                    aria-label="Filtrar por situación económica"
                  >
                    <option value="">Situación: Todas</option>
                    {situacionesEconomicas.map(s => (
                      <option key={s.id} value={s.name}>{s.name}</option>
                    ))}
                  </select>
                  <button className="btn btn-ghost btn-chips" type="button" onClick={()=>setShowCiudadForm(s=>!s)}>
                    {showCiudadForm ? 'Cerrar' : 'Nueva Ciudad'}
                  </button>
                </div>
              </div>

              {showCiudadForm && (
                <div className="card" style={{marginBottom:16}}>
                  <CityForm
                    onSubmit={handleCreateCity}
                    onCancel={()=>setShowCiudadForm(false)}
                    countries={paises}
                    economySituations={situacionesEconomicas}
                    loadingCombos={loadingCombos}
                  />
                </div>
              )}

              <div className="table-wrap">
                <CitiesTable
                  cities={ciudades}
                  countries={paises}
                  economySituations={situacionesEconomicas}
                  loading={loading}
                  error={null}
                  filterSituation={filtroSituacion}
                  onFilterChange={setFiltroSituacion}
                  onEdit={iniciarEdicionCiudad}
                  onSaveEdit={handleUpdateCity}
                  onCancelEdit={()=>{ setEditandoCiudad(null); setCiudadEditTemp(null); }}
                  onDelete={handleDeleteCity}
                  editingCity={editandoCiudad}
                  editTempData={ciudadEditTemp}
                  onEditTempChange={handleEditTempChange}
                />
              </div>

            </div>
          </div>
        </main>
      </div>
    </div>
  );
}