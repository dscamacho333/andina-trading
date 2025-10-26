import React from 'react';
import styles from './LandingLayout.module.css';

const LandingLayout = ({ children }) => {
  return (
    <div className={styles.layout}>
      <header className={styles.header}>
        <div className={styles.logo}>
          {/* Logo simple, puedes poner el logo SVG o imagen */}
          <h1>Forest Trade</h1>
          <small>Trading</small>
        </div>
      </header>
      <main className={styles.content}>
        {children}
      </main>
      <footer className={styles.footer}>
        {/* Puedes agregar pie de página simple si quieres */}
        <small>© 2025 Forest Trade</small>
      </footer>
    </div>
  );
};

export default LandingLayout;