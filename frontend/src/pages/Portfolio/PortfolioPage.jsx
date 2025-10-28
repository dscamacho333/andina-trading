import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import OrderGallery from "../../components/OrderGallery/OrderGallery";
import styles from "./PortfolioPage.module.css";
import { Loading } from "../../components/Loading/Loading";
import { PositionTable } from "../../components/PositionTable/PositionTable";

const PortfolioPage = () => {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 2000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <DashboardLayout>
      {isLoading ? (
        <Loading message="Cargando portafolio..." />
      ) : (
        <div className={styles.container}>
          <span className={styles.span}>Última actualización: Hace 1 min</span>
          <h2 className={styles.title}>Mi portafolio</h2>

          <PositionTable />

          <div className={styles.box}>
            <h3>Otros</h3>
          </div>

          <div className={`${styles.box} ${styles.ordersWrapper}`}>
            <OrderGallery />
          </div>
        </div>
      )}
    </DashboardLayout>
  );
};

export default PortfolioPage;
