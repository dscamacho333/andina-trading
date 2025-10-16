import { useState } from 'react';
import { Button } from '../UI/Button';
import { styles } from '../../styles/styles';

export const CityForm = ({ 
  onSubmit, 
  onCancel, 
  countries, 
  economySituations, 
  loadingCombos,
  initialData = { name: '', countryId: '', economySituationId: '' }
}) => {
  const [formData, setFormData] = useState(initialData);
  const [submitting, setSubmitting] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.name || !formData.countryId || !formData.economySituationId) {
      return false;
    }

    setSubmitting(true);
    try {
      await onSubmit(formData);
      setFormData({ name: '', countryId: '', economySituationId: '' });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={styles.formContainer}>
      <h2 style={styles.formTitle}>
        {initialData.id ? 'Editar Ciudad' : 'Crear Nueva Ciudad'}
      </h2>
      
      <form onSubmit={handleSubmit} style={styles.form}>
        <div style={styles.formGroup}>
          <label style={styles.label}>Nombre de la ciudad:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            style={styles.input}
            placeholder="Ej: Bogotá"
            required
          />
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>País:</label>
          <select
            name="countryId"
            value={formData.countryId}
            onChange={handleInputChange}
            style={styles.select}
            disabled={loadingCombos}
            required
          >
            <option value="">Seleccionar país</option>
            {countries.map(country => (
              <option key={country.id} value={country.id}>
                {country.name} ({country.code})
              </option>
            ))}
          </select>
          {loadingCombos && <div style={styles.loadingText}>Cargando países...</div>}
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Situación Económica:</label>
          <select
            name="economySituationId"
            value={formData.economySituationId}
            onChange={handleInputChange}
            style={styles.select}
            disabled={loadingCombos}
            required
          >
            <option value="">Seleccionar situación económica</option>
            {economySituations.map(situation => (
              <option key={situation.id} value={situation.id}>
                {situation.name}
              </option>
            ))}
          </select>
          {loadingCombos && <div style={styles.loadingText}>Cargando situaciones...</div>}
        </div>

        <div style={{ display: 'flex', gap: '12px', marginTop: '16px' }}>
          <Button 
            type="submit" 
            disabled={submitting || loadingCombos}
            variant="primary"
          >
            {submitting ? 'Guardando...' : (initialData.id ? 'Actualizar Ciudad' : 'Crear Ciudad')}
          </Button>
          <Button 
            type="button" 
            onClick={onCancel}
            variant="cancel"
          >
            Cancelar
          </Button>
        </div>
      </form>
    </div>
  );
};