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

---

## Guía 03 — Modelos TypeScript, datos simulados y tablas

Todavía no se llama al backend: primero se domina el modelo de datos, las props y el renderizado de listas.

| Archivo | Responsabilidad |
|---|---|
| `features/personas/models/Persona.ts` | Interface `Persona` (y el tipo `EstadoPersona`). Se movió desde `types/` porque la guía separa *modelos* de *tipos de formulario*. |
| `features/residencias/models/Residencia.ts` | Interface `Residencia` con `personaId`, que representa la relación 1:N. |
| `features/*/data/*.mock.ts` | Datos simulados tipados como `Persona[]` y `Residencia[]`. |
| `features/personas/components/PersonaTable.tsx` | Recibe `personas` por props y dibuja una fila por persona con `map()`. |
| `features/residencias/components/ResidenciaTable.tsx` | Recibe `residencias` y `personas`; con `find()` convierte `personaId` en un nombre. |
| `features/*/pages/*Page.tsx` | Eligen los datos, calculan las tarjetas con `filter()` y se los pasan a la tabla. |
| `utils/formatDate.ts` | Muestra las fechas ISO de la API como `dd/mm/aaaa`. |

**Alineación con el backend.** Los campos no son los didácticos de la guía (`id`, `email`, `activo`), sino los del contrato real de la API (`personaId`, `correoElectronico`, `estado`), como pide la guía: *"si tu backend usa otros nombres exactos, alinea los modelos con el contrato real"*. El estado es una unión de textos (`'ACTIVO' | 'INACTIVO'`), no un `boolean`, porque así lo guarda PostgreSQL (CHECK `ck_persona_estado`).

### Flujo datos → props → map → JSX → DOM

```
personas.mock.ts      exporta Persona[]
      ↓
PersonasPage.tsx      pasa personas={personasMock}
      ↓
PersonaTable.tsx      recibe Persona[] por props
      ↓
personas.map(...)     transforma cada objeto en un <tr> con key={persona.personaId}
      ↓
React actualiza el DOM y el navegador muestra la tabla
```

En residencias el flujo es igual, pero `ResidenciaTable` también recibe `personasMock` para convertir `personaId` en el nombre de la persona.

### Práctica obligatoria

- [x] Dos personas nuevas en el mock: Ana Mendez (activa) y Jorge Paz (inactiva).
- [x] Tres residencias nuevas asociadas a personas existentes (ids 5, 6 y 7).
- [x] Una residencia con `personaId: 999`: la tabla muestra **"Sin persona asociada"**.
- [x] Error de tipo provocado a propósito: con `unidadId: '1'` el compilador respondió `error TS2322: Type 'string' is not assignable to type 'number'.` porque el modelo exige `number`. Se corrigió.
- [x] Con `personasMock = []` la página muestra 0 en las tarjetas y el mensaje **"No hay personas registradas."** (retorno temprano). Se restauraron los datos.
- [x] Al cambiar una fecha de una residencia sólo cambia esa fila: React identifica cada fila por su `key` estable (`residenciaId`).
- [x] `npm run build` sin errores.

---

## Guía 04 — Formularios profesionales con React + TypeScript

Formularios **controlados** con `useState`: el valor de cada campo sale del estado (`value={formData.nombre}`) y sólo cambia con `onChange`. Todavía no se llama a Spring Boot: "Guardar" valida y arma el *payload* que después se enviará a la API.

| Archivo | Responsabilidad |
|---|---|
| `features/personas/types/PersonaFormData.ts` | Lo que el usuario escribe en el formulario de persona. |
| `features/residencias/types/ResidenciaFormData.ts` | `personaId` y `unidadId` como `string`, porque así los entrega un `<select>`. |
| `features/*/utils/*Validation.ts` | Reglas de validación; devuelven un objeto con un mensaje por campo. |
| `features/personas/components/PersonaForm.tsx` | Formulario de persona. |
| `features/residencias/components/ResidenciaForm.tsx` | Formulario de residencia con selectores de persona, unidad y tipo. |
| `features/unidades/` | Modelo `Unidad` y catálogo simulado para el selector de unidad. |
| `styles/forms.css` | Estilos de formularios y botones, responsive. |

