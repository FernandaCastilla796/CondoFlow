# CondoFlow — Frontend (Guía 01)

React 19 + TypeScript + Vite. Proyecto `condoflow-frontend`, creado con:

```bash
npm create vite@latest frontend -- --template react-ts
```

## Cómo ejecutarlo

```bash
cd frontend
npm install        # sólo la primera vez (instala node_modules desde package-lock.json)
npm run dev        # servidor de desarrollo en http://localhost:5173 (con HMR)
npm run build      # verifica tipos con tsc y genera dist/ para producción
npm run lint       # análisis estático con oxlint
```

Para cambiar la URL del backend: copiar `.env.example` como `.env.local` y ajustar `VITE_API_BASE_URL`.

## Estructura

```text
src/
├── app/                 App.tsx (componente raíz)
├── assets/
├── components/common/
├── config/              env.ts
├── features/
│   ├── personas/        components/PersonaCard.tsx, pages/, services/, types/Persona.ts
│   └── residencias/     components/ResidenciaCard.tsx, pages/, services/, types/Residencia.ts
├── hooks/
├── layouts/
├── pages/
├── routes/
├── services/http/
├── styles/              global.css
├── types/               ApiError.ts
├── utils/
└── main.tsx             punto de entrada
```

## Propósito de las carpetas (explicado por el equipo)

1. **`features/`** — Cada módulo del negocio tiene su propia carpeta con todo lo que le pertenece: componentes, páginas, servicios HTTP y tipos. Si mañana hay que tocar algo de residencias, se trabaja dentro de `features/residencias` sin buscar archivos por todo el proyecto. Es el mismo criterio que usamos en el backend: organizar por módulo de negocio y no por tipo técnico.
2. **`components/common/`** — Sólo para piezas de interfaz que no saben nada del dominio y se reutilizan en cualquier pantalla: botones, inputs, mensajes de error, un indicador de carga. `PersonaCard` no va aquí porque sólo tiene sentido para personas.
3. **`services/http/`** — Aquí vivirá el cliente HTTP común (URL base, cabeceras, manejo del formato `ApiError`). Los servicios de cada feature lo usan para llamar al backend; así, si cambia la forma de conectarse, se cambia en un solo lugar.
4. **`config/`** — Lee las variables de entorno de forma controlada. Vite sólo publica las que empiezan con `VITE_`, y todo lo que está aquí termina en el navegador, por eso nunca se ponen contraseñas.
5. **`types/`** — Tipos TypeScript compartidos por varias features. Por ejemplo `ApiError`: todas las pantallas reciben los errores del backend con el mismo formato.
6. **`layouts/` y `routes/`** — Quedan preparadas para la Guía 02: el layout con menú y cabecera, y la definición de rutas con React Router.

## Decisiones

- **Tipos escritos a partir del contrato HTTP**, no copiados de las clases Java: `Persona.ts` y `Residencia.ts` reflejan `PersonaResponse` y `ResidenciaResponse` de la API. Los estados son uniones de strings (`'VIGENTE' | 'FINALIZADA'`) que coinciden con los CHECK de PostgreSQL, y así TypeScript rechaza un estado mal escrito antes de ejecutar.
- **`App.tsx` se movió a `src/app/`** y `main.tsx` se actualizó para importarlo desde ahí.
- Se eliminó el contenido de demostración de Vite (logos, `App.css` y el README de la plantilla).
- `node_modules/` y `dist/` no se suben a Git: se regeneran con `npm install` y `npm run build`.
- Todavía **no** se instalaron Axios, React Router ni librerías de componentes (fuera del alcance de la Guía 01).

## Evidencia

- `npm run build` → `✓ built`, sin errores de TypeScript.
- `npm run lint` → sin advertencias.
- `npm run dev` → la página muestra "CondoFlow · Frontend en construcción" en `http://localhost:5173`.

## Respuestas de defensa (resumen)

| Pregunta | Respuesta |
|---|---|
| Node.js vs npm vs Vite vs React vs TypeScript | Node ejecuta JavaScript fuera del navegador; npm instala paquetes; Vite es el servidor de desarrollo y el empaquetador; React construye la interfaz con componentes; TypeScript agrega tipos que se verifican antes de ejecutar. |
| ¿Punto de entrada? | `src/main.tsx`, que monta `<App />` en el `<div id="root">` de `index.html`. |
| ¿Para qué sirve `package.json`? | Describe el proyecto, sus scripts (`dev`, `build`, `lint`) y dependencias. |
| ¿Por qué no subir `node_modules`? | Pesa mucho y se reconstruye exacto con `npm install` gracias a `package-lock.json`. |
| `npm run dev` vs `npm run build` | `dev` levanta un servidor con recarga en caliente (HMR); `build` verifica tipos y genera archivos optimizados en `dist/`. |
| ¿Por qué el frontend no se conecta a PostgreSQL? | El navegador sólo habla con la API HTTP del backend. Las reglas de negocio y las credenciales de la base viven en el servidor. |
