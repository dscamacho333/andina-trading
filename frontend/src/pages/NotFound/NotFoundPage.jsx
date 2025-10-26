import React from 'react';
import LandingLayout from '../../layouts/LandingLayout';
import styles from '../NotFound/NotFoundPage.module.css';
import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <LandingLayout>
      <div className={styles.wrapper}>
        <div className={styles.status}>404</div>
        <div className={styles.subtitle}>Esta página no existe</div>
        <button
          className={styles.btn}
          style={{ textDecoration: "none" }}
          onClick={() => navigate(-1)}  // Esto hace que vaya a la página anterior
        >
          Volver
        </button>
      </div>
    </LandingLayout>
  );
};

export default NotFoundPage;