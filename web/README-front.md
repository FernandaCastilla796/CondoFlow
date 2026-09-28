# CondoFlow — Frontend (Guía 01)

Proyecto **condoflow-frontend** creado con React + TypeScript + Vite, siguiendo la *Guía 01 · Estructuración del Frontend en WebStorm con Vite*.

```bash
npm create vite@latest condoflow-frontend -- --template react-ts
cd condoflow-frontend
npm install
npm run dev
```

En el repositorio el proyecto vive en la carpeta `web/`, según la estructura del monorepo definida en la Clase 01 y en el Manual de Git (`backend/`, `web/`, `mobile/`, `docs/`).

## Propósito de cinco carpetas

1. **`features/`**: guarda todo lo que pertenece a un módulo del negocio. `features/personas` y `features/residencias` tienen cada una sus componentes, páginas, servicios y tipos. Si hay que cambiar algo de residencias, sólo se trabaja dentro de esa carpeta.
2. **`components/common/`**: es para piezas visuales que se pueden reutilizar en cualquier pantalla porque no dependen de un módulo, como un botón o un mensaje de error. `PersonaCard` no va aquí porque sólo sirve para personas.
3. **`services/http/`**: aquí irá el código común para comunicarse con el backend de Spring Boot. Así la forma de llamar a la API se define en un solo lugar y no en cada pantalla.
4. **`config/`**: sirve para leer la configuración general, por ejemplo la URL del backend que se define en `VITE_API_BASE_URL`. Ahí nunca van contraseñas, porque todo el código del frontend termina en el navegador.
5. **`routes/`**: aquí se van a definir las rutas de la aplicación cuando se instale React Router en la siguiente guía.

## Práctica obligatoria

- [x] Proyecto creado con la plantilla `react-ts`.
- [x] `App.tsx` movido a `src/app/App.tsx` y el import de `main.tsx` actualizado.
- [x] Estructura de carpetas de la sección 8 creada.
- [x] `README-front.md` con el propósito de cinco carpetas.
- [x] Componente vacío `PersonaCard.tsx` en `features/personas/components`.
- [x] Componente vacío `ResidenciaCard.tsx` en `features/residencias/components`.
- [x] Tipo `Persona.ts` en `features/personas/types` y tipo `Residencia.ts` en `features/residencias/types`.
- [x] `.env.example` con `VITE_API_BASE_URL`, sin credenciales.
- [x] `npm run build` sin errores.

## Qué no se hizo todavía

Según el alcance de la guía, todavía no se instalaron Axios, React Router, librerías de formularios ni componentes visuales externos, y el frontend no consume la API.
