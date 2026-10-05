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
3. **`services/http/`**: aquí irá el código común para comunicarse con el backend de Spring Boot. Así la forma de llamar a la API se define en un solo lugar y no en cada pantalla. *(Desde la Guía 05 ese código vive en `src/api/apiClient.ts`, la carpeta que indica esa guía.)*
4. **`config/`**: sirve para leer la configuración general, por ejemplo la URL del backend que se define en `VITE_API_BASE_URL` *(desde la Guía 05, `VITE_API_URL`)*. Ahí nunca van contraseñas, porque todo el código del frontend termina en el navegador.
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

---

## Guía 05 — Conexión React ↔ Spring Boot

Los datos simulados se reemplazaron por peticiones HTTP reales a la API. Los archivos `*.mock.ts` se eliminaron porque dejaron de ser el origen de los datos (quedan en el historial de Git, commit de la Guía 03).

### Contrato confirmado en Swagger

| Dato | CondoFlow |
|---|---|
| Puerto del backend | `http://localhost:8080` |
| Personas | `GET /api/personas` (200 + `Persona[]`), `POST /api/personas` (201 + persona creada) |
| Residencias | `GET /api/residencias` (200 + `Residencia[]`), `POST /api/residencias` (201) |
| Unidades | `GET /api/unidades` (200 + `Unidad[]`) para el selector |
| JSON de `POST /api/personas` | `nombre`, `apellido`, `documento`, `telefono`, `correoElectronico` (sin id ni estado) |
| JSON de `POST /api/residencias` | `personaId`, `unidadId`, `tipoResidencia`, `fechaInicio`: la relación viaja como **`personaId`**, no como objeto `persona: { id }` |
| Errores | Formato `ApiError` del backend: `status`, `message`, `path`, `fieldErrors` |

### Qué se agregó

| Archivo | Responsabilidad |
|---|---|
| `.env.development` | `VITE_API_URL=http://localhost:8080/api`. Es una URL pública, no un secreto, por eso se versiona (excepción en `.gitignore`). |
| `.env.example` | Plantilla con la misma variable. |
| `src/vite-env.d.ts` | Tipa `import.meta.env.VITE_API_URL`. |
| `src/api/apiClient.ts` | **Una sola puerta HTTP**: URL base, `Content-Type: application/json`, `response.ok`, 204 y errores. La guía ubica el cliente HTTP en `src/api/`, por eso se quitó la carpeta vacía `services/http/` de la Guía 01. |
| `features/*/services/*Service.ts` | `personaService`, `residenciaService` y `unidadService`: conocen los endpoints; los componentes sólo llaman `listar()` o `crear()`. |
| `features/*/types/*CreateRequest.ts` | Cuerpo exacto de cada POST. `PersonaCreateRequest` omite `personaId` **y** `estado`, porque el DTO del backend no los recibe. |
| Backend `shared/web/WebConfig.java` | CORS para el origen `http://localhost:5173` (decisión D-15). |
| Backend `GET /api/residencias` | Listado completo de residencias para la pantalla. |

**Mejora sobre la guía en `apiClient`.** Además de lanzar `ApiError` con el `status`, se lee el JSON de error del backend para mostrar su `message` (por ejemplo, *"Ya existe una persona registrada con el correo …"*) y sus `fieldErrors`, que el formulario pinta junto a cada campo. Si `fetch` no obtiene respuesta (backend apagado o CORS), el mensaje lo dice explícitamente.

### Estados de la interfaz

| Estado | Qué ve el usuario |
|---|---|
| `loading` (GET) | "Cargando personas..." / "Cargando residencias..." en lugar de una tabla vacía. |
| `submitting` (POST) | El botón dice "Guardando..." y queda deshabilitado: evita un doble POST. |
| error HTTP | Mensaje visible en rojo con el `message` del backend. |
| éxito | "Persona creada correctamente." y la fila nueva aparece con el **id que devolvió el backend**. |

