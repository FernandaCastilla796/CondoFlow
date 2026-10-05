// Mientras el usuario escribe, input y select entregan texto: personaId y unidadId son string aquí
// aunque en el modelo Residencia sean number. Se validan primero y se convierten al construir el payload.
// finalizada y fechaFin sólo se usan al editar: una residencia nueva siempre nace VIGENTE.
export interface ResidenciaFormData {
  personaId: string;
  unidadId: string;
  tipoResidencia: string;
  fechaInicio: string;
  finalizada: boolean;
  fechaFin: string;
}
