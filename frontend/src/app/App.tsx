import { apiBaseUrl } from '../config/env';

/**
 * Componente raíz de CondoFlow.
 * Guía 01: sólo estructura y arranque. En la Guía 02 se agregan React Router y el layout principal.
 */
export default function App() {
  return (
    <main className="app">
      <h1>CondoFlow</h1>
      <p>Administración operativa de condominio</p>
      <p className="app__estado">Frontend en construcción · API: {apiBaseUrl}</p>
    </main>
  );
}
