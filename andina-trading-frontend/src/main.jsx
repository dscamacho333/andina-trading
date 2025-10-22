import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import { NotificationProvider } from './context/NotificationContext.jsx';
import './styles/base.css';
import './styles/admin-theme.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <NotificationProvider>
      <App />
    </NotificationProvider>
  </React.StrictMode>
);
