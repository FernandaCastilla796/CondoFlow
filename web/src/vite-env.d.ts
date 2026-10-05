/// <reference types="vite/client" />

// Tipado de las variables de entorno que usa el frontend (Guía 05).
interface ImportMetaEnv {
  readonly VITE_API_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
