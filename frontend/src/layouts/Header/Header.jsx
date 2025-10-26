import React, { useEffect, useState } from "react";
import styles from "./Header.module.css";
import WalletLogo from "../../assets/icons/WalletLogo";
import { Link } from "react-router-dom";
import { useBalance } from "../../context/BalanceContext";
import { useUser } from "../../context/UserContext";
import { Navigate } from "react-router-dom";

const Header = ({ toggleSidebar, toggleMenu }) => {
  const { user, loading } = useUser();
  const { balance } = useBalance();

  if (loading) return null;

  if (user === null) {
    return <Navigate to="/" replace />;
  }

  return (
    <header className={styles.headerContainer}>
      <div className={styles.userInfoContainer}>
        <div className={styles.brand}>
          <div className={styles.iconWrapper}>
            <div className={styles.circleIcon}></div>
          </div>
          <div className={styles.textWrapper}>
            <p className={styles.userLabel}>{user.name}</p>
            <p className={styles.balanceLabel}>
              Saldo:{" "}
              <span className={styles.balanceValue}>
                {balance !== null
                  ? `USD $${Number(balance).toFixed(2)}`
                  : "Cargando..."}
              </span>
            </p>
          </div>
        </div>
        <Link
          className={styles.button}
          style={{ textDecoration: "none" }}
          to="/recharge-account"
        >
          Depositar <WalletLogo className={styles.iconButton}></WalletLogo>{" "}
        </Link>
      </div>
      <div className={styles.actions}>
        <button className={styles.icon} onClick={toggleSidebar}>
          ☰
        </button>

        <a className={styles.link} onClick={toggleMenu}>
          Configuración{" "}
        </a>
      </div>
    </header>
  );
};

export default Header;
