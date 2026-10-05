import type { Unidad } from '../models/Unidad';

// Catálogo simulado de unidades para el selector del formulario de residencias.
export const unidadesMock: Unidad[] = [
  { unidadId: 1, numeroUnidad: 'A-102', tipo: 'Departamento', estado: 'ACTIVA' },
  { unidadId: 2, numeroUnidad: 'B-201', tipo: 'Departamento', estado: 'ACTIVA' },
  { unidadId: 3, numeroUnidad: 'C-301', tipo: 'Departamento', estado: 'ACTIVA' },
  { unidadId: 4, numeroUnidad: 'D-101', tipo: 'Casa', estado: 'INACTIVA' },
];
