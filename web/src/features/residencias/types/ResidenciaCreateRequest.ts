import type { Residencia } from '../models/Residencia';

// Cuerpo de POST /api/residencias: igual a CrearResidenciaRequest del backend.
// La relación con la persona viaja como personaId; el backend crea la residencia VIGENTE y sin fecha de fin.
export type ResidenciaCreateRequest = Omit<Residencia, 'residenciaId' | 'fechaFin' | 'estado'>;
