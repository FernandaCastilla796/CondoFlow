/**
 * Tipos del módulo Residencias según el contrato HTTP de /api/residencias.
 * Las uniones de strings reflejan los CHECK de PostgreSQL y los enums del backend.
 */
export type TipoResidencia = 'PROPIETARIO' | 'INQUILINO';

export type EstadoResidencia = 'VIGENTE' | 'FINALIZADA';

export interface Residencia {
  residenciaId: number;
  personaId: number;
  unidadId: number;
  tipoResidencia: TipoResidencia;
  /** Fecha ISO (AAAA-MM-DD) */
  fechaInicio: string;
  /** null mientras la residencia está VIGENTE */
  fechaFin: string | null;
  estado: EstadoResidencia;
}

/** Datos que se envían en POST /api/residencias. */
export interface CrearResidenciaRequest {
  personaId: number;
  unidadId: number;
  tipoResidencia: TipoResidencia;
  fechaInicio: string;
}
