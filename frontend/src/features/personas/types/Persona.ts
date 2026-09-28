/**
 * Tipos del módulo Personas, escritos a partir del contrato HTTP de /api/personas
 * (PersonaResponse y CrearPersonaRequest del backend), no copiados de las clases Java.
 */
export type EstadoPersona = 'ACTIVO' | 'INACTIVO';

export interface Persona {
  personaId: number;
  nombre: string;
  apellido: string;
  documento: string;
  telefono: string;
  correoElectronico: string;
  estado: EstadoPersona;
}

/** Datos que se envían en POST /api/personas: sin id ni estado, los decide el backend. */
export interface CrearPersonaRequest {
  nombre: string;
  apellido: string;
  documento: string;
  telefono: string;
  correoElectronico: string;
}
