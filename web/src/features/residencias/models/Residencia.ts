// Contrato de datos de una residencia en el frontend (forma del ResidenciaResponse de la API).
// Relación 1:N: muchas residencias pueden tener el mismo personaId.
export type TipoResidencia = 'PROPIETARIO' | 'INQUILINO';
export type EstadoResidencia = 'VIGENTE' | 'FINALIZADA';

export interface Residencia {
  residenciaId: number;
  personaId: number;
  unidadId: number;
  tipoResidencia: TipoResidencia;
  fechaInicio: string;
  fechaFin: string | null;
  estado: EstadoResidencia;
}
