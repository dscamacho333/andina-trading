// src/pages/IssuerAdmin.jsx
import IssuerManagement from '../components/Layout/IssuerManagement';
import '../styles/issuers-theme.css';

export default function IssuerAdmin() {
  return (
    <div className="admin-issuers">
      <div className="layout">
        <header className="header">
          <div className="brand">
            <h1>Gestión de Emisores</h1>
            <small>Andina Trading</small>
          </div>
        </header>

        <main className="content">
          <div className="container">
            {/* Panel general: dentro va tu propio layout de filtros/tabla/form */}
            <div className="panel">
              {/* IssuerManagement ya pinta filtros, botón “Nuevo Emisor”, tabla y (si aplica) el form */}
              <IssuerManagement />
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}