import React, { useRef, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styles from './DropdownMenu.module.css';
import { useSidebar } from '../../context/SidebarContext';

const DropdownMenu = ({ isOpen, onClose }) => {
  const menuRef = useRef(null);
  const { setPreset } = useSidebar();

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        onClose();
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [onClose]);

  if (!isOpen) return null;

  return (

    <div className={styles.menuContainer} >
      <div className={styles.card} ref={menuRef}>
        <ul className={styles.list}>
          <Link
                to='/userpreferences'>
          <li className={styles.element} onClick={() => setPreset('profile')}>
            {/* SVG */}
            <p className={styles.label}>Configuración de perfil</p>
          </li>
            </Link>
        </ul>
        <div className={styles.separator}></div>
        <ul className={styles.list}>
          <li className={styles.element}>
            {/* SVG */}
            <p className={styles.label}>Settings</p>
          </li>
          <li className={`${styles.element} ${styles.delete}`}>
            {/* SVG */}
            <p className={styles.label}>Delete</p>
          </li>
        </ul>
        <div className={styles.separator}></div>
        <ul className={styles.list}>
          <Link
            to="/login" className={styles.navLink }
          >
            <li className={`${styles.element} ${styles.delete}`}>
              {/* SVG */}
              <p className={styles.label}>Cerrar Sesión</p>
            </li>
          </Link>
        </ul>
      </div>
    </div>
  );
};

export default DropdownMenu;