En `ResidenciasPage` las residencias, las personas y las unidades se piden **una sola vez y en paralelo** con `Promise.all`; la tabla resuelve el nombre de la persona y el número de unidad en memoria, sin una petición por fila.

### Evidencias (DevTools → Network)

| Acción | Petición | Resultado |
|---|---|---|
| Abrir `/personas` | `GET /api/personas` | 200 con las personas de PostgreSQL |
| Crear persona | `OPTIONS` (preflight) + `POST /api/personas` | 200 + **201**; la fila aparece con su `personaId` |
| Crear con correo de otra persona | `POST /api/personas` | **409** "Ya existe una persona registrada con el correo maria.lopez@condoflow.com" |
| Crear residencia | `POST /api/residencias` con `personaId` numérico | **201** |
| Repetir la misma residencia vigente | `POST /api/residencias` | **409** "La persona 3 ya tiene una residencia vigente en la unidad 1" |

En desarrollo aparecen GET cancelados (`ERR_ABORTED`) seguidos de un GET 200: `StrictMode` monta el componente dos veces y el `AbortController` del `useEffect` cancela la primera petición.

### Tres fallos intencionales y su diagnóstico

| Fallo provocado | Qué se observa | Diagnóstico |
|---|---|---|
| URL incorrecta (`/api/personaz`) | **404** `"La ruta solicitada no existe"` | Comparar el endpoint del service con Swagger. |
| Origen no permitido (`Origin: http://localhost:3000`) | **403** `Invalid CORS request` (en el navegador: error CORS en la consola y la respuesta bloqueada) | Revisar `allowedOrigins` en `WebConfig`. |
| Body inválido (`tipoResidencia: "ALQUILER"`) | **400** `"El cuerpo de la solicitud no es un JSON válido o contiene un valor no permitido"` | Comparar el Request Payload con `CrearResidenciaRequest`. |

### Práctica evaluada

- [x] `VITE_API_URL` creada y leída desde React.
- [x] CORS sólo para el origen de desarrollo `http://localhost:5173`.
- [x] `apiClient` reutilizable con `response.ok` y errores HTTP.
- [x] Services para personas y residencias (y unidades para el selector).
- [x] Mocks reemplazados por GET real en ambos listados.
- [x] POST de la entidad padre (persona) y de la entidad hija (residencia).
- [x] La relación se envía como `personaId`, igual que en el contrato de Swagger.
- [x] Estados `loading`, `error` y `submitting` visibles.
- [x] Tres fallos intencionales diagnosticados (tabla anterior).
- [x] Evidencias de Network: GET y POST exitosos.

---

## Guía 06 — CRUD completo de Persona

### Contrato REST (Swagger)

| Operación | Método y ruta | Respuesta |
|---|---|---|
| Listar | `GET /api/personas` | 200 + `Persona[]` |
| Buscar | `GET /api/personas/{id}` | 200 + `Persona` / 404 |
| Crear | `POST /api/personas` | **201** + persona creada / 400 / 409 |
| Actualizar | `PUT /api/personas/{id}` | 200 + persona actualizada / 400 / 404 / 409 |
| Eliminar | `DELETE /api/personas/{id}` | **204** sin cuerpo / 404 / **409** si tiene residencias |

El PUT y el DELETE se agregaron al backend para esta guía (decisión D-16), con pruebas de controller y de servicio.

### CRUD sobre HTTP

| CRUD | HTTP | Idempotente |
|---|---|---|
| Read | GET | Sí: leer no cambia nada. |
| Create | POST | No necesariamente: repetirlo crea otra persona (o da 409 por el correo). |
| Update | PUT | Sí: repetir el mismo PUT deja a la persona igual. |
| Delete | DELETE | Sí respecto del estado final: la persona queda eliminada. |

### Qué cambió en el frontend

