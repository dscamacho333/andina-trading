// src/pages/Stocks/StocksPage.jsx

import React, { useEffect, useState } from 'react';
import DashboardLayout from '../../layouts/DashboardLayout';
import StockCard from '../../components/StockCard/StockCard';
import { fetchStocks } from '../../services/stockService';
import styles from './StocksPage.module.css';
import StockModal from '../../components/StockModal/StockModal';
import { Loading } from '../../components/Loading/Loading';

const StocksPage = () => {
    const [stocks, setStocks] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedStock, setSelectedStock] = useState(null);

    useEffect(() => {
        setIsLoading(true);
        fetchStocks()
            .then((data) => {
                if (Array.isArray(data)) {
                    setStocks(data);
                } else if (Array.isArray(data.stocks)) {
                    setStocks(data.stocks); 
                } else {
                    console.error('Respuesta inesperada de fetchStocks:', data);
                    setStocks([]); 
                }
            })
            .catch(console.error)
            .finally(() => setIsLoading(false));
    }, []);


    return (
        <DashboardLayout>
            <div>
                {isLoading ? (
                    <Loading message={"Cargando acciones..."} />
                ) : (
                    <div className={styles.container}>
                        <div className={styles['stock-grid']}>
                            {stocks.map(stock => (
                                <StockCard key={stock.symbol} stock={stock} onClick={() => setSelectedStock(stock)} />
                            ))}
                        </div>
                    </div>
                )}
            </div>
            {selectedStock && (
                <StockModal
                    stock={selectedStock}
                    isOpen={true}
                    onClose={() => setSelectedStock(null)}
                />
            )}

        </DashboardLayout>

    );
};

export default StocksPage;
