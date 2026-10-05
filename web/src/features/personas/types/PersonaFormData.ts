// Lo que el usuario está escribiendo en el formulario de persona (todo texto mientras se edita).
// No incluye personaId ni estado: los decide el sistema al registrar (toda persona nueva nace ACTIVO).
export interface PersonaFormData {
  nombre: string;
  apellido: string;
  documento: string;
  telefono: string;
  correoElectronico: string;
}
