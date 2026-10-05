import { apiFetch } from '../../../api/apiClient';
import type { Persona } from '../models/Persona';
import type { PersonaCreateRequest } from '../types/PersonaCreateRequest';

// El service conoce los endpoints de /api/personas; los componentes sólo expresan intenciones.
export const personaService = {
  listar(signal?: AbortSignal): Promise<Persona[]> {
    return apiFetch<Persona[]>('/personas', { signal });
  },

  crear(data: PersonaCreateRequest): Promise<Persona> {
    return apiFetch<Persona>('/personas', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },
};
