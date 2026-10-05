import { useEffect, useState } from 'react';
import PersonaForm from '../components/PersonaForm';
import PersonaTable from '../components/PersonaTable';
import type { Persona } from '../models/Persona';
import { personaService } from '../services/personaService';

// Los datos ya no salen de un mock: se piden a Spring Boot con GET /api/personas (Guía 05).
export default function PersonasPage() {
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [mostrarFormulario, setMostrarFormulario] = useState(false);

  useEffect(() => {
    // AbortController cancela la petición si el componente se desmonta antes de recibir la respuesta.
    const controller = new AbortController();

    const cargarPersonas = async () => {
      try {
        setLoading(true);
        setError('');
        const data = await personaService.listar(controller.signal);
        setPersonas(data);
      } catch (err) {
        if (err instanceof DOMException && err.name === 'AbortError') return;
        setError(err instanceof Error ? err.message : 'Error inesperado');
      } finally {
        if (!controller.signal.aborted) setLoading(false);
      }
    };

    void cargarPersonas();
    return () => controller.abort();
  }, []);

  const total = personas.length;
  const activas = personas.filter((persona) => persona.estado === 'ACTIVO').length;

  return (
    <section className="feature-page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">GESTIÓN DE PERSONAS</p>
          <h1>Personas</h1>
          <p>Personas registradas en el backend de CondoFlow.</p>
        </div>
        <button
          type="button"
          className="btn-primary"
          onClick={() => setMostrarFormulario((prev) => !prev)}
        >
          {mostrarFormulario ? 'Cerrar formulario' : '+ Nueva persona'}
        </button>
      </div>

      {loading && <div className="state-card">Cargando personas...</div>}
      {error && <div className="state-card error">{error}</div>}

      {!loading && !error && (
        <>
          <div className="stats-grid">
            <article className="stat-card"><span>Total</span><strong>{total}</strong></article>
            <article className="stat-card"><span>Activas</span><strong>{activas}</strong></article>
            <article className="stat-card"><span>Inactivas</span><strong>{total - activas}</strong></article>
          </div>

          {/* La tabla incorpora el objeto que devolvió el backend (con su personaId real). */}
          {mostrarFormulario && (
            <PersonaForm onCreated={(nueva) => setPersonas((prev) => [...prev, nueva])} />
          )}
          <PersonaTable personas={personas} />
        </>
      )}
    </section>
  );
}
