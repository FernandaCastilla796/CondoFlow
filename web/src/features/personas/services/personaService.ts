import { apiFetch } from '../../../api/apiClient';
import type { Persona } from '../models/Persona';
import type { PersonaCreateRequest, PersonaUpdateRequest } from '../types/PersonaRequests';

// El service conoce los endpoints de /api/personas; los componentes sólo expresan intenciones,
// por ejemplo personaService.actualizar(7, datos) o personaService.eliminar(7).
export const personaService = {
  listar(signal?: AbortSignal): Promise<Persona[]> {
    return apiFetch<Persona[]>('/personas', { signal });
  },

  obtenerPorId(id: number, signal?: AbortSignal): Promise<Persona> {
    return apiFetch<Persona>(`/personas/${id}`, { signal });
  },

  crear(data: PersonaCreateRequest): Promise<Persona> {
    return apiFetch<Persona>('/personas', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  actualizar(id: number, data: PersonaUpdateRequest): Promise<Persona> {
    return apiFetch<Persona>(`/personas/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  eliminar(id: number): Promise<void> {
    return apiFetch<void>(`/personas/${id}`, {
      method: 'DELETE',
    });
  },
};
