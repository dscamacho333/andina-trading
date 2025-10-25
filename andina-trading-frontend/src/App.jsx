// src/app.jsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CityCountryManagement from './pages/CityCountryManagement';
import IssuerAdmin from './pages/issuerAdmin.jsx';

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/admin/cityCountryManagement" element={<CityCountryManagement />} />
        <Route path="/admin/issuerManagement" element={<IssuerAdmin />} />
      </Routes>
    </Router>
  );
}