import styles from "./RelateBankAccount.module.css";
import { useState } from "react";
import Swal from "sweetalert2";
import { relateBankAccount } from "../../services/rechargeAccountService";
import { useNavigate } from "react-router-dom";

export const RelateBankAccount = ({ user }) => {
  const [accountType, setAccountType] = useState("CHECKING");
  const [accountNumber, setAccountNumber] = useState("");
  const [routingNumber, setRoutingNumber] = useState("");
  const [isSending, setIsSending] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSending(true);

    try {
      if (!accountNumber || !routingNumber) {
        Swal.fire({
          icon: "warning",
          title: "Campos incompletos",
          text: "Por favor, complete todos los campos.",
        });

        return;
      }

      if (accountNumber.length < 8 || accountNumber.length > 12) {
        Swal.fire({
          icon: "error",
          title: "Número de cuenta inválido",
          text: "Debe tener entre 8 y 12 dígitos.",
        });

        return;
      }

      if (routingNumber.length !== 9) {
        Swal.fire({
          icon: "error",
          title: "Número de ruta inválido",
          text: "Debe tener exactamente 9 dígitos.",
        });

        return;
      }

      var data = {
        accountId: user.alpacaAccountId,
        userId: user.userId,
        accountOwnerName: user.name,
        bankAccountType: accountType,
        bankAccountNumber: accountNumber,
        bankRoutingNumber: routingNumber,
      };

      await relateBankAccount(data);

      Swal.fire({
        icon: "success",
        title: "Proceso exitoso",
        text: "La cuenta bancaria ha sido enviada a revisión.",
      }).then(() => {
        navigate("/"); // Redirige al home
      });
    } catch (error) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: error.message || "Ocurrió un error inesperado.",
      });
    } finally {
      setIsSending(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Relacionar Cuenta Bancaria</h2>
        <p>
          Por favor, ingrese los detalles de su cuenta bancaria relacionados a{" "}
          <span>{user.name}</span>.
        </p>
      </div>

      <div className={styles.formContainer}>
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.inputGroup}>
            <label htmlFor="accountType">Tipo de cuenta</label>

            <select
              name="accountType"
              id="accountType"
              required
              onChange={(e) => setAccountType(e.target.value)}
              value={accountType}
            >
              <option value="CHECKING">Cuenta Corriente</option>
              <option value="SAVINGS">Cuenta de Ahorros</option>
            </select>
          </div>

          <div className={styles.inputGroup}>
            <label htmlFor="accountNumber">Número de Cuenta</label>
            <input
              type="text"
              id="accountNumber"
              placeholder="Ingrese el número de cuenta"
              required
              onChange={(e) => setAccountNumber(e.target.value)}
              value={accountNumber}
            />
          </div>

          <div className={styles.inputGroup}>
            <label htmlFor="routingNumber">Número de Ruta</label>
            <input
              type="text"
              id="routingNumber"
              placeholder="Ingrese el número de ruta"
              required
              onChange={(e) => setRoutingNumber(e.target.value)}
              value={routingNumber}
            />
          </div>

          {
            <button
              type="submit"
              className={styles.submitButton}
              disabled={isSending}
            >
              {isSending ? "Enviando..." : "Enviar"}
            </button>
          }
        </form>
      </div>
    </div>
  );
};
