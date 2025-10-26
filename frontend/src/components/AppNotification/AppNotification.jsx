import React from "react";
import styles from "./AppNotification.module.css";
import PropTypes from "prop-types";

const AppNotification = ({ message, type = "danger", onClose }) => {
  if (!message) return null;

  return (
    <div className={`${styles.notification} ${styles[type]}`} role="alert">
      <span>{message}</span>
      <button
        type="button"
        className={styles.closeButton}
        aria-label="Close"
        onClick={onClose}
      >
        ×
      </button>
    </div>
  );
};

AppNotification.propTypes = {
  message: PropTypes.string,
  type: PropTypes.oneOf(["success", "danger", "info", "warning"]),
  onClose: PropTypes.func.isRequired,
};

export default AppNotification;