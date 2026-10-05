// Contrato de datos de una persona en el frontend. Sigue la forma del PersonaResponse que expone la API;
// no es la entidad JPA ni crea ninguna tabla: sólo existe para el chequeo estático de TypeScript.
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
