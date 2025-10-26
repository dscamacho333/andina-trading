import React, { useEffect, useRef, useState } from 'react';
import styles from './StockCard.module.css';

const StockCard = ({ stock, onClick }) => {
    const isPositive = stock.change > 0;
    const titleRef = useRef();
    const wrapperRef = useRef();
    const [shouldAnimate, setShouldAnimate] = useState(false);

    useEffect(() => {
        const titleWidth = titleRef.current.scrollWidth;
        const wrapperWidth = wrapperRef.current.clientWidth;
        setShouldAnimate(titleWidth > wrapperWidth);
    }, [stock.stockName]);

    return (
        <div className={styles.container} onClick={onClick}>
        <div className={styles.card} >
            <div className={styles.header}>
                <div className={styles.logo}>
                    <span className={styles.logoIcon}>({stock.symbol})</span>
                </div>
                <div className={styles.meta}>
                    <p className={styles.type}>Acción</p>
                    <div className={styles.titleWrapper} ref={wrapperRef}>
                        <h3
                            ref={titleRef}
                            className={`${styles.title} ${shouldAnimate ? styles.animate : ''}`}
                        >
                            {stock.stockName}
                        </h3>
                    </div>
                </div>
                <div className={styles.openIcon}>↗</div>
            </div>

            <div className={styles.rewardSection}>
                <p className={styles.rewardLabel}>Precio Actual</p>
                <h2 className={styles.rewardValue}>${stock.currentPrice}</h2>
                <p className={isPositive ? styles.positiveChange : styles.negativeChange}>
                    ● {isPositive ? '+' : ''}{stock.change.toFixed(2)}%
                </p>
            </div>
        </div>
        </div>
    );
};

export default StockCard;
