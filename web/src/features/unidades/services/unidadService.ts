import { apiFetch } from '../../../api/apiClient';
import type { Unidad } from '../models/Unidad';

// Catálogo de unidades (GET /api/unidades) para el selector del formulario de residencias.
export const unidadService = {
  listar(signal?: AbortSignal): Promise<Unidad[]> {
    return apiFetch<Unidad[]>('/unidades', { signal });
  },
};
