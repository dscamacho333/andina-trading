// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { NotificationProvider } from "./context/NotificationContext";
import { UserProvider } from './context/UserContext';
import { BalanceProvider } from './context/BalanceContext';
import { SidebarProvider } from "./context/SidebarContext";

// Pages
import StockPage from "./pages/Stocks/StocksPage";
import RegisterPage from "./pages/Register/RegisterPage";
import LoginPage from "./pages/Login/LoginPage";
import { RechargeAccount } from "./pages/RechargeAccount/RechargeAccount";
import { UserProfilePage } from "./pages/UserProfile/UserProfilePage";
import PortfolioPage from './pages/Portfolio/PortfolioPage';
import NotFoundPage from './pages/NotFound/NotFoundPage';


function App() {
  return (
    <Router>
      <SidebarProvider>
        <NotificationProvider>
          <UserProvider> {/* Agrega el UserProvider aquí */}
            <BalanceProvider> {/* Ahora también dentro del UserProvider */}
              <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/portfolio" element={<PortfolioPage />} />
                <Route path="/stocks" element={<StockPage />} />
                <Route path="/userpreferences" element={<UserProfilePage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/recharge-account" element={<RechargeAccount />} />
                <Route path="*" element={<NotFoundPage />} />
              </Routes>
            </BalanceProvider>
          </UserProvider>
        </NotificationProvider>
      </SidebarProvider>
    </Router>
  );
}

export default App;
