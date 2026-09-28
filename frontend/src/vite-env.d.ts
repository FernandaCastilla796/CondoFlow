/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** URL base del backend Spring Boot, por ejemplo http://localhost:8080 */
  readonly VITE_API_BASE_URL?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
