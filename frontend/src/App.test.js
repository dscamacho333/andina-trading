// src/App.test.js
import { render, screen } from '@testing-library/react';
import App from './App';

beforeEach(() => {
  // Simula el contenedor #root que react-modal espera
  const root = document.createElement('div');
  root.setAttribute('id', 'root');
  document.body.appendChild(root);
});

test('renders learn react link', () => {
  render(<App />);
  const titleElement = screen.getByText(/Inicia sesión/i);
  expect(titleElement).toBeInTheDocument();
});
