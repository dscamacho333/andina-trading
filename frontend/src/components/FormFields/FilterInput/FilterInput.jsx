import React from "react";
import styles from "./FilterInput.module.css";

const FilterInput = ({ label, options, selected, onChange }) => {
    return (
        <div className={styles.container}>
            <label className={styles.label}>{label}:</label>
            <select
                className={styles.select}
                value={selected}
                onChange={(e) => onChange(e.target.value)}
            >
                <option value="">Todos</option>
                {options.map((opt) => (
                    <option key={opt} value={opt}>
                        {opt}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default FilterInput;
