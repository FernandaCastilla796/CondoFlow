import type { Persona } from '../models/Persona';

// Cuerpo de POST /api/personas: igual a CrearPersonaRequest del backend.
// No lleva personaId (lo genera PostgreSQL) ni estado (toda persona nueva nace ACTIVO).
export type PersonaCreateRequest = Omit<Persona, 'personaId' | 'estado'>;
