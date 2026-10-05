import { useState, type ChangeEvent, type FormEvent } from 'react';
import type { PersonaFormData } from '../types/PersonaFormData';
import { validarPersona, type PersonaFormErrors } from '../utils/personaValidation';

const initialPersonaForm: PersonaFormData = {
  nombre: '',
  apellido: '',
  documento: '',
  telefono: '',
  correoElectronico: '',
};

// Formulario controlado: cada input lee su valor de formData y lo actualiza con onChange.
export default function PersonaForm() {
  const [formData, setFormData] = useState<PersonaFormData>(initialPersonaForm);
  const [errors, setErrors] = useState<PersonaFormErrors>({});
  const [mensaje, setMensaje] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const cantidadErrores = Object.keys(errors).length;

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    // No se muta formData: se crea un objeto nuevo copiando el anterior.
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMensaje('');

    const validationErrors = validarPersona(formData);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) return;

    const personaPayload = {
      nombre: formData.nombre.trim(),
      apellido: formData.apellido.trim(),
      documento: formData.documento.trim().toUpperCase(),
      telefono: formData.telefono.trim(),
      correoElectronico: formData.correoElectronico.trim().toLowerCase(),
    };

    // Envío simulado de 500 ms (reto de la práctica): el botón queda deshabilitado mientras dura.
    setSubmitting(true);
    await new Promise((resolve) => setTimeout(resolve, 500));
    setSubmitting(false);

    console.log('Persona lista para API:', personaPayload);
    setMensaje('Datos válidos. Persona lista para enviarse al backend.');
  };

  const limpiar = () => {
    setFormData(initialPersonaForm);
    setErrors({});
    setMensaje('');
  };

  return (
    <form className="entity-form" onSubmit={handleSubmit} noValidate>
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
          {submitting ? 'Guardando...' : 'Guardar persona'}
        </button>
      </div>
    </form>
  );
}
