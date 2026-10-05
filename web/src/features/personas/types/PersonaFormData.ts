// Lo que el usuario está escribiendo en el formulario de persona (todo texto mientras se edita).
// activo sólo se usa al editar: al crear, la persona siempre nace ACTIVO y la API no recibe el estado.
export interface PersonaFormData {
  nombre: string;
  apellido: string;
  documento: string;
  telefono: string;
  correoElectronico: string;
  activo: boolean;
}
