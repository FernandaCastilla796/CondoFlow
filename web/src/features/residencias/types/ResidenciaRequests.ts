import type { Residencia } from '../models/Residencia';

// Cuerpos de las peticiones, iguales a los DTO del backend. La relación con la persona viaja como personaId.
// - POST /api/residencias (CrearResidenciaRequest): la residencia nace VIGENTE y sin fecha de fin.
// - PUT /api/residencias/{id} (ActualizarResidenciaRequest): representación completa; cambiar personaId
//   reasigna la residencia a otra persona y estado FINALIZADA + fechaFin cierra la vigencia.
export type ResidenciaCreateRequest = Omit<Residencia, 'residenciaId' | 'fechaFin' | 'estado'>;
export type ResidenciaUpdateRequest = Omit<Residencia, 'residenciaId'>;
