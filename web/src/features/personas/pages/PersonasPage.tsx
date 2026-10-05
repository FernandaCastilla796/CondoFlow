import { useState } from 'react';
import PersonaForm from '../components/PersonaForm';
import PersonaTable from '../components/PersonaTable';
import { personasMock } from '../data/personas.mock';

// La página conoce el origen de los datos y se los entrega a la tabla mediante la prop personas.
export default function PersonasPage() {
  const [mostrarFormulario, setMostrarFormulario] = useState(false);

  const total = personasMock.length;
  const activas = personasMock.filter((persona) => persona.estado === 'ACTIVO').length;

  return (
    <section className="feature-page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">GESTIÓN DE PERSONAS</p>
          <h1>Personas</h1>
          <p>Listado local preparado para la futura integración con la API REST.</p>
        </div>
        <button
          type="button"
          className="btn-primary"
          onClick={() => setMostrarFormulario((prev) => !prev)}
        >
          {mostrarFormulario ? 'Cerrar formulario' : '+ Nueva persona'}
        </button>
      </div>

      <div className="stats-grid">
        <article className="stat-card"><span>Total</span><strong>{total}</strong></article>
        <article className="stat-card"><span>Activas</span><strong>{activas}</strong></article>
        <article className="stat-card"><span>Inactivas</span><strong>{total - activas}</strong></article>
      </div>

      {mostrarFormulario && <PersonaForm />}
      <PersonaTable personas={personasMock} />
    </section>
  );
}
