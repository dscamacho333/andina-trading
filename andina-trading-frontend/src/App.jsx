// src/app.jsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CityCountryManagement from './pages/CityCountryManagement';
import IssuerAdmin from './pages/issuerAdmin.jsx';
import BrokerAdmin from './pages/BrokerAdmin.jsx';
import InvestorAdmin from './pages/InvestorAdmin.jsx';
import AdminLogs from './pages/AdminLogs.jsx';

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/admin/cityCountryManagement" element={<CityCountryManagement />} />
        <Route path="/admin/issuerManagement" element={<IssuerAdmin />} />
        <Route path="/admin/brokerManagement" element={<BrokerAdmin />} />
        <Route path="/admin/investorManagement" element={<InvestorAdmin />} />
        <Route path="/admin/logs" element={<AdminLogs />} />
      </Routes>
    </Router>
  );
}