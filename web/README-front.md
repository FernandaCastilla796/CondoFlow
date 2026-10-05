# CondoFlow — Frontend

Proyecto **condoflow-frontend** (React + TypeScript + Vite) construido guía por guía. En el repositorio vive en la carpeta `web/`, según la estructura del monorepo definida en la Clase 01 y en el Manual de Git (`backend/`, `web/`, `mobile/`, `docs/`).

Las guías usan el ejemplo ParkFlow360 (Cliente 1:N Vehículo). En CondoFlow la misma relación es **Persona 1:N Residencia**:

| ParkFlow360 (guías) | CondoFlow |
|---|---|
| `features/clientes`, `Cliente`, `/clientes` | `features/personas`, `Persona`, `/personas` |
| `features/vehiculos`, `Vehiculo`, `/vehiculos` | `features/residencias`, `Residencia`, `/residencias` |
| `clienteId` dentro de `Vehiculo` | `personaId` dentro de `Residencia` |

---

## Guía 01 — Estructuración del frontend con Vite

```bash
npm create vite@latest condoflow-frontend -- --template react-ts
cd condoflow-frontend
npm install
npm run dev
```

### Propósito de cinco carpetas

1. **`features/`**: guarda todo lo que pertenece a un módulo del negocio. `features/personas` y `features/residencias` tienen cada una sus componentes, páginas, servicios y tipos. Si hay que cambiar algo de residencias, sólo se trabaja dentro de esa carpeta.
2. **`components/common/`**: es para piezas visuales que se pueden reutilizar en cualquier pantalla porque no dependen de un módulo, como un botón o un mensaje de error. `PersonaCard` no va aquí porque sólo sirve para personas.
3. **`services/http/`**: aquí irá el código común para comunicarse con el backend de Spring Boot. Así la forma de llamar a la API se define en un solo lugar y no en cada pantalla.
4. **`config/`**: sirve para leer la configuración general, por ejemplo la URL del backend que se define en `VITE_API_BASE_URL`. Ahí nunca van contraseñas, porque todo el código del frontend termina en el navegador.
5. **`routes/`**: aquí se van a definir las rutas de la aplicación cuando se instale React Router en la siguiente guía.

### Práctica obligatoria

- [x] Proyecto creado con la plantilla `react-ts`.
- [x] `App.tsx` movido a `src/app/App.tsx` y el import de `main.tsx` actualizado.
- [x] Estructura de carpetas de la sección 8 creada.
- [x] `README-front.md` con el propósito de cinco carpetas.
- [x] Componente vacío `PersonaCard.tsx` en `features/personas/components`.
- [x] Componente vacío `ResidenciaCard.tsx` en `features/residencias/components`.
- [x] Tipo `Persona.ts` en `features/personas/types` y tipo `Residencia.ts` en `features/residencias/types`.
- [x] `.env.example` con `VITE_API_BASE_URL`, sin credenciales.
- [x] `npm run build` sin errores.

---

## Guía 02 — Navegación, layout principal y menú

Se instaló React Router con `npm install react-router` (API declarativa del paquete `react-router`, no `react-router-dom`).

| Archivo | Responsabilidad |
|---|---|
| `src/main.tsx` | Monta React y envuelve `App` con `BrowserRouter`. Importa `styles/global.css`. |
| `src/app/App.tsx` | Sólo delega a `AppRouter`. |
| `src/routes/AppRouter.tsx` | Declara qué página corresponde a cada URL. |
| `src/layouts/MainLayout.tsx` | Estructura permanente: `Sidebar`, `Header` y `Outlet`. |
| `src/components/common/Sidebar.tsx` | Menú lateral con `NavLink` y opción activa. |
| `src/components/common/Header.tsx` | Encabezado común. |
| `src/pages/` | Páginas globales: `DashboardPage`, `AcercaPage`, `NotFoundPage`. |
| `src/features/*/pages/` | `PersonasPage` y `ResidenciasPage`, dentro de su feature. |
| `src/styles/global.css` | Estilos del layout y comportamiento responsive (reemplaza a `src/index.css`). |

| URL | Página |
|---|---|
| `/` (ruta `index`) | `DashboardPage` |
| `/personas` | `PersonasPage` |
| `/residencias` | `ResidenciasPage` |
| `/acerca` | `AcercaPage` |
| cualquier otra (`*`) | `NotFoundPage`, dentro del mismo layout |

### Pruebas de la sección 15

| Prueba | Resultado |
|---|---|
| `/` | `DashboardPage` con "Inicio" activo ✅ |
| `/personas` | `PersonasPage` con "Personas" activo ✅ |
| `/residencias` | `ResidenciasPage` con "Residencias" activo ✅ |
| `/ruta-inexistente` | `NotFoundPage` dentro del layout ✅ |
| Atrás / Adelante | El historial cambia de ruta sin perder el layout ni recargar el documento ✅ |
| Ancho de celular | El menú pasa arriba en una fila horizontal con desplazamiento ✅ |

### Práctica obligatoria

1. [x] `AcercaPage.tsx` creada en `src/pages`.
2. [x] Ruta `/acerca` agregada dentro del layout principal.
3. [x] "Acerca" agregado al menú con `NavLink`.
4. [x] La opción activa se distingue por el fondo blanco y una marca lateral de color (`.sidebar__link--active`), sin cambiar el comportamiento de las demás.
5. [x] **Cambio de `path="personas"` por `path="personas2"`**: al hacer clic en "Personas" la URL pasa a `/personas`, el menú la marca como activa (porque `NavLink` compara la URL con su `to`), pero el contenido muestra la página 404, porque ninguna `Route` coincide y la captura `*`. Escribiendo `/personas2` a mano sí aparece `PersonasPage`. Se restauró el código.
6. [x] **Sin `Outlet` en `MainLayout`**: el menú y el encabezado siguen visibles en todas las rutas, pero el área de contenido queda vacía, porque `Outlet` es el lugar donde se dibuja la ruta hija. Se restauró el código.
7. [x] Flujo desde un clic en "Personas" hasta `PersonasPage` (máximo 8 líneas):

> 1. El usuario hace clic en "Personas" del `Sidebar`.
> 2. `NavLink` cambia la URL a `/personas` sin pedir un HTML nuevo al servidor.
> 3. `BrowserRouter` detecta la nueva ubicación.
> 4. `Routes` busca la `Route` que coincide: `personas`, anidada en la ruta del layout.
> 5. `MainLayout` se mantiene: `Sidebar` y `Header` no se vuelven a montar.
> 6. `Outlet` dibuja `PersonasPage` en el área de contenido.
> 7. `NavLink` recibe `isActive = true` y aplica la clase `sidebar__link--active`.

### Qué no se hizo todavía

Según la guía, todavía no se llamó al backend ni se agregaron formularios: primero se aisló el problema de navegación y layout.
