// src/config/sidebarConfig.js

import DashboardIcon from '../assets/icons/DashboardIcon';
import AssetsIcon from '../assets/icons/AssetsIcon';
import ProviderIcon from '../assets/icons/ProviderIcon';
import StockingIcon from '../assets/icons/StockingIcon';

// Configuración por tipo de usuario o módulo
export const sidebarPresets = {
  default: [
    { label: 'Dashboard', path: '/portfolio', icon: DashboardIcon },
    { label: 'Acciones', path: '/stocks', icon: AssetsIcon },
    { label: 'Proveedores', path: '/alerts', icon: ProviderIcon },
    { label: 'Mi Trading', path: '/register', icon: StockingIcon },
  ],
  admin: [
    { label: 'Panel Admin', path: '/admin', icon: DashboardIcon },
    { label: 'Usuarios', path: '/admin/users', icon: AssetsIcon },
    { label: 'Logs', path: '/admin/logs', icon: ProviderIcon },
  ],
  profile: [
    { label: 'Preferencias', path: '/userpreferences', icon: ProviderIcon },
    { label: 'Notificaciones', path: '/history', icon: AssetsIcon },
  ]
};
