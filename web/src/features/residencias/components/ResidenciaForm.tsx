import { useState, type ChangeEvent, type FormEvent } from 'react';
import { personasMock } from '../../personas/data/personas.mock';
import { unidadesMock } from '../../unidades/data/unidades.mock';
import type { TipoResidencia } from '../models/Residencia';
import type { ResidenciaFormData } from '../types/ResidenciaFormData';
import { validarResidencia, type ResidenciaFormErrors } from '../utils/residenciaValidation';

const initialResidenciaForm: ResidenciaFormData = {
  personaId: '',
  unidadId: '',
  tipoResidencia: '',
  fechaInicio: '',
};

export default function ResidenciaForm() {
  const [formData, setFormData] = useState<ResidenciaFormData>(initialResidenciaForm);
  const [errors, setErrors] = useState<ResidenciaFormErrors>({});
  const [mensaje, setMensaje] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const cantidadErrores = Object.keys(errors).length;

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMensaje('');

    const validationErrors = validarResidencia(formData);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) return;

    // Recién después de validar se convierten los textos a los tipos del dominio.
    const residenciaPayload = {
      personaId: Number(formData.personaId),
      unidadId: Number(formData.unidadId),
      tipoResidencia: formData.tipoResidencia as TipoResidencia,
      fechaInicio: formData.fechaInicio,
    };

    setSubmitting(true);
    await new Promise((resolve) => setTimeout(resolve, 500));
    setSubmitting(false);

    console.log('Residencia lista para API:', residenciaPayload);
    setMensaje('Datos válidos. Residencia lista para enviarse al backend.');
  };

  const limpiar = () => {
    setFormData(initialResidenciaForm);
    setErrors({});
    setMensaje('');
  };

  return (
    <form className="entity-form" onSubmit={handleSubmit} noValidate>
      <div className="form-grid">
        <label>
          Persona
          {/* El usuario ve el nombre, pero el estado guarda personaId: así se representa la relación 1:N. */}
          <select name="personaId" value={formData.personaId} onChange={handleChange}>
            <option value="">Seleccione una persona</option>
            {personasMock
              .filter((persona) => persona.estado === 'ACTIVO')
              .map((persona) => (
                <option key={persona.personaId} value={persona.personaId}>
                  {persona.nombre} {persona.apellido}
                </option>
              ))}
          </select>
          {errors.personaId && <small className="field-error">{errors.personaId}</small>}
        </label>

        <label>
          Unidad
          <select name="unidadId" value={formData.unidadId} onChange={handleChange}>
            <option value="">Seleccione una unidad</option>
            {unidadesMock
              .filter((unidad) => unidad.estado === 'ACTIVA')
              .map((unidad) => (
                <option key={unidad.unidadId} value={unidad.unidadId}>
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
      </div>

      {cantidadErrores > 0 && (
        <div className="form-error-count">El formulario tiene {cantidadErrores} error(es).</div>
      )}
      {mensaje && <div className="form-success">{mensaje}</div>}

      <div className="form-actions">
        <button type="button" className="btn-secondary" onClick={limpiar} disabled={submitting}>
          Limpiar
        </button>
        <button type="submit" className="btn-primary" disabled={submitting}>
          {submitting ? 'Guardando...' : 'Guardar residencia'}
        </button>
      </div>
    </form>
  );
}
