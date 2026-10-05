import type { ResidenciaFormData } from '../types/ResidenciaFormData';

export type ResidenciaFormErrors = Partial<Record<keyof ResidenciaFormData, string>>;

export function validarResidencia(data: ResidenciaFormData): ResidenciaFormErrors {
  const errors: ResidenciaFormErrors = {};
  const anioActual = new Date().getFullYear();

  if (!data.personaId) errors.personaId = 'Debe seleccionar una persona.';
  if (!data.unidadId) errors.unidadId = 'Debe seleccionar una unidad.';

  if (data.tipoResidencia !== 'PROPIETARIO' && data.tipoResidencia !== 'INQUILINO') {
    errors.tipoResidencia = 'Debe elegir Propietario o Inquilino.';
  }

  if (!data.fechaInicio) {
    errors.fechaInicio = 'La fecha de inicio es obligatoria.';
  } else {
    const anio = Number(data.fechaInicio.slice(0, 4));
    if (!Number.isInteger(anio) || anio < 1950 || anio > anioActual + 1) {
      errors.fechaInicio = 'Ingrese una fecha de inicio válida.';
    }
  }

  return errors;
}
