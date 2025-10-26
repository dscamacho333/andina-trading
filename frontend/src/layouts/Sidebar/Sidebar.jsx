import { Link, useLocation } from 'react-router-dom';
import styles from './Sidebar.module.css';

const Sidebar = ({ isVisible, items }) => {
  const location = useLocation();

  return (
    <aside className={`${styles.sidebar} ${isVisible ? styles.visible : styles.hidden}`}>
      <nav>
        <ul className={styles.navList}>
          {items.map(({ label, path, icon: IconComponent }, index) => (
            <li key={index} className={styles.navItem}>
              <Link
                to={path}
                className={`${styles.navLink} ${location.pathname === path ? styles.active : ''}`}
              >
                <IconComponent className={styles.navIcon} /> {label}
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;