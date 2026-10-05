import { useEffect, useState } from 'react';
import PersonaForm from '../components/PersonaForm';
import PersonaTable from '../components/PersonaTable';
import type { Persona } from '../models/Persona';
import { personaService } from '../services/personaService';

// CRUD completo de Persona conectado con Spring Boot (Guía 06).
export default function PersonasPage() {
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [editingPersona, setEditingPersona] = useState<Persona | null>(null);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState('');
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [deletingId, setDeletingId] = useState<number | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    // AbortController cancela la petición si el componente se desmonta antes de recibir la respuesta.
    const controller = new AbortController();

    const cargarPersonas = async () => {
      try {
        setLoading(true);
        setLoadError('');
        const data = await personaService.listar(controller.signal);
        setPersonas(data);
      } catch (err) {
        if (err instanceof DOMException && err.name === 'AbortError') return;
        setLoadError(err instanceof Error ? err.message : 'Error al listar las personas');
      } finally {
        if (!controller.signal.aborted) setLoading(false);
      }
    };

    void cargarPersonas();
    return () => controller.abort();
  }, []);

  // GET /api/personas/{id}: copia actual del recurso antes de editar (404 si ya no existe).
  const handleEdit = async (id: number) => {
    try {
      setLoadingDetail(true);
      setError('');
      const persona = await personaService.obtenerPorId(id);
      setEditingPersona(persona);
      setMostrarFormulario(true);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo cargar la persona');
    } finally {
      setLoadingDetail(false);
    }
  };

  // Sincroniza la lista con la respuesta del backend: agrega (POST) o reemplaza sólo la fila editada (PUT).
  const handleSaved = (saved: Persona, mode: 'create' | 'edit') => {
    setPersonas((prev) => {
      if (mode === 'create') {
        return [...prev, saved];
      }
      return prev.map((persona) => (persona.personaId === saved.personaId ? saved : persona));
    });
    setEditingPersona(null);
  };

  // DELETE /api/personas/{id}: la fila sólo se quita si el backend respondió 204.
  // Si la persona tiene residencias, el backend responde 409 y la fila se conserva.
  const handleDelete = async (persona: Persona) => {
    const confirmed = window.confirm(`¿Eliminar a ${persona.nombre} ${persona.apellido}?`);
    if (!confirmed) return;

    try {
      setDeletingId(persona.personaId);
      setError('');
      await personaService.eliminar(persona.personaId);
      setPersonas((prev) => prev.filter((item) => item.personaId !== persona.personaId));
      if (editingPersona?.personaId === persona.personaId) {
        setEditingPersona(null);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar la persona');
    } finally {
      setDeletingId(null);
    }
  };

  const toggleFormulario = () => {
    if (mostrarFormulario) setEditingPersona(null);
    setMostrarFormulario((prev) => !prev);
  };

  const total = personas.length;
  const activas = personas.filter((persona) => persona.estado === 'ACTIVO').length;

  return (
    <section className="feature-page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">GESTIÓN DE PERSONAS</p>
          <h1>Personas</h1>
          <p>CRUD completo conectado con Spring Boot.</p>
        </div>
        <button type="button" className="btn-primary" onClick={toggleFormulario}>
          {mostrarFormulario ? 'Cerrar formulario' : '+ Nueva persona'}
        </button>
      </div>

      {loading && <div className="state-card">Cargando personas...</div>}
      {loadError && <div className="state-card error">{loadError}</div>}

      {!loading && !loadError && (
        <>
          <div className="stats-grid">
            <article className="stat-card"><span>Total</span><strong>{total}</strong></article>
            <article className="stat-card"><span>Activas</span><strong>{activas}</strong></article>
            <article className="stat-card"><span>Inactivas</span><strong>{total - activas}</strong></article>
          </div>

          {error && <div className="state-card error">{error}</div>}
          {loadingDetail && <div className="state-card">Cargando detalle...</div>}

          {mostrarFormulario && (
            <PersonaForm
              persona={editingPersona}
              onSaved={handleSaved}
              onCancelEdit={() => setEditingPersona(null)}
            />
          )}

          <PersonaTable
            personas={personas}
            onEdit={handleEdit}
            onDelete={handleDelete}
            deletingId={deletingId}
          />
        </>
      )}
    </section>
  );
}
