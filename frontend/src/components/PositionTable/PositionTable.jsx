import React, { useEffect, useState } from "react";
import { getPortfolio } from "../../services/portfolioService";
import styles from "./PositionTable.module.css";
import { syncAccount } from "../../services/userService";
import { getUserFromToken } from "../../services/authService";
import { PositionModal } from "../PositionModal/PositionModal";

export const PositionTable = () => {
  const [portfolio, setPortfolio] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedPosition, setSelectedPosition] = useState(null);

  useEffect(() => {
    let isMounted = true;

    const loadPortfolio = async () => {
      try {
        const userInfo = getUserFromToken();

        if (userInfo?.userId && userInfo?.alpacaAccountId) {
          await syncAccount(userInfo.alpacaAccountId);
          const data = await getPortfolio(userInfo.alpacaAccountId);
          if (isMounted) setPortfolio(data.positions);
        }
      } catch (err) {
        console.error("Error en sincronización o carga de portafolio:", err);
      } finally {
        if (isMounted) setLoading(false);
      }
    };

    loadPortfolio();

    return () => {
      isMounted = false;
    };
  }, []);

  const handlePositionClick = (position) => setSelectedPosition(position);

  return (
    <div className={styles.box}>
      <h3>Posiciones</h3>

      {loading ? (
        <p>Cargando...</p>
      ) : (
        <div className={styles.tableWrapper}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Acción</th>
                <th>Precio</th>
                <th>Cantidad</th>
                <th>Lado</th>
                <th>Valor mercado</th>
              </tr>
            </thead>
            <tbody>
              {portfolio.length > 0 ? (
                portfolio.map((item, index) => (
                  <tr
                    key={index}
                    className={styles.tableRow}
                    onClick={() => handlePositionClick(item)}
                  >
                    <td>{item.symbol}</td>
                    <td>${item.current_price}</td>
                    <td>{item.qty}</td>
                    <td>{item.side}</td>
                    <td>{item.market_value}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5">No hay datos en el portafolio.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {selectedPosition && (
        <PositionModal
          position={selectedPosition}
          isOpen={true}
          onClose={() => setSelectedPosition(null)}
        />
      )}
    </div>
  );
};
