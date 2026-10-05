// Contrato de una unidad (forma del UnidadResponse de la API). Se usa para elegir la unidad de una residencia.
export type EstadoUnidad = 'ACTIVA' | 'INACTIVA';

export interface Unidad {
  unidadId: number;
  numeroUnidad: string;
  tipo: string;
  estado: EstadoUnidad;
}
