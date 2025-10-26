import { useNavigate } from "react-router-dom";
import styles from "./ValidateRelateBankAccount.module.css";
import Swal from "sweetalert2";

export const ValidateRelateBankAccount = ({ bankRelationDetail }) => {
  const navigate = useNavigate();

  Swal.fire({
    title: "Validando relación de cuenta",
    html: `
    ${bankRelationDetail}<br>
    Por favor, intenta nuevamente más tarde.
    `,
    icon: "info",
    confirmButtonText: "Volver al inicio",
    confirmButtonColor: "#3085d6",
  }).then((result) => {
    if (result.isConfirmed) {
      navigate("/portfolio");
    }
  });

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Validando Relación De Cuenta Bancaria</h2>
        <p>La cuenta se encuentra en revisión, aún no está activa...</p>
      </div>
    </div>
  );
};