**Modelo vs. tipo de formulario.** `Residencia` describe datos ya válidos (`personaId: number`); `ResidenciaFormData` describe lo que se está escribiendo (`personaId: string`, que puede estar vacío). Se valida primero y recién después se convierte con `Number(...)`.

**Alineación con el backend.** Las reglas son las mismas de `CrearPersonaRequest` y `CrearResidenciaRequest`: teléfono obligatorio con 7 a 30 dígitos, documento de 5 a 50 caracteres, correo con formato válido. El formulario de persona **no tiene casilla "Activo"**: en CondoFlow toda persona nueva se registra `ACTIVO` y la API de alta no recibe el estado. El estado se cambia al editar (Guía 06).

### Flujo para la defensa

| Paso | Qué ocurre |
|---|---|
| 1 | El usuario escribe "Ana": el input dispara `onChange`. |
| 2 | `handleChange` lee `name` y `value` del evento. |
| 3 | `setFormData(prev => ({ ...prev, [name]: value }))` crea un objeto nuevo sin mutar el anterior. |
| 4 | React vuelve a renderizar y el input muestra el valor del estado. |
| 5 | Al presionar Guardar, el formulario dispara `onSubmit`. |
| 6 | `e.preventDefault()` evita que el navegador recargue la página. |
| 7 | `validarPersona` / `validarResidencia` llenan `errors` si hay problemas. |
| 8 | Si no hay errores, se normalizan los textos y se convierten los ids a `number`. |
| 9 | Se arma el payload, listo para la petición HTTP de la Guía 05. |

### Pruebas manuales (sección 12)

| Prueba | Resultado |
|---|---|
| Guardar persona con todo vacío | 5 mensajes junto a cada campo y "El formulario tiene 5 error(es)", sin recargar ✅ |
| Documento `abc`, teléfono `123`, correo `no-es-correo` | Mensaje en cada uno de esos tres campos ✅ |
| Datos corregidos | Mensaje de éxito y en la consola `{"documento":"9988776-1A", ..., "correoElectronico":"ana.suarez@condoflow.com"}` ✅ |
| Guardar residencia sin elegir nada | 4 errores: persona, unidad, tipo y fecha ✅ |
| Fecha de inicio `1890-05-01` | "Ingrese una fecha de inicio válida." ✅ |
| Residencia válida | Payload con `personaId: 2` y `unidadId: 1` convertidos a `number` ✅ |
| Ancho angosto | El formulario pasa a una columna ✅ |

### Práctica evaluada

- [x] **Campo Dirección**: no se agregó, porque Persona no tiene dirección en nuestro modelo ni en la API: en CondoFlow "dónde vive" una persona es su **residencia** en una unidad. Agregar un campo que el backend no recibe sería inventar un contrato. En su lugar, el **teléfono es obligatorio**, igual que en el backend.
- [x] El documento acepta al menos 5 caracteres (entre 5 y 50 letras, números o guiones).
- [x] Equivalente a "placa en mayúsculas": el **documento se convierte a mayúsculas** en el payload (por ejemplo, un complemento `1a` → `1A`) sin obligar al usuario a escribirlo así. El correo se pasa a minúsculas.
- [x] En el selector de residencias sólo aparecen las personas `ACTIVO` (y las unidades `ACTIVA`).
- [x] Botón **Limpiar** también en el formulario de residencia.
- [x] Reto avanzado: envío simulado de 500 ms; mientras dura, el botón dice "Guardando..." y queda deshabilitado.
- [x] Contador de errores al intentar guardar ("El formulario tiene N error(es).").
- [x] **Por qué la validación del frontend no reemplaza a la del backend**: el frontend corre en el navegador del usuario, que puede modificar el código o saltarlo mandando la petición directo con Postman o curl. Además, hay reglas que el navegador no puede saber, como si el correo ya está registrado o si la persona ya tiene una residencia vigente en esa unidad. Por eso la validación del frontend sólo da respuesta rápida al usuario; la que protege los datos es la de Spring Boot (`@Valid` y reglas del servicio) y la de PostgreSQL (NOT NULL, UNIQUE, CHECK, FK).
