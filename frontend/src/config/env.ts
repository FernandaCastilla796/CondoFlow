/**
 * Lectura controlada de variables de entorno.
 * Vite sólo expone al navegador las variables que empiezan con VITE_.
 * Nunca poner contraseñas ni tokens aquí: este código termina en el navegador del usuario.
 */
export const apiBaseUrl: string = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';
