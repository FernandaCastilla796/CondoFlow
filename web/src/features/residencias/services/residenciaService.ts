import { apiFetch } from '../../../api/apiClient';
import type { Residencia } from '../models/Residencia';
import type { ResidenciaCreateRequest } from '../types/ResidenciaCreateRequest';

export const residenciaService = {
  listar(signal?: AbortSignal): Promise<Residencia[]> {
    return apiFetch<Residencia[]>('/residencias', { signal });
  },

  crear(data: ResidenciaCreateRequest): Promise<Residencia> {
    return apiFetch<Residencia>('/residencias', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },
};
