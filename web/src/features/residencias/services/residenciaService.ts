import { apiFetch } from '../../../api/apiClient';
import type { Residencia } from '../models/Residencia';
import type { ResidenciaCreateRequest, ResidenciaUpdateRequest } from '../types/ResidenciaRequests';

// CRUD completo de /api/residencias. Para el selector de personas se reutiliza personaService.listar():
// no se duplica un servicio de personas dentro de esta feature.
export const residenciaService = {
  listar(signal?: AbortSignal): Promise<Residencia[]> {
    return apiFetch<Residencia[]>('/residencias', { signal });
  },

  obtenerPorId(id: number, signal?: AbortSignal): Promise<Residencia> {
    return apiFetch<Residencia>(`/residencias/${id}`, { signal });
  },

  crear(data: ResidenciaCreateRequest): Promise<Residencia> {
    return apiFetch<Residencia>('/residencias', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  actualizar(id: number, data: ResidenciaUpdateRequest): Promise<Residencia> {
    return apiFetch<Residencia>(`/residencias/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  eliminar(id: number): Promise<void> {
    return apiFetch<void>(`/residencias/${id}`, { method: 'DELETE' });
  },
};
