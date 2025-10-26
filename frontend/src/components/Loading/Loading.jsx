import styles from "./Loading.module.css";

export const Loading = ({ message }) => {
  return (
    <div className={styles.loaderWrapper}>
      <div className={styles.hole}>
        {Array.from({ length: 10 }).map((_, index) => (
          <i key={index}></i>
        ))}
      </div>
      {message && <span className={styles.loaderText}>{message}</span>}
    </div>
  );
};
