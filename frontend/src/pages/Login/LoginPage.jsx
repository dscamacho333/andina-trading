import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../../services/userService";
import { useUser } from "../../context/UserContext";
import { useNotification } from "../../context/NotificationContext";
import styles from "./LoginPage.module.css";

const LoginPage = () => {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const { notify } = useNotification();
  const navigate = useNavigate();
  const { setUser } = useUser();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const { token, user } = await loginUser(formData);
      localStorage.setItem("token", token);
      setUser(user);
      navigate("/portfolio");
    } catch {
      notify(" Inicio de sesión fallido. Verifica tus datos.", "danger"); 
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.logoPlaceholder}></div>

        <h2 className={styles.title}>Ingresa a tu cuenta</h2>
        <p className={styles.subtitle}>
          Ingresa a tu cuenta y disfruta de la experiencia Forest Trade!
        </p>

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.inputGroup}>
            <input
              type="email"
              name="email"
              placeholder="name@mail.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className={styles.inputGroup}>
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className={styles.signInButton}>
            Inicia sesión
          </button>
        </form>

        <div className={styles.separator}>
          <hr />
          <span>Or</span>
          <hr />
        </div>

        <button className={styles.googleButton}>
           Ingresa con Google
        </button>

        <button className={styles.appleButton}>
           Ingresa con Apple
        </button>

        <p className={styles.terms}>
          ¿Aún no tienes cuenta de Forest Trade? 
          <a href="/register"><br/> Regístrate aquí</a>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
