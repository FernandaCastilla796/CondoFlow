import type { Persona } from '../models/Persona';

// Los tipos se nombran por intención (Guía 06). Aquí además tienen formas distintas, igual que en el backend:
// - POST /api/personas (CrearPersonaRequest): sin id ni estado, porque toda persona nueva nace ACTIVO.
// - PUT /api/personas/{id} (ActualizarPersonaRequest): representación completa, incluido el estado.
//   El id no va en el cuerpo: viaja en la URL.
export type PersonaCreateRequest = Omit<Persona, 'personaId' | 'estado'>;
export type PersonaUpdateRequest = Omit<Persona, 'personaId'>;
