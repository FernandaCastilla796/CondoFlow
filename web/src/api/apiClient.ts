// Única puerta HTTP del frontend (Guía 05): URL base, cabeceras JSON, response.ok, errores y lectura del JSON.
// Los services usan apiFetch y no repiten esta lógica.
const API_URL = import.meta.env.VITE_API_URL;

export class ApiError extends Error {
  status: number;
  fieldErrors: Record<string, string>;

  constructor(status: number, message: string, fieldErrors: Record<string, string> = {}) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

// El backend responde los errores con el formato ApiError de GlobalExceptionHandler:
// { status, error, message, path, fieldErrors }. Si el cuerpo no es ese JSON, se usa el texto tal cual.
function leerError(status: number, statusText: string, detail: string): ApiError {
  try {
    const body = JSON.parse(detail) as { message?: string; fieldErrors?: Record<string, string> };
    if (body.message) {
      return new ApiError(status, body.message, body.fieldErrors ?? {});
    }
  } catch {
    // El cuerpo no era JSON: se conserva el texto recibido.
  }
  return new ApiError(status, detail || statusText || 'Error HTTP');
}

export async function apiFetch<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  if (!API_URL) {
    throw new Error('Falta configurar VITE_API_URL');
  }

  const headers = new Headers(options.headers);
  if (options.body && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json');
  }

  let response: Response;
  try {
    response = await fetch(`${API_URL}${endpoint}`, { ...options, headers });
  } catch (err) {
    // fetch sólo lanza si no hubo respuesta: backend apagado, red caída o CORS rechazado.
    if (err instanceof TypeError) {
      throw new Error('No se pudo conectar con el backend. Verifique que esté encendido y que CORS permita este origen.');
    }
    throw err;
  }

  // fetch NO lanza por un 404 o un 500: hay que revisar response.ok.
  if (!response.ok) {
    const detail = await response.text();
    throw leerError(response.status, response.statusText, detail);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}
