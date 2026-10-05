import type { Residencia } from '../models/Residencia';

// personaId señala a qué persona pertenece cada residencia: la persona 1 aparece varias veces (relación 1:N).
export const residenciasMock: Residencia[] = [
  { residenciaId: 1, personaId: 1, unidadId: 1, tipoResidencia: 'PROPIETARIO', fechaInicio: '2025-01-15', fechaFin: null, estado: 'VIGENTE' },
  { residenciaId: 2, personaId: 2, unidadId: 2, tipoResidencia: 'PROPIETARIO', fechaInicio: '2025-03-01', fechaFin: null, estado: 'VIGENTE' },
  { residenciaId: 3, personaId: 1, unidadId: 2, tipoResidencia: 'INQUILINO', fechaInicio: '2024-02-01', fechaFin: '2024-12-31', estado: 'FINALIZADA' },
  { residenciaId: 4, personaId: 4, unidadId: 3, tipoResidencia: 'INQUILINO', fechaInicio: '2026-01-10', fechaFin: null, estado: 'VIGENTE' },
  // Práctica Guía 03: tres residencias nuevas asociadas a personas existentes.
  { residenciaId: 5, personaId: 5, unidadId: 4, tipoResidencia: 'INQUILINO', fechaInicio: '2026-06-01', fechaFin: null, estado: 'VIGENTE' },
  { residenciaId: 6, personaId: 4, unidadId: 1, tipoResidencia: 'INQUILINO', fechaInicio: '2023-05-01', fechaFin: '2024-04-30', estado: 'FINALIZADA' },
  { residenciaId: 7, personaId: 2, unidadId: 3, tipoResidencia: 'INQUILINO', fechaInicio: '2024-08-01', fechaFin: '2025-07-31', estado: 'FINALIZADA' },
  // Práctica Guía 03: personaId que no existe, para comprobar "Sin persona asociada".
  { residenciaId: 8, personaId: 999, unidadId: 4, tipoResidencia: 'INQUILINO', fechaInicio: '2026-02-01', fechaFin: null, estado: 'VIGENTE' },
];
