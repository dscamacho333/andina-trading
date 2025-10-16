import { useState } from 'react';
import { Button } from '../UI/Button';
import { styles } from '../../styles/styles';

export const CountryForm = ({ 
  onSubmit, 
  onCancel,
  initialData = { code: '', name: '' }
}) => {
  const [formData, setFormData] = useState(initialData);
  const [submitting, setSubmitting] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'code' ? value.toUpperCase() : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.code || !formData.name) {
      return false;
    }

    if (formData.code.length !== 2) {
      return false;
    }

    setSubmitting(true);
    try {
      await onSubmit(formData);
      setFormData({ code: '', name: '' });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={styles.formContainer}>
      <h2 style={styles.formTitle}>
        {initialData.id ? 'Editar País' : 'Crear Nuevo País'}
      </h2>
      
      <form onSubmit={handleSubmit} style={styles.form}>
        <div style={styles.formGroup}>
          <label style={styles.label}>Código del País (2 letras):</label>
          <input
            type="text"
            name="code"
            value={formData.code}
            onChange={handleInputChange}
            maxLength={2}
            style={styles.input}
            placeholder="Ej: AR"
            required
          />
          <div style={styles.helperText}>Código ISO de 2 letras (se convierte a mayúsculas)</div>
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Nombre del País:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            style={styles.input}
            placeholder="Ej: Argentina"
            required
          />
          <div style={styles.helperText}>Nombre completo del país</div>
        </div>

        <div style={{ display: 'flex', gap: '12px', marginTop: '16px' }}>
          <Button 
            type="submit" 
            disabled={submitting}
            variant="info"
          >
            {submitting ? 'Guardando...' : (initialData.id ? 'Actualizar País' : 'Crear País')}
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