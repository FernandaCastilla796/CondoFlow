// Tipo escrito a mano a partir del concepto Residencia del proyecto (no copiado automáticamente de Java).
export type Residencia = {
  residenciaId: number;
  personaId: number;
  unidadId: number;
  tipoResidencia: 'PROPIETARIO' | 'INQUILINO';
  fechaInicio: string;
  fechaFin: string | null;
  estado: 'VIGENTE' | 'FINALIZADA';
};
