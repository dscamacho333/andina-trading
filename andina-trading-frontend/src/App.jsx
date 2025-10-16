// App.jsx
import { useEffect, useState } from 'react';
import { apiService } from './services/api';
import { useNotification } from './hooks/useNotification';
import { useErrorHandler } from './hooks/useErrorHandler';

// Components
import { Notification } from './components/UI/Notification';
import { ConfirmationModal } from './components/UI/ConfirmationModal';
import { Header } from './components/Layout/Header';
import { ActionButtons } from './components/Layout/ActionButtons';
import { CityForm } from './components/Forms/CityForm';
import { CountryForm } from './components/Forms/CountryForm';
import { CitiesTable } from './components/Tables/CitiesTable';
import { CountriesTable } from './components/Tables/CountriesTable';

// Styles
import { styles } from './styles/styles';

export default function App() {
  // State
  const [ciudades, setCiudades] = useState([]);
  const [paises, setPaises] = useState([]);
  const [situacionesEconomicas, setSituacionesEconomicas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [cargandoCombos, setCargandoCombos] = useState(true);
  
  // Form states
  const [mostrarFormCiudad, setMostrarFormCiudad] = useState(false);
  const [mostrarFormPais, setMostrarFormPais] = useState(false);
  
  // Edit states
  const [editandoCiudad, setEditandoCiudad] = useState(null);
  const [editandoPais, setEditandoPais] = useState(null);
  const [ciudadEditTemp, setCiudadEditTemp] = useState(null);
  const [paisEditTemp, setPaisEditTemp] = useState(null);
  
  // Filter state
  const [filtroSituacion, setFiltroSituacion] = useState('');

  // Custom hooks
  const { notification, showNotification, hideNotification } = useNotification();
  const { handleError } = useErrorHandler();
  const [confirmation, setConfirmation] = useState(null);

  // Data loading
  const cargarCiudades = async (situation = filtroSituacion) => {
    try {
      setCargando(true);
      const data = await apiService.getCities(situation);
      setCiudades(data);
    } catch (error) {
      handleError(error);
    } finally {
      setCargando(false);
    }
  };

  const cargarPaises = async () => {
    try {
      const data = await apiService.getCountries();
      setPaises(data);
    } catch (error) {
      handleError(error);
    }
  };

  const cargarCombos = async () => {
    try {
      setCargandoCombos(true);
      const [economySituations] = await Promise.all([
        apiService.getEconomySituations()
      ]);
      setSituacionesEconomicas(economySituations);
    } catch (error) {
      handleError(error);
    } finally {
      setCargandoCombos(false);
    }
  };

  // City operations
  const handleCreateCity = async (cityData) => {
    try {
      const ciudadData = {
        name: cityData.name,
        country: { id: parseInt(cityData.countryId) },
        economySituation: { id: parseInt(cityData.economySituationId) }
      };

      await apiService.createCity(ciudadData);
      setMostrarFormCiudad(false);
      await cargarCiudades();
      showNotification('Ciudad creada exitosamente', 'success');
    } catch (error) {
      handleError(error);
      // ❌ NO relanzar — evita Uncaught (in promise)
    }
  };

  const handleUpdateCity = async (id) => {
    try {
      const ciudadData = {
        name: ciudadEditTemp.name,
        country: { id: parseInt(ciudadEditTemp.countryId) },
        economySituation: { id: parseInt(ciudadEditTemp.economySituationId) }
      };

      await apiService.updateCity(id, ciudadData);
      await cargarCiudades();
      setEditandoCiudad(null);
      setCiudadEditTemp(null);
      showNotification('Ciudad actualizada exitosamente', 'success');
    } catch (error) {
      handleError(error);
      // ❌ NO relanzar
    }
  };

  const handleDeleteCity = async (id, nombre) => {
    setConfirmation({
      title: 'Confirmar eliminación',
      message: `¿Estás seguro de que quieres eliminar la ciudad "${nombre}"? Esta acción no se puede deshacer.`,
      onConfirm: async () => {
        try {
          await apiService.deleteCity(id);
          await cargarCiudades();
          showNotification('Ciudad eliminada exitosamente', 'success');
        } catch (error) {
          handleError(error);
        } finally {
          setConfirmation(null);
        }
      }
    });
  };

  // Country operations
  const handleCreateCountry = async (countryData) => {
    try {
      await apiService.createCountry(countryData);
      setMostrarFormPais(false);
      await cargarPaises();
      showNotification('País creado exitosamente', 'success');
    } catch (error) {
      handleError(error);
      // ❌ NO relanzar
    }
  };

  const handleUpdateCountry = async (id) => {
    try {
      await apiService.updateCountry(id, paisEditTemp);
      await cargarPaises();
      setEditandoPais(null);
      setPaisEditTemp(null);
      showNotification('País actualizado exitosamente', 'success');
    } catch (error) {
      handleError(error);
      // ❌ NO relanzar
    }
  };

  const handleDeleteCountry = async (id, nombre) => {
    setConfirmation({
      title: 'Confirmar eliminación',
      message: `¿Estás seguro de que quieres eliminar el país "${nombre}"? Esta acción no se puede deshacer.`,
      onConfirm: async () => {
        try {
          await apiService.deleteCountry(id);
          await cargarPaises();
          showNotification('País eliminado exitosamente', 'success');
        } catch (error) {
          handleError(error);
        } finally {
          setConfirmation(null);
        }
      }
    });
  };

  // Edit handlers
  const iniciarEdicionCiudad = (ciudad) => {
    setEditandoCiudad(ciudad.id);
    setCiudadEditTemp({
      name: ciudad.name,
      countryId: ciudad.country?.id,
      economySituationId: ciudad.economySituation?.id
    });
  };

  const iniciarEdicionPais = (pais) => {
    setEditandoPais(pais.id);
    setPaisEditTemp({
      code: pais.code,
      name: pais.name
    });
  };

  const handleEditTempChange = (field, value) => {
    if (editandoCiudad) {
      setCiudadEditTemp(prev => ({ ...prev, [field]: value }));
    } else if (editandoPais) {
      setPaisEditTemp(prev => ({ ...prev, [field]: value }));
    }
  };

  // Initial load
  useEffect(() => { 
    const cargarTodo = async () => {
      await cargarCombos();
      await cargarPaises();
      await cargarCiudades();
    };
    cargarTodo();
  }, []);

  // Filter effect
  useEffect(() => {
    if (cargandoCombos === false) {
      cargarCiudades();
    }
  }, [filtroSituacion]);

  return (
    <div style={styles.container}>
      {/* Notificación */}
      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={hideNotification}
        />
      )}

      {/* Modal de confirmación */}
      {confirmation && (
        <ConfirmationModal
          isOpen={true}
          title={confirmation.title}
          message={confirmation.message}
          onConfirm={confirmation.onConfirm}
          onCancel={() => setConfirmation(null)}
        />
      )}

      {/* Header */}
      <Header citiesCount={ciudades.length} countriesCount={paises.length} />

      {/* Action Buttons */}
      <ActionButtons
        showCityForm={mostrarFormCiudad}
        showCountryForm={mostrarFormPais}
        onToggleCityForm={() => setMostrarFormCiudad(!mostrarFormCiudad)}
        onToggleCountryForm={() => setMostrarFormPais(!mostrarFormPais)}
        onRefresh={() => {
          cargarCiudades();
          cargarPaises();
        }}
      />

      {/* City Form */}
      {mostrarFormCiudad && (
        <CityForm
          onSubmit={handleCreateCity}
          onCancel={() => setMostrarFormCiudad(false)}
          countries={paises}
          economySituations={situacionesEconomicas}
          loadingCombos={cargandoCombos}
        />
      )}

      {/* Country Form */}
      {mostrarFormPais && (
        <CountryForm
          onSubmit={handleCreateCountry}
          onCancel={() => setMostrarFormPais(false)}
        />
      )}

      {/* Countries Table */}
      <CountriesTable
        countries={paises}
        onEdit={iniciarEdicionPais}
        onSaveEdit={handleUpdateCountry}
        onCancelEdit={() => {
          setEditandoPais(null);
          setPaisEditTemp(null);
        }}
        onDelete={handleDeleteCountry}
        editingCountry={editandoPais}
        editTempData={paisEditTemp}
        onEditTempChange={handleEditTempChange}
      />

      {/* Cities Table */}
      <CitiesTable
        cities={ciudades}
        countries={paises}
        economySituations={situacionesEconomicas}
        loading={cargando}
        error={null}
        filterSituation={filtroSituacion}
        onFilterChange={setFiltroSituacion}
        onEdit={iniciarEdicionCiudad}
        onSaveEdit={handleUpdateCity}
        onCancelEdit={() => {
          setEditandoCiudad(null);
          setCiudadEditTemp(null);
        }}
        onDelete={handleDeleteCity}
        editingCity={editandoCiudad}
        editTempData={ciudadEditTemp}
        onEditTempChange={handleEditTempChange}
      />
    </div>
  );
}