// src/pages/InvestorAdmin.jsx
import InvestorManagement from "../components/Layout/InvestorManagement";

export default function InvestorAdmin() {
  return (
    <div className="admin-issuers">
      <div className="layout">
        <header className="header">
          <div className="brand">
            <h1>Gestión de Inversionistas</h1>
            <small>Andina Trading</small>
          </div>
        </header>
        <main className="content">
          <div className="container">
            <section className="panel">
              <InvestorManagement />
            </section>
          </div>
        </main>
      </div>
    </div>
  );
}