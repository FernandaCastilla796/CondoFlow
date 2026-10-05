import type { Persona } from '../../personas/models/Persona';
import type { Unidad } from '../../unidades/models/Unidad';
import type { Residencia } from '../models/Residencia';
import type { ResidenciaFormData } from '../types/ResidenciaFormData';

export type ResidenciaFormErrors = Partial<Record<keyof ResidenciaFormData, string>>;

// Valida la relación antes de construir el payload (Guía 07): la persona y la unidad elegidas deben existir
// y estar activas. Al editar se permite conservar la persona o la unidad actuales aunque ya estén inactivas.
// Es una ayuda para el usuario: Spring Boot vuelve a validar todo (404, 409) y PostgreSQL tiene la FK.
export function validarResidencia(
  data: ResidenciaFormData,
  personas: Persona[],
  unidades: Unidad[],
  actual?: Residencia | null,
): ResidenciaFormErrors {
  const errors: ResidenciaFormErrors = {};
  const anioActual = new Date().getFullYear();

  const personaId = Number(data.personaId);
  const personaValida = personas.some(
    (persona) => persona.personaId === personaId
      && (persona.estado === 'ACTIVO' || persona.personaId === actual?.personaId),
  );
  if (!data.personaId) {
    errors.personaId = 'Debe seleccionar una persona.';
  } else if (!personaValida) {
    errors.personaId = 'Seleccione una persona válida y activa.';
  }

  const unidadId = Number(data.unidadId);
  const unidadValida = unidades.some(
    (unidad) => unidad.unidadId === unidadId
      && (unidad.estado === 'ACTIVA' || unidad.unidadId === actual?.unidadId),
  );
  if (!data.unidadId) {
    errors.unidadId = 'Debe seleccionar una unidad.';
  } else if (!unidadValida) {
    errors.unidadId = 'Seleccione una unidad válida y activa.';
  }

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

  // RN-01: una residencia finalizada necesita fecha de fin, y no puede ser anterior al inicio.
  if (data.finalizada) {
    if (!data.fechaFin) {
      errors.fechaFin = 'Indique la fecha de fin para finalizar la residencia.';
    } else if (data.fechaInicio && data.fechaFin < data.fechaInicio) {
      errors.fechaFin = 'La fecha de fin no puede ser anterior a la fecha de inicio.';
    }
  }

  return errors;
}
