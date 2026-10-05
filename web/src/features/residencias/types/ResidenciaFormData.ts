// Mientras el usuario escribe, input y select entregan texto: personaId y unidadId son string aquí
// aunque en el modelo Residencia sean number. Se validan primero y se convierten al construir el payload.
export interface ResidenciaFormData {
  personaId: string;
  unidadId: string;
  tipoResidencia: string;
  fechaInicio: string;
}
