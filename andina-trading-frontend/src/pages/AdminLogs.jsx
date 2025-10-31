// src/pages/AdminLogs.jsx
import AdminLogsManagement from "../components/Layout/AdminLogsManagement";

export default function AdminLogs() {
  return (
    <div className="admin-issuers">
      <div className="layout">
        <header className="header">
          <div className="brand">
            <h1>Gestión de reportes de logs</h1>
            <small>Andina Trading</small>
          </div>
        </header>
        <main className="content">
          <div className="container">
            <section className="panel">
              <AdminLogsManagement />
            </section>
          </div>
        </main>
      </div>
    </div>
  );
}