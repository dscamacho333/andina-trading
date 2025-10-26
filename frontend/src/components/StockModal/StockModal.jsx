// src/components/StockModal/StockModal.jsx
import React, { useEffect } from "react";
import Modal from 'react-modal';
import Swal from 'sweetalert2';
import withReactContent from 'sweetalert2-react-content';
import styles from './StockModal.module.css';
import { createBuyOrder, getFilteredOrdersByUser } from '../../services/orderService';
import { useNotification } from '../../context/NotificationContext';
import { useBalance } from '../../context/BalanceContext';
import { useUser } from '../../context/UserContext';

const MySwal = withReactContent(Swal);


const StockModal = ({ isOpen, onClose, stock }) => {
    const { balance, loadBalance } = useBalance();
    const { user } = useUser();
    const { notify } = useNotification();

    useEffect(() => {
        if (typeof document !== 'undefined') {
            Modal.setAppElement('#root');
        }
    }, []);

    if (!stock) return null;

    const handleBuyClick = async () => {

        try {
            const orders = await getFilteredOrdersByUser(user.id);

            if (orders.length >= user.dailyOrderLimit) {

                notify('Has alcanzado el límite diario de órdenes' ,'danger');
                return;
            }


            const orderTypeToInputs = {
                "LIMIT": ["limitPrice"],
                "STOP": ["stopPrice"],
                "STOP_LIMIT": ["limitPrice", "stopPrice"],
                "MARKET": []
            };

            const tifDescriptions = {
                GTC: "Good Till Cancelled: válida hasta que se cancele.",
                IOC: "Immediate or Cancel: ejecuta de inmediato o cancela.",
                FOK: "Fill or Kill: ejecuta completamente o cancela.",
                DAY: "Solo válida por el día actual.",
                OPG: "Orden de apertura.",
                CLS: "Orden de cierre."
            };


            const commissionRateForestTrade = 0.02; // 2%

            const container = document.createElement('div');
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
           <div id="tifDescription" style="margin-top:10px; font-size:12px; color:gray;"></div>
        <hr />
        <div id="orderPreview" style="text-align:left; font-size:14px; margin-top:10px;">
            <strong>Previsualización según costo actual:</strong><br><br>
            Subtotal: $0<br>
            Comisión (2%): $0<br>
            <strong>Total estimado: $0</strong>
        </div>
        `;

            const { value: formValues } = await MySwal.fire({
                title: `Comprar ${stock.symbol}`,
                html: container,
                focusConfirm: false,
                showCancelButton: true,
                confirmButtonText: "Confirmar",
                cancelButtonText: "Cancelar",
                didOpen: () => {
                    const orderTypeEl = document.getElementById('orderType');
                    const limitInput = document.getElementById('limitPrice');
                    const stopInput = document.getElementById('stopPrice');
                    const tifSelect = document.getElementById('timeInForce');
                    const tifDescription = document.getElementById('tifDescription');
                    const quantityInput = document.getElementById('quantity');
                    const orderPreview = document.getElementById('orderPreview');
                    const confirmButton = document.querySelector('.swal2-confirm');

                    orderTypeEl.value = user?.defaultOrderType || "MARKET";

                    const disableConfirmButton = () => {
                        confirmButton.setAttribute('disabled', 'true');
                        confirmButton.style.cursor = 'not-allowed';
                        confirmButton.style.opacity = '0.5';
                        confirmButton.style.pointerEvents = 'none';
                    }

                    const enableConfirmButton = () => {
                        confirmButton.removeAttribute('disabled');
                        confirmButton.style.cursor = 'pointer';
                        confirmButton.style.opacity = '1';
                        confirmButton.style.pointerEvents = 'auto';
                    }

                    disableConfirmButton();

                    const updateInputs = () => {
                        const selected = orderTypeEl.value;
                        limitInput.style.display = orderTypeToInputs[selected]?.includes("limitPrice") ? 'block' : 'none';
                        stopInput.style.display = orderTypeToInputs[selected]?.includes("stopPrice") ? 'block' : 'none';
                    };

                    const updateTifDescription = () => {
                        const val = tifSelect.value;
                        tifDescription.textContent = tifDescriptions[val] || '';
                    };

                    const updatePreview = () => {
                        const quantity = parseInt(quantityInput.value) || 0;
                        const price = stock.currentPrice;
                        const subtotal = quantity * price;
                        const commission = Math.round(subtotal * commissionRateForestTrade * 100) / 100;
                        const total = Math.round((subtotal + commission) * 100) / 100;

                        orderPreview.innerHTML = `
                    <strong>Previsualización según costo actual:</strong><br>
                    Subtotal: $${subtotal <= 0 ? 0.00 : subtotal.toFixed(2)}<br>
                    Comisión (2%): $${commission <= 0 ? 0.00 : commission.toFixed(2)}<br>
                    <strong>Total estimado: $${total <= 0 ? 0.00 : total.toFixed(2)}</strong>
                    `;

                        if (quantity <= 0) {
                            disableConfirmButton();
                            return;
                        }

                        if (balance < total) {
                            orderPreview.innerHTML += `<br><span style="color:#df6e6e;">No tienes suficiente saldo para esta orden.</span>`;
                            disableConfirmButton();
                            return
                        }

                        enableConfirmButton();
                    };

                    orderTypeEl.addEventListener('change', updateInputs);
                    tifSelect.addEventListener('change', updateTifDescription);
                    quantityInput.addEventListener('input', updatePreview);

                    updateInputs();
                    updateTifDescription();
                    updatePreview();
                },
                preConfirm: () => {
                    const quantity = parseInt(document.getElementById('quantity').value);
                    const orderType = document.getElementById('orderType').value;
                    const timeInForce = document.getElementById('timeInForce').value;
                    const limitPrice = document.getElementById('limitPrice').value;
                    const stopPrice = document.getElementById('stopPrice').value;

                    if (!quantity || quantity <= 0) {
                        Swal.showValidationMessage("La cantidad debe ser mayor que 0");
                        return false;
                    }

                    if (orderType === 'LIMIT' || orderType === 'STOP_LIMIT') {
                        if (!limitPrice || parseFloat(limitPrice) <= 0) {
                            Swal.showValidationMessage("Limit Price requerido");
                            return false;
                        }
                    }

                    if (orderType === 'STOP' || orderType === 'STOP_LIMIT') {
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
                        stopPrice: stopPrice ? parseFloat(stopPrice) : undefined
                    };
                }
            });

            if (formValues) {
                try {
                    const response = await createBuyOrder(
                        stock,
                        formValues.quantity,
                        formValues.orderType,
                        formValues.timeInForce,
                        formValues.limitPrice,
                        formValues.stopPrice
                    );
                    loadBalance();
                    const estadoTexto = response.alpacaStatus === 'FILLED'
                        ? 'Ejecutada al instante'
                        : 'Reservada';

                    Swal.fire('¡Éxito!', `Orden #${response.orderId} de compra enviada.<br><strong>Estado:</strong> ${estadoTexto}`, 'success');


                } catch (error) {
                    Swal.fire('Error', error.response?.data?.message || 'No se pudo crear la orden', 'error');
                }
            }
        } catch (error) {
            notify(' No se pudo validar el límite de órdenes: ', 'error');
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
            contentLabel="Stock Detail"
            className={styles.modal}
            overlayClassName={styles.overlay}
        >
            <div className={styles.modalHeader}>
                <h3 className={styles.modalStockName}>{stock.stockName} ({stock.symbol})</h3>
            </div>

            <div className={styles.modalContent}>
                <p className={styles.modalContentName}><strong>Mercado:</strong> {stock.market.marketCode}</p>
                <p className={styles.modalContentName}><strong>Precio actual:</strong> ${stock.currentPrice}</p>
                <p className={styles.modalContentName}><strong>Volumen:</strong> {stock.volume.toLocaleString('en-US')}</p>
                <p className={styles.modalContentName}><strong>Capitalización:</strong> {stock.marketCapitalization.toLocaleString('en-US')}</p>
                <p className={styles.modalContentName}><strong>Sector:</strong> {stock.sector}</p>
                <p className={styles.modalContentName}><strong>Industria:</strong> {stock.industry}</p>
            </div>

            <div className={styles.footerContent}>
                <button className={styles.btnSuccess} onClick={handleBuyClick}>Comprar</button>
                <button className={styles.btnBase} onClick={onClose} style={{ marginLeft: 10 }}>Cerrar</button>
            </div>
        </Modal>
    );
};

export default StockModal;