/**
 * Formato único de error que devuelve el backend (GlobalExceptionHandler → ApiError).
 * Es un tipo compartido por todas las features.
 */
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  fieldErrors: Record<string, string>;
}
