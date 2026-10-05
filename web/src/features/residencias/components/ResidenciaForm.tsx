import { useEffect, useState, type ChangeEvent, type FormEvent } from 'react';
import { ApiError } from '../../../api/apiClient';
import type { Persona } from '../../personas/models/Persona';
import type { Unidad } from '../../unidades/models/Unidad';
import type { Residencia, TipoResidencia } from '../models/Residencia';
import { residenciaService } from '../services/residenciaService';
import type { ResidenciaFormData } from '../types/ResidenciaFormData';
import { validarResidencia, type ResidenciaFormErrors } from '../utils/residenciaValidation';

interface ResidenciaFormProps {
  personas: Persona[];
  unidades: Unidad[];
  residencia?: Residencia | null;
  onSaved: (residencia: Residencia, mode: 'create' | 'edit') => void;
  onCancelEdit?: () => void;
}

const initialResidenciaForm: ResidenciaFormData = {
  personaId: '',
  unidadId: '',
  tipoResidencia: '',
  fechaInicio: '',
  finalizada: false,
  fechaFin: '',
};

// Un solo formulario para crear y editar residencias (Guía 07). Al editar se puede cambiar la persona
// (reasignar la residencia) y finalizarla con una fecha de fin.
export default function ResidenciaForm({ personas, unidades, residencia, onSaved, onCancelEdit }: ResidenciaFormProps) {
  const [formData, setFormData] = useState<ResidenciaFormData>(initialResidenciaForm);
  const [errors, setErrors] = useState<ResidenciaFormErrors>({});
  const [mensaje, setMensaje] = useState('');
  const [saving, setSaving] = useState(false);
  const [apiError, setApiError] = useState('');

  const isEditing = residencia != null;
  const cantidadErrores = Object.keys(errors).length;

  // Precarga el formulario cuando cambia la residencia a editar. Los ids pasan a string porque
  // así los maneja un <select> controlado. No hace ninguna petición HTTP.
  /* oxlint-disable react/set-state-in-effect */
  useEffect(() => {
    if (residencia) {
      setFormData({
        personaId: String(residencia.personaId),
        unidadId: String(residencia.unidadId),
        tipoResidencia: residencia.tipoResidencia,
        fechaInicio: residencia.fechaInicio,
        finalizada: residencia.estado === 'FINALIZADA',
        fechaFin: residencia.fechaFin ?? '',
      });
    } else {
      setFormData(initialResidenciaForm);
    }
    setErrors({});
    setMensaje('');
    setApiError('');
  }, [residencia]);
  /* oxlint-enable react/set-state-in-effect */

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    if (e.target instanceof HTMLInputElement && e.target.type === 'checkbox') {
      const { checked } = e.target;
      setFormData((prev) => ({ ...prev, [name]: checked }));
      return;
    }
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMensaje('');
    setApiError('');

    const validationErrors = validarResidencia(formData, personas, unidades, residencia);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) return;

    // Recién después de validar se convierten los textos a los tipos del dominio.
    // El usuario eligió a la persona por su nombre, pero el request envía sólo la referencia personaId.
    const datos = {
      personaId: Number(formData.personaId),
      unidadId: Number(formData.unidadId),
      tipoResidencia: formData.tipoResidencia as TipoResidencia,
      fechaInicio: formData.fechaInicio,
    };

    try {
      setSaving(true);
      if (residencia) {
        const actualizada = await residenciaService.actualizar(residencia.residenciaId, {
          ...datos,
          fechaFin: formData.finalizada ? formData.fechaFin : null,
          estado: formData.finalizada ? 'FINALIZADA' : 'VIGENTE',
        });
        onSaved(actualizada, 'edit');
      } else {
        const creada = await residenciaService.crear(datos);
        onSaved(creada, 'create');
        setFormData(initialResidenciaForm);
        setMensaje('Residencia registrada correctamente.');
      }
    } catch (err) {
      if (err instanceof ApiError) setErrors(err.fieldErrors as ResidenciaFormErrors);
      setApiError(err instanceof Error ? err.message : 'No se pudo guardar la residencia');
    } finally {
      setSaving(false);
    }
  };

  const limpiar = () => {
    setFormData(initialResidenciaForm);
    setErrors({});
    setMensaje('');
    setApiError('');
  };

  // Sólo personas y unidades activas; al editar también la actual, aunque ya esté inactiva.
  const personasDisponibles = personas.filter(
    (persona) => persona.estado === 'ACTIVO' || persona.personaId === residencia?.personaId,
  );
  const unidadesDisponibles = unidades.filter(
    (unidad) => unidad.estado === 'ACTIVA' || unidad.unidadId === residencia?.unidadId,
  );

  return (
    <form className="entity-form" onSubmit={handleSubmit} noValidate>
      <h2 className="form-title">
        {isEditing ? `Editar residencia #${residencia.residenciaId}` : 'Nueva residencia'}
      </h2>

      <div className="form-grid">
        <label>
          Persona
          {/* Select controlado: value sale del estado y onChange lo actualiza; no se usa selected en option. */}
          <select name="personaId" value={formData.personaId} onChange={handleChange}>
            <option value="">Seleccione una persona</option>
            {personasDisponibles.map((persona) => (
              <option key={persona.personaId} value={String(persona.personaId)}>
                {persona.nombre} {persona.apellido} - {persona.documento}
                {persona.estado === 'INACTIVO' ? ' (inactiva)' : ''}
              </option>
            ))}
          </select>
          {errors.personaId && <small className="field-error">{errors.personaId}</small>}
        </label>

        <label>
          Unidad
          <select name="unidadId" value={formData.unidadId} onChange={handleChange}>
            <option value="">Seleccione una unidad</option>
            {unidadesDisponibles.map((unidad) => (
              <option key={unidad.unidadId} value={String(unidad.unidadId)}>
                {unidad.numeroUnidad} ({unidad.tipo})
              </option>
            ))}
          </select>
          {errors.unidadId && <small className="field-error">{errors.unidadId}</small>}
        </label>

        <label>
          Tipo de residencia
          <select name="tipoResidencia" value={formData.tipoResidencia} onChange={handleChange}>
            <option value="">Seleccione el tipo</option>
            <option value="PROPIETARIO">Propietario</option>
            <option value="INQUILINO">Inquilino</option>
          </select>
          {errors.tipoResidencia && <small className="field-error">{errors.tipoResidencia}</small>}
        </label>

        <label>
          Fecha de inicio
          <input type="date" name="fechaInicio" value={formData.fechaInicio} onChange={handleChange} />
          {errors.fechaInicio && <small className="field-error">{errors.fechaInicio}</small>}
        </label>

        {/* RN-01: al editar se puede cerrar la vigencia; una residencia nueva siempre nace VIGENTE. */}
        {isEditing && (
          <label className="checkbox-field form-span-2">
            <input type="checkbox" name="finalizada" checked={formData.finalizada} onChange={handleChange} />
            Residencia finalizada
          </label>
        )}

        {isEditing && formData.finalizada && (
          <label>
            Fecha de fin
            <input type="date" name="fechaFin" value={formData.fechaFin} onChange={handleChange} />
            {errors.fechaFin && <small className="field-error">{errors.fechaFin}</small>}
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
          {saving ? 'Guardando...' : isEditing ? 'Actualizar residencia' : 'Crear residencia'}
        </button>
      </div>
    </form>
  );
}
