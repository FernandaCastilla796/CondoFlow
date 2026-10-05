import { Route, Routes } from 'react-router';
import MainLayout from '../layouts/MainLayout';
import AcercaPage from '../pages/AcercaPage';
import DashboardPage from '../pages/DashboardPage';
import NotFoundPage from '../pages/NotFoundPage';
import PersonasPage from '../features/personas/pages/PersonasPage';
import ResidenciasPage from '../features/residencias/pages/ResidenciasPage';

// Declaración central de rutas: qué componente corresponde a cada dirección.
export default function AppRouter() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="personas" element={<PersonasPage />} />
        <Route path="residencias" element={<ResidenciasPage />} />
        <Route path="acerca" element={<AcercaPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
