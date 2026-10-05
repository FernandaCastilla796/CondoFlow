import type { PersonaFormData } from '../types/PersonaFormData';

export type PersonaFormErrors = Partial<Record<keyof PersonaFormData, string>>;

// Mismas reglas que CrearPersonaRequest en el backend. Aquí mejoran la experiencia del usuario;
// la validación real sigue estando en Spring Boot (@Valid) y en PostgreSQL.
export function validarPersona(data: PersonaFormData): PersonaFormErrors {
  const errors: PersonaFormErrors = {};

  if (!data.nombre.trim()) {
    errors.nombre = 'El nombre es obligatorio.';
  } else if (data.nombre.trim().length > 100) {
    errors.nombre = 'El nombre admite como máximo 100 caracteres.';
  }

  if (!data.apellido.trim()) {
    errors.apellido = 'El apellido es obligatorio.';
  } else if (data.apellido.trim().length > 100) {
    errors.apellido = 'El apellido admite como máximo 100 caracteres.';
  }

  if (!data.documento.trim()) {
    errors.documento = 'El documento es obligatorio.';
  } else if (!/^[A-Za-z0-9-]{5,50}$/.test(data.documento.trim())) {
    errors.documento = 'El documento debe tener entre 5 y 50 letras, números o guiones.';
  }

  if (!data.telefono.trim()) {
    errors.telefono = 'El teléfono es obligatorio.';
  } else if (!/^\+?[0-9 ]{7,30}$/.test(data.telefono.trim())) {
    errors.telefono = 'El teléfono debe tener entre 7 y 30 dígitos y puede iniciar con +.';
  }

  if (!data.correoElectronico.trim()) {
    errors.correoElectronico = 'El correo electrónico es obligatorio.';
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.correoElectronico.trim())) {
    errors.correoElectronico = 'El correo electrónico no tiene un formato válido.';
  } else if (data.correoElectronico.trim().length > 150) {
    errors.correoElectronico = 'El correo electrónico admite como máximo 150 caracteres.';
  }

  return errors;
}
