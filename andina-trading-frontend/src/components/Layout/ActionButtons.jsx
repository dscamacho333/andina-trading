import { Button } from '../UI/Button';
import { styles } from '../../styles/styles';

export const ActionButtons = ({ 
  showCityForm, 
  showCountryForm, 
  onToggleCityForm, 
  onToggleCountryForm, 
  onRefresh 
}) => {
  return (
    <div style={styles.actionButtons}>
      <Button 
        onClick={onToggleCityForm}
        variant={showCityForm ? 'cancel' : 'primary'}
      >
        {showCityForm ? 'Cancelar' : 'Crear Nueva Ciudad'}
      </Button>

      <Button 
        onClick={onToggleCountryForm}
        variant={showCountryForm ? 'cancel' : 'info'}
      >
        {showCountryForm ? 'Cancelar' : 'Crear Nuevo País'}
      </Button>

      <Button 
        onClick={onRefresh}
        variant="secondary"
      >
        Refrescar Todo
      </Button>
    </div>
  );
};