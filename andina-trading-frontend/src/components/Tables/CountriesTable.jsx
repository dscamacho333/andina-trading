import { Table, Th, Td } from '../UI/Table';
import { Button } from '../UI/Button';
import { styles } from '../../styles/styles';

export const CountriesTable = ({ 
  countries, 
  onEdit,
  onSaveEdit,
  onCancelEdit,
  onDelete,
  editingCountry,
  editTempData,
  onEditTempChange
}) => {
  return (
    <div style={styles.tableSection}>
      <div style={styles.sectionHeader}>
        <h2 style={styles.sectionTitle}>Lista de Países</h2>
        <span style={styles.countBadge}>{countries.length} países</span>
      </div>
      
      <div style={styles.tableContainer}>
        <Table>
          <thead>
            <tr>
              <Th>ID</Th>
              <Th>Código</Th>
              <Th>Nombre</Th>
              <Th>Acciones</Th>
            </tr>
          </thead>
          <tbody>
            {countries.map((country) => (
              <tr key={country.id}>
                {editingCountry === country.id ? (
                  <>
                    <Td>{country.id}</Td>
                    <Td>
                      <input
                        type="text"
                        name="code"
                        value={editTempData?.code || ''}
                        onChange={(e) => onEditTempChange('code', e.target.value.toUpperCase())}
                        style={styles.editInput}
                        maxLength={2}
                      />
                    </Td>
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
                      <Button
                        onClick={() => onSaveEdit(country.id)}
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
                    <Td>{country.id}</Td>
                    <Td>{country.code}</Td>
                    <Td>{country.name}</Td>
                    <Td>
                      <Button
                        onClick={() => onEdit(country)}
                        variant="warning"
                        style={{ marginRight: '8px', fontSize: '12px', padding: '8px 16px' }}
                      >
                        Editar
                      </Button>
                      <Button
                        onClick={() => onDelete(country.id, country.name)}
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
            {countries.length === 0 && (
              <tr>
                <Td colSpan={4} style={styles.noData}>No hay países registrados</Td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>
    </div>
  );
};