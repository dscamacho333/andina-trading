import { styles } from '../../styles/styles';

export const Header = ({ citiesCount, countriesCount }) => {
  return (
    <div style={styles.header}>
      <h1 style={styles.title}>Gestión de Ciudades y Países</h1>
      <div style={styles.stats}>
        <span style={styles.statItem}>{citiesCount} ciudades</span>
        <span style={styles.statItem}>{countriesCount} países</span>
      </div>
    </div>
  );
};