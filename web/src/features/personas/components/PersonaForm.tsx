import { useEffect, useState, type ChangeEvent, type FormEvent } from 'react';
import { ApiError } from '../../../api/apiClient';
import type { Persona } from '../models/Persona';
import { personaService } from '../services/personaService';
import type { PersonaFormData } from '../types/PersonaFormData';
import { validarPersona, type PersonaFormErrors } from '../utils/personaValidation';

interface PersonaFormProps {
  persona?: Persona | null;
  onSaved: (persona: Persona, mode: 'create' | 'edit') => void;
  onCancelEdit?: () => void;
}

const initialPersonaForm: PersonaFormData = {
  nombre: '',
  apellido: '',
  documento: '',
  telefono: '',
  correoElectronico: '',
  activo: true,
};

// Un solo formulario para crear y editar (Guía 06). Si recibe una persona, está en modo edición.
export default function PersonaForm({ persona, onSaved, onCancelEdit }: PersonaFormProps) {
  const [formData, setFormData] = useState<PersonaFormData>(initialPersonaForm);
  const [errors, setErrors] = useState<PersonaFormErrors>({});
  const [mensaje, setMensaje] = useState('');
  const [saving, setSaving] = useState(false);
  const [apiError, setApiError] = useState('');

  const isEditing = persona != null;
  const cantidadErrores = Object.keys(errors).length;

  // Sincroniza el estado editable cuando cambia la prop persona. No hace ninguna petición HTTP.
  // La Guía 06 usa este Effect a propósito; la alternativa que sugiere el linter es remontar el
  // formulario con una key distinta por persona.
  /* oxlint-disable react/set-state-in-effect */
  useEffect(() => {
    if (persona) {
      setFormData({
        nombre: persona.nombre,
        apellido: persona.apellido,
        documento: persona.documento,
        telefono: persona.telefono,
        correoElectronico: persona.correoElectronico,
        activo: persona.estado === 'ACTIVO',
      });
    } else {
      setFormData(initialPersonaForm);
    }
    setErrors({});
    setMensaje('');
    setApiError('');
  }, [persona]);
  /* oxlint-enable react/set-state-in-effect */

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    // No se muta formData: se crea un objeto nuevo copiando el anterior.
    // El checkbox usa checked (true/false); los demás inputs usan value (texto).
    setFormData((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMensaje('');
    setApiError('');

    const validationErrors = validarPersona(formData);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) return;

    const datos = {
      nombre: formData.nombre.trim(),
      apellido: formData.apellido.trim(),
      documento: formData.documento.trim().toUpperCase(),
      telefono: formData.telefono.trim(),
      correoElectronico: formData.correoElectronico.trim().toLowerCase(),
    };

    try {
      setSaving(true);
      // POST o PUT no depende del texto del botón, sino de si existe una persona persistida con id.
      if (persona) {
        const actualizada = await personaService.actualizar(persona.personaId, {
          ...datos,
          estado: formData.activo ? 'ACTIVO' : 'INACTIVO',
        });
        onSaved(actualizada, 'edit');
      } else {
        const creada = await personaService.crear(datos);
        onSaved(creada, 'create');
        setFormData(initialPersonaForm);
        setMensaje('Persona creada correctamente.');
      }
    } catch (err) {
      // 400 del backend: sus fieldErrors usan los mismos nombres que los campos del formulario.
      if (err instanceof ApiError) setErrors(err.fieldErrors as PersonaFormErrors);
      setApiError(err instanceof Error ? err.message : 'No se pudo guardar la persona');
    } finally {
      setSaving(false);
    }
  };

  const limpiar = () => {
    setFormData(initialPersonaForm);
    setErrors({});
    setMensaje('');
    setApiError('');
  };

  return (
    <form className="entity-form" onSubmit={handleSubmit} noValidate>
      <h2 className="form-title">{isEditing ? `Editar persona #${persona.personaId}` : 'Nueva persona'}</h2>

      <div className="form-grid">
        <label>
          Nombre
          <input name="nombre" value={formData.nombre} onChange={handleChange} />
          {errors.nombre && <small className="field-error">{errors.nombre}</small>}
        </label>

        <label>
          Apellido
          <input name="apellido" value={formData.apellido} onChange={handleChange} />
          {errors.apellido && <small className="field-error">{errors.apellido}</small>}
        </label>

        <label>
          Documento
          <input name="documento" value={formData.documento} onChange={handleChange} />
          {errors.documento && <small className="field-error">{errors.documento}</small>}
        </label>

        <label>
          Teléfono
          <input name="telefono" value={formData.telefono} onChange={handleChange} />
          {errors.telefono && <small className="field-error">{errors.telefono}</small>}
        </label>

        <label className="form-span-2">
          Correo electrónico
          <input type="email" name="correoElectronico" value={formData.correoElectronico} onChange={handleChange} />
          {errors.correoElectronico && <small className="field-error">{errors.correoElectronico}</small>}
        </label>

        {/* El estado sólo se cambia al editar: desmarcarlo es la baja lógica (INACTIVO). */}
        {isEditing && (
          <label className="checkbox-field form-span-2">
            <input type="checkbox" name="activo" checked={formData.activo} onChange={handleChange} />
            Persona activa
          </label>
        )}
      </div>

      {cantidadErrores > 0 && (
        <div className="form-error-count">El formulario tiene {cantidadErrores} error(es).</div>
      )}
      {apiError && <div className="form-error">{apiError}</div>}
      {mensaje && <div className="form-success">{mensaje}</div>}

      <div className="form-actions">
        {isEditing ? (
          <button type="button" className="btn-secondary" onClick={onCancelEdit} disabled={saving}>
            Cancelar edición
          </button>
        ) : (
          <button type="button" className="btn-secondary" onClick={limpiar} disabled={saving}>
            Limpiar
          </button>
        )}
        <button type="submit" className="btn-primary" disabled={saving}>
          {saving ? 'Guardando...' : isEditing ? 'Actualizar persona' : 'Crear persona'}
        </button>
      </div>
    </form>
  );
}
