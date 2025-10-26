import styles from "./Recharge.module.css";
import {useEffect, useState} from "react";
import Swal from "sweetalert2";
import CurrencyInput from "react-currency-input-field";
import {getRechargeHistory, rechargeAccount} from "../../services/rechargeAccountService";
import { RechargeTable } from "../RechargeTable/RechargeTable";

export const Recharge = ({ user }) => {
  const [amount, setAmount] = useState(0);
  const [isSending, setIsSending] = useState(false);

  const [rechargeHistory, setRechargeHistory] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    updateHistoryTable();
  }, [getRechargeHistory]);

  const handleChange = (value) => {
    const numericValue = parseFloat(value);

    if (!numericValue || numericValue <= 0) {
      setAmount("");
      return;
    }

    setAmount(numericValue);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSending(true);

    try {
      if (!amount || amount <= 0) {
        Swal.fire({
          icon: "warning",
          title: "Campos incompletos",
          text: "Por favor, ingrese un monto valido.",
        });

        return;
      }

      var data = {
        userId: user.userId,
        amount: amount,
      };

      await rechargeAccount(data);

      Swal.fire({
        icon: "success",
        title: "Gracias por tu transferencia",
        text: "tu recarga se esta procesando, esto puede tardar hasta 20 min",
      });

      updateHistoryTable();
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

  const updateHistoryTable = () => {
    try {
      const fetchRechargeHistory = async () => {
        const data = await getRechargeHistory(user.userId);
        setRechargeHistory(data);
      };

      fetchRechargeHistory()
          .then(() => setIsLoading(false))
          .catch((error) => {
            console.error("Error fetching recharge history:", error);
            setIsLoading(false);
          });
    } catch (error) {}
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Recarga De Cuenta</h2>
        <p>
          Por favor, ingrese la información necesaria para realizar la recarga a
          la cuenta de <span>{user.name}</span>.
        </p>
      </div>

      <div className={styles.formContainer}>
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.inputGroup}>
            <label htmlFor="accountNumber">Cantidad de recarga</label>

            <CurrencyInput
              id="amount"
              name="amount"
              placeholder="Ingrese la cantidad"
              decimalsLimit={0}
              value={amount}
              prefix="$"
              groupSeparator=","
              decimalSeparator="."
              intlConfig={{ locale: "en-US", currency: "USD" }}
              onValueChange={handleChange}
              className={styles.input}
            />
          </div>

          {
            <button
              type="submit"
              className={styles.submitButton}
              disabled={isSending}
            >
              {isSending ? "Recargando..." : "Recargar"}
            </button>
          }
        </form>
      </div>

      {/* Tabla de historial de recargas */}

      <div className={styles.historyContainer}>
        <RechargeTable rechargeHistory={rechargeHistory} isLoading={isLoading} />
      </div>
    </div>
  );
};
