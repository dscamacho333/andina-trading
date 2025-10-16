import { useState } from 'react';
import { Table, Th, Td } from '../UI/Table';
import { Button } from '../UI/Button';
import { styles } from '../../styles/styles';

export const CitiesTable = ({ 
  cities, 
  countries, 
  economySituations, 
  loading, 
  error, 
  filterSituation,
  onFilterChange,
  onEdit,
  onSaveEdit,
  onCancelEdit,
  onDelete,
  editingCity,
  editTempData,
  onEditTempChange
}) => {
  const [localFilter, setLocalFilter] = useState(filterSituation);

  const handleFilterChange = (e) => {
    const value = e.target.value;
    setLocalFilter(value);
    onFilterChange(value);
  };

  if (loading) return <div style={styles.loading}>Cargando ciudades...</div>;
  if (error) return <div style={styles.error}>Error: {error}</div>;

  return (
    <div style={styles.tableSection}>
      <div style={styles.sectionHeader}>
        <h2 style={styles.sectionTitle}>Lista de Ciudades</h2>
        <div style={styles.filterSection}>
          <select
            value={localFilter}
            onChange={handleFilterChange}
            style={styles.filterSelect}
          >
            <option value="">Todas las situaciones</option>
            {economySituations.map(situation => (
              <option key={situation.id} value={situation.name}>
                {situation.name}
              </option>
            ))}
          </select>
          <span style={styles.countBadge}>{cities.length} ciudades</span>
        </div>
      </div>

      <div style={styles.tableContainer}>
        <Table>
          <thead>
            <tr>
              <Th>ID</Th>
              <Th>Nombre</Th>
              <Th>País</Th>
              <Th>Código</Th>
              <Th>Situación Económica</Th>
              <Th>Descripción</Th>
              <Th>Acciones</Th>
            </tr>
          </thead>
          <tbody>
            {cities.map((city) => (
              <tr key={city.id}>
                {editingCity === city.id ? (
                  <>
                    <Td>{city.id}</Td>
                    <Td>
                      <input
                        type="text"
                        name="name"
                        value={editTempData?.name || ''}
                        onChange={(e) => onEditTempChange('name', e.target.value)}
                        style={styles.editInput}
                      />
                    </Td>
                    <Td>
                      <select
                        name="countryId"
                        value={editTempData?.countryId || ''}
                        onChange={(e) => onEditTempChange('countryId', e.target.value)}
                        style={styles.editSelect}
                      >
                        {countries.map(country => (
                          <option key={country.id} value={country.id}>
                            {country.name}
                          </option>
                        ))}
                      </select>
                    </Td>
                    <Td>{city.country?.code}</Td>
                    <Td>
                      <select
                        name="economySituationId"
                        value={editTempData?.economySituationId || ''}
                        onChange={(e) => onEditTempChange('economySituationId', e.target.value)}
                        style={styles.editSelect}
                      >
                        {economySituations.map(situation => (
                          <option key={situation.id} value={situation.id}>
                            {situation.name}
                          </option>
                        ))}
                      </select>
                    </Td>
                    <Td>{city.economySituation?.description}</Td>
                    <Td>
                      <Button
                        onClick={() => onSaveEdit(city.id)}
                        variant="primary"
                        style={{ marginRight: '8px', fontSize: '12px', padding: '8px 16px' }}
                      >
                        Guardar
                      </Button>
                      <Button
                        onClick={onCancelEdit}
                        variant="cancel"
                        style={{ fontSize: '12px', padding: '8px 16px' }}
                      >
                        Cancelar
                      </Button>
                    </Td>
                  </>
                ) : (
                  <>
                    <Td>{city.id}</Td>
                    <Td>{city.name}</Td>
                    <Td>{city.country?.name}</Td>
                    <Td>{city.country?.code}</Td>
                    <Td>{city.economySituation?.name}</Td>
                    <Td>{city.economySituation?.description}</Td>
                    <Td>
                      <Button
                        onClick={() => onEdit(city)}
                        variant="warning"
                        style={{ marginRight: '8px', fontSize: '12px', padding: '8px 16px' }}
                      >
                        Editar
                      </Button>
                      <Button
                        onClick={() => onDelete(city.id, city.name)}
                        variant="danger"
                        style={{ fontSize: '12px', padding: '8px 16px' }}
                      >
                        Eliminar
                      </Button>
                    </Td>
                  </>
                )}
              </tr>
            ))}
            {cities.length === 0 && (
              <tr>
                <Td colSpan={7} style={styles.noData}>
                  {localFilter ? 'No hay ciudades con esta situación económica' : 'No hay ciudades registradas'}
                </Td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>
    </div>
  );
};