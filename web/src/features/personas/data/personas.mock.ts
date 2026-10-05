import type { Persona } from '../models/Persona';

// Datos simulados temporales: permiten construir la interfaz antes de consumir la API.
export const personasMock: Persona[] = [
  {
    personaId: 1,
    nombre: 'Maria',
    apellido: 'Lopez',
    documento: '4512378',
    telefono: '+591 70000001',
    correoElectronico: 'maria.lopez@condoflow.com',
    estado: 'ACTIVO',
  },
  {
    personaId: 2,
    nombre: 'Carlos',
    apellido: 'Rojas',
    documento: '6231457',
    telefono: '+591 70000002',
    correoElectronico: 'carlos.rojas@condoflow.com',
    estado: 'ACTIVO',
  },
  {
    personaId: 3,
    nombre: 'Lucia',
    apellido: 'Vargas',
    documento: '9123476',
    telefono: '+591 70010003',
    correoElectronico: 'lucia.vargas@condoflow.com',
    estado: 'INACTIVO',
  },
  {
    personaId: 4,
    nombre: 'Diego',
    apellido: 'Suarez',
    documento: '7345892',
    telefono: '+591 70010004',
    correoElectronico: 'diego.suarez@condoflow.com',
    estado: 'ACTIVO',
  },
  // Práctica Guía 03: dos personas nuevas, una activa y una inactiva.
  {
    personaId: 5,
    nombre: 'Ana',
    apellido: 'Mendez',
    documento: '8456123',
    telefono: '+591 70010005',
    correoElectronico: 'ana.mendez@condoflow.com',
    estado: 'ACTIVO',
  },
  {
    personaId: 6,
    nombre: 'Jorge',
    apellido: 'Paz',
    documento: '5698741',
    telefono: '+591 70010006',
    correoElectronico: 'jorge.paz@condoflow.com',
    estado: 'INACTIVO',
  },
];
