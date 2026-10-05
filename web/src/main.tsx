import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router';
import App from './app/App';
import './styles/global.css';
import './styles/forms.css';

// BrowserRouter envuelve a App: cualquier componente descendiente puede usar React Router (Guía 02).
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>,
);
