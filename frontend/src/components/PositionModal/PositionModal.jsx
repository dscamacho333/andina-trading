import Modal from "react-modal";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import styles from "./PositionModal.module.css";
import { sellPosition } from "../../services/positionService";

const MySwal = withReactContent(Swal);

export const PositionModal = ({ position, isOpen, onClose }) => {
  const handleSaleClick = async () => {
    const orderTypeToInputs = {
      LIMIT: ["limitPrice"],
      STOP: ["stopPrice"],
      STOP_LIMIT: ["limitPrice", "stopPrice"],
      MARKET: [],
    };

    const tifDescriptions = {
      GTC: "Good Till Cancelled: válida hasta que se cancele.",
      IOC: "Immediate or Cancel: ejecuta de inmediato o cancela.",
      FOK: "Fill or Kill: ejecuta completamente o cancela.",
      DAY: "Solo válida por el día actual.",
      OPG: "Orden de apertura.",
      CLS: "Orden de cierre.",
    };

    const saleComission = 0.02;

    const container = document.createElement("div");
    container.innerHTML = `
      <input
          type="number"
          id="quantity"
          class="swal2-input"
          placeholder="Cantidad"
          min="1"
          step="1"
      >
            
      <select id="orderType" class="swal2-input">
        <option value="MARKET">MARKET</option>
        <option value="LIMIT">LIMIT</option>
        <option value="STOP">STOP</option>
        <option value="STOP_LIMIT">STOP LIMIT</option>
      </select>

      <input type="number" id="limitPrice" class="swal2-input" placeholder="Limit Price" style="display:none;">
      <input type="number" id="stopPrice" class="swal2-input" placeholder="Stop Price" style="display:none;">
      
      <select id="timeInForce" class="swal2-input">
        <option value="GTC">GTC</option>
        <option value="IOC">IOC</option>
        <option value="FOK">FOK</option>
        <option value="DAY">DAY</option>
        <option value="OPG">OPG</option>
        <option value="CLS">CLS</option>
      </select>

      <div id="tifDescription" style="margin-top:10px; font-size:12px; color:gray;"></div><hr />

      <div id="orderPreview" style="text-align:left; font-size:14px; margin-top:10px;">
        <strong>Previsualización según costo actual:</strong><br><br>
        Valor de las acciones: $0<br>
        Comisión (2%): $0<br>
        <strong>Total estimado despues de comisión: $0</strong>
      </div>
    `;

    const { value: formValues } = await MySwal.fire({
      title: `Vender ${position.symbol}`,
      html: container,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: "Confirmar",
      cancelButtonText: "Cancelar",
      didOpen: () => {
        const orderTypeEl = document.getElementById("orderType");
        const limitInput = document.getElementById("limitPrice");
        const stopInput = document.getElementById("stopPrice");
        const tifSelect = document.getElementById("timeInForce");
        const tifDescription = document.getElementById("tifDescription");
        const quantityInput = document.getElementById("quantity");
        const orderPreview = document.getElementById("orderPreview");
        const confirmButton = document.querySelector(".swal2-confirm");

        const disableConfirmButton = () => {
          confirmButton.setAttribute("disabled", "true");
          confirmButton.style.cursor = "not-allowed";
          confirmButton.style.opacity = "0.5";
          confirmButton.style.pointerEvents = "none";
        };

        const enableConfirmButton = () => {
          confirmButton.removeAttribute("disabled");
          confirmButton.style.cursor = "pointer";
          confirmButton.style.opacity = "1";
          confirmButton.style.pointerEvents = "auto";
        };

        disableConfirmButton();

        const updateInputs = () => {
          const selected = orderTypeEl.value;
          limitInput.style.display = orderTypeToInputs[selected]?.includes(
            "limitPrice"
          )
            ? "block"
            : "none";
          stopInput.style.display = orderTypeToInputs[selected]?.includes(
            "stopPrice"
          )
            ? "block"
            : "none";
        };

        const updateTifDescription = () => {
          const val = tifSelect.value;
          tifDescription.textContent = tifDescriptions[val] || "";
        };

        const updatePreview = () => {
          const quantity = parseInt(quantityInput.value) || 0;
          const price = position.current_price;
          const subtotal = quantity * price;
          const commission = Math.round(subtotal * saleComission * 100) / 100;
          const total = Math.round((subtotal - commission) * 100) / 100;

          orderPreview.innerHTML = `
            <strong>Previsualización según costo actual:</strong><br>

            Subtotal: $${subtotal <= 0 ? 0.0 : subtotal.toFixed(2)}<br>
            Comisión (2%): $${commission <= 0 ? 0.0 : commission.toFixed(2)}<br>

            <strong>Total estimado: $${
              total <= 0 ? 0.0 : total.toFixed(2)
            }</strong>
          `;

          if (quantity <= 0) {
            disableConfirmButton();
            return;
          }

          if (quantity > position.qty_available) {
            orderPreview.innerHTML += `<br><span style="color:red;">No puedes vender más de ${position.qty_available} acciones.</span>`;
            disableConfirmButton();
            return;
          }

          enableConfirmButton();
        };

        orderTypeEl.addEventListener("change", updateInputs);
        tifSelect.addEventListener("change", updateTifDescription);
        quantityInput.addEventListener("input", updatePreview);

        updateInputs();
        updateTifDescription();
        updatePreview();
      },
      preConfirm: () => {
        const quantity = parseInt(document.getElementById("quantity").value);
        const orderType = document.getElementById("orderType").value;
        const timeInForce = document.getElementById("timeInForce").value;
        const limitPrice = document.getElementById("limitPrice").value;
        const stopPrice = document.getElementById("stopPrice").value;

        if (!quantity || quantity <= 0) {
          Swal.showValidationMessage("La cantidad debe ser mayor que 0");
          return false;
        }

        if (orderType === "LIMIT" || orderType === "STOP_LIMIT") {
          if (!limitPrice || parseFloat(limitPrice) <= 0) {
            Swal.showValidationMessage("Limit Price requerido");
            return false;
          }
        }

        if (orderType === "STOP" || orderType === "STOP_LIMIT") {
          if (!stopPrice || parseFloat(stopPrice) <= 0) {
            Swal.showValidationMessage("Stop Price requerido");
            return false;
          }
        }

        return {
          quantity,
          orderType,
          timeInForce,
          limitPrice: limitPrice ? parseFloat(limitPrice) : undefined,
          stopPrice: stopPrice ? parseFloat(stopPrice) : undefined,
        };
      },
    });

    if (formValues) {
      try {
        const response = await sellPosition({
          position,
          quantity: formValues.quantity,
          orderType: formValues.orderType,
          timeInForce: formValues.timeInForce,
          limitPrice: formValues.limitPrice,
          stopPrice: formValues.stopPrice,
        });

        const statusText =
          response.alpacaStatus === "FILLED"
            ? "Ejecutada al instante"
            : "Reservada";

        Swal.fire('¡Éxito!', `Orden #${response.orderId} de venta enviada.<br><strong>Estado:</strong> ${statusText}`, 'success');


      } catch (error) {
        Swal.fire('Error', error.response?.data?.message || 'No se pudo crear la orden', 'error');
      }
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="Detalles de la posición"
      className={styles.modal}
      overlayClassName={styles.overlay}
    >
      <div className={styles.modalHeader}>
        <h3 className={styles.modalPositionName}>Posición {position.symbol}</h3>
      </div>

      <div className={styles.modalContent}>
        <div className={styles.modalContentItem}>
          <strong>Cantidad:</strong> {position.qty_available}
        </div>
        <div className={styles.modalContentItem}>
          <strong>Precio actual:</strong> ${position.current_price}
        </div>
        <div className={styles.modalContentItem}>
          <strong>Valor total:</strong> ${position.market_value}
        </div>
      </div>

      <div className={styles.footerContent}>
        <button className={styles.btnDanger} onClick={handleSaleClick}>
          Vender
        </button>

        <button
          className={styles.btnBase}
          onClick={onClose}
          style={{ marginLeft: 10 }}
        >
          Cerrar
        </button>
      </div>
    </Modal>
  );
};