| Archivo | Cambio |
|---|---|
| `types/PersonaRequests.ts` | `PersonaCreateRequest` (sin id ni estado) y `PersonaUpdateRequest` (con estado). Se nombran por intención y, en CondoFlow, además tienen formas distintas, igual que los DTO del backend. |
| `services/personaService.ts` | `listar`, `obtenerPorId`, `crear`, `actualizar` y `eliminar`. |
| `components/PersonaTable.tsx` | Columna **Acciones** con Editar y Eliminar. Avisa con los callbacks `onEdit` y `onDelete`; **no importa `personaService`**. |
| `components/PersonaForm.tsx` | Un solo formulario para crear y editar. Si recibe `persona`, está en modo edición: título "Editar persona #id", casilla **Persona activa**, botón "Actualizar persona" y "Cancelar edición". |
| `pages/PersonasPage.tsx` | `handleEdit` (GET/{id}), `handleSaved` (POST/PUT) y `handleDelete` (DELETE con confirmación). |

- **GET/{id} antes de editar**: se pide una copia actual de la persona; si ya no existe, el backend responde 404 y no se abre el formulario con datos viejos.
- **`useEffect` en el formulario**: sincroniza los campos cuando cambia la prop `persona`. No hace ninguna petición: el PUT sólo sale al presionar "Actualizar persona".
- **Sincronización** con la respuesta del backend: el POST agrega la fila (`[...prev, saved]`), el PUT reemplaza sólo la fila editada (`map`) y el DELETE la quita (`filter`) **recién cuando el backend respondió 204**.
- **Baja lógica**: desmarcar "Persona activa" y actualizar envía `estado: "INACTIVO"`.

### Relación 1:N al eliminar

Si la persona tiene residencias, PostgreSQL rechaza el DELETE por la clave foránea `fk_residencia_persona`; el backend responde **409** con el mensaje *"No se puede eliminar la persona 3 porque tiene residencias o reservas registradas. Puede cambiar su estado a INACTIVO."*. React muestra el error y **no quita la fila**.

### Flujos

| Acción | Flujo |
|---|---|
| Listar | Página → `personaService.listar` → GET → Controller → Service → Repository → SELECT → JSON → `setPersonas` |
| Editar | Clic en Editar → GET/{id} → `editingPersona` → el formulario carga los datos → PUT → UPDATE → respuesta → `map()` reemplaza la fila |
| Crear | Submit → validación → POST → INSERT → persona con id → la lista agrega la fila |
| Eliminar | Clic → `confirm` → DELETE/{id} → FK → 204 → `filter()` quita la fila; 409 → se muestra el error y la fila queda |

### Evidencias (DevTools → Network)

| Acción | Petición | Status |
|---|---|---|
| Editar la persona 3 | `GET /api/personas/3` | 200 |
| Actualizar apellido y desmarcar "activa" | `PUT /api/personas/3` | 200: la fila muestra "Guia Seis" e "Inactivo" |
| Eliminar la persona 3 (tiene una residencia) | `DELETE /api/personas/3` | **409**: la fila se conserva |
| Crear y eliminar una persona sin residencias | `POST` + `DELETE /api/personas/4` | 201 + **204**: la fila desaparece |
| Editar y luego "Cancelar edición" | sólo `GET /api/personas/1` | El formulario vuelve a "Nueva persona" sin enviar nada |

### Práctica evaluada

- [x] `listar`, `obtenerPorId`, `crear`, `actualizar` y `eliminar` en el service de la entidad padre.
- [x] Botones Editar y Eliminar en la tabla mediante callbacks.
- [x] Un mismo formulario para crear y editar.
- [x] GET/{id} antes de editar.
- [x] Lista sincronizada después de POST, PUT y DELETE.
- [x] Cancelar edición.
- [x] Acciones bloqueadas mientras se guarda o elimina ("Guardando...", "Eliminando...").
- [x] 404 real: `PUT` y `DELETE` sobre `/api/personas/999999` (colección de Postman).
- [x] Eliminar la entidad padre con entidades hijas: **409** y la fila se conserva.
- [x] Evidencia de Network para GET, POST, PUT y DELETE (tabla anterior).
