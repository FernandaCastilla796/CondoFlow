// Tipo escrito a mano a partir del concepto Persona del proyecto (no copiado automáticamente de Java).
export type Persona = {
  personaId: number;
  nombre: string;
  apellido: string;
  documento: string;
  telefono: string;
  correoElectronico: string;
  estado: 'ACTIVO' | 'INACTIVO';
};
