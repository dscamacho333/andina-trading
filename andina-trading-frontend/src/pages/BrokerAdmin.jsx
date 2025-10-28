// src/pages/BrokerAdmin.jsx
import BrokerManagement from "../components/Layout/BrokerManagement";

export default function BrokerAdmin() {
  return (
    <div className="admin-issuers">
      <div className="layout">
        <header className="header">
          <div className="brand">
            <h1>Gestión de comisionistas</h1>
            <small>Andina Trading</small>
          </div>
        </header>
        <main className="content">
          <div className="container">
            <section className="panel">
              <BrokerManagement />
            </section>
          </div>
        </main>
      </div>
    </div>
  );
}