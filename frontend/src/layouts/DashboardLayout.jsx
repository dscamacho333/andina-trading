// src/layouts/DashboardLayout.jsx
import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';
import Header from './Header/Header';
import Sidebar from './Sidebar/Sidebar';
import { useSidebar } from './../context/SidebarContext';
import DropdownMenu from '../components/DropdownMenu/DropdownMenu';
import styles from './DashboardLayout.module.css';

const DashboardLayout = ({ children }) => {
  const location = useLocation();
  const [sidebarVisible, setSidebarVisible] = useState(true);
  const [menuOpen, setMenuOpen] = useState(false);
  const { sidebarItems, setPreset, currentPreset } = useSidebar();


  const manualChangeRef = useRef(false);

  useEffect(() => {
    if (manualChangeRef.current) {
      manualChangeRef.current = false;
      return;
    }

    if (location.pathname.startsWith('/admin')) {
      setPreset('admin');
    } else if (location.pathname.startsWith('/backtesting')) {
      setPreset('miniDashboard');
    }
  }, [location.pathname, setPreset]);

  const toggleSidebar = () => setSidebarVisible(!sidebarVisible);
  const toggleMenu = () => setMenuOpen(prev => !prev);
  const closeMenu = () => setMenuOpen(false);

  const handleManualPreset = (presetKey) => {
    manualChangeRef.current = true;
    setPreset(presetKey);
  };

  return (
    <div className={styles.layout}>
      <Sidebar isVisible={sidebarVisible} items={sidebarItems} />

      <div
        className={`${styles.mainContent} ${!sidebarVisible ? styles.mainContentCollapsed : ''
          }`}
      >
        <Header toggleSidebar={toggleSidebar} toggleMenu={toggleMenu} />
        {currentPreset !== 'default' && (
          <div className={styles.toolbar}>
            <Link to='/portfolio'>
              <button onClick={() => handleManualPreset('default')}>Volver</button>
            </Link>
          </div>
        )}
        <div className={styles.content}>{children}</div>
        <DropdownMenu isOpen={menuOpen} onClose={closeMenu} />
      </div>
    </div>
  );
};

export default DashboardLayout;