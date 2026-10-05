import { useEffect, useState } from 'react';
import type { Persona } from '../../personas/models/Persona';
import { personaService } from '../../personas/services/personaService';
import type { Unidad } from '../../unidades/models/Unidad';
import { unidadService } from '../../unidades/services/unidadService';
import ResidenciaForm from '../components/ResidenciaForm';
import ResidenciaTable from '../components/ResidenciaTable';
import type { Residencia } from '../models/Residencia';
import { residenciaService } from '../services/residenciaService';

// CRUD completo de Residencia + relación Persona 1:N (Guía 07).
export default function ResidenciasPage() {
  const [residencias, setResidencias] = useState<Residencia[]>([]);
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [unidades, setUnidades] = useState<Unidad[]>([]);
  const [editingResidencia, setEditingResidencia] = useState<Residencia | null>(null);
  const [personaFiltro, setPersonaFiltro] = useState('');
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [deletingId, setDeletingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState('');
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const controller = new AbortController();

    const cargar = async () => {
      try {
        setLoading(true);
        setLoadError('');
        // Las tres consultas son independientes: Promise.all las ejecuta en paralelo.
        // Si una falla, la carga inicial se considera fallida y se muestra el error.
        const [residenciasData, personasData, unidadesData] = await Promise.all([
          residenciaService.listar(controller.signal),
          personaService.listar(controller.signal),
          unidadService.listar(controller.signal),
        ]);
        setResidencias(residenciasData);
        setPersonas(personasData);
        setUnidades(unidadesData);
      } catch (err) {
        if (err instanceof DOMException && err.name === 'AbortError') return;
        setLoadError(err instanceof Error ? err.message : 'Error al cargar los datos');
      } finally {
        if (!controller.signal.aborted) setLoading(false);
      }
    };

    void cargar();
    return () => controller.abort();
  }, []);

  // GET /api/residencias/{id} antes de editar.
  const handleEdit = async (id: number) => {
    try {
      setLoadingDetail(true);
      setError('');
      const residencia = await residenciaService.obtenerPorId(id);
      setEditingResidencia(residencia);
      setMostrarFormulario(true);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo cargar la residencia');
    } finally {
      setLoadingDetail(false);
    }
  };

  // Se usa la respuesta del backend (no sólo el payload enviado): es lo que quedó guardado en PostgreSQL.
  const handleSaved = (saved: Residencia, mode: 'create' | 'edit') => {
    setResidencias((prev) =>
      mode === 'create'
        ? [...prev, saved]
        : prev.map((item) => (item.residenciaId === saved.residenciaId ? saved : item)),
    );
    setEditingResidencia(null);
  };

  // La UI sólo cambia después de una respuesta exitosa; si el backend rechaza, se conserva la fila.
  const handleDelete = async (residencia: Residencia) => {
    if (!window.confirm(`¿Eliminar la residencia #${residencia.residenciaId}?`)) return;

    try {
      setDeletingId(residencia.residenciaId);
      setError('');
      await residenciaService.eliminar(residencia.residenciaId);
      setResidencias((prev) => prev.filter((item) => item.residenciaId !== residencia.residenciaId));
      if (editingResidencia?.residenciaId === residencia.residenciaId) setEditingResidencia(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar la residencia');
    } finally {
      setDeletingId(null);
    }
  };

  const toggleFormulario = () => {
    if (mostrarFormulario) setEditingResidencia(null);
    setMostrarFormulario((prev) => !prev);
  };

  // Filtro local: la lista ya está cargada, así que se filtra en memoria sin otra petición.
  // El value del select es string; se convierte con Number para compararlo con personaId (number).
  const residenciasVisibles = personaFiltro === ''
    ? residencias
    : residencias.filter((residencia) => residencia.personaId === Number(personaFiltro));

  const total = residencias.length;
  const vigentes = residencias.filter((residencia) => residencia.estado === 'VIGENTE').length;

  return (
    <section className="feature-page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">GESTIÓN DE RESIDENCIAS</p>
          <h1>Residencias</h1>
          <p>CRUD completo + relación Persona 1:N.</p>
        </div>
        <button type="button" className="btn-primary" onClick={toggleFormulario}>
          {mostrarFormulario ? 'Cerrar formulario' : '+ Nueva residencia'}
        </button>
      </div>

      {loading && <div className="state-card">Cargando residencias...</div>}
      {loadError && <div className="state-card error">{loadError}</div>}

      {!loading && !loadError && (
        <>
          <div className="stats-grid">
            <article className="stat-card"><span>Total</span><strong>{total}</strong></article>
            <article className="stat-card"><span>Vigentes</span><strong>{vigentes}</strong></article>
            <article className="stat-card"><span>Finalizadas</span><strong>{total - vigentes}</strong></article>
          </div>

          {error && <div className="state-card error">{error}</div>}
          {loadingDetail && <div className="state-card">Cargando detalle...</div>}

          {mostrarFormulario && (
            <ResidenciaForm
              personas={personas}
              unidades={unidades}
              residencia={editingResidencia}
              onSaved={handleSaved}
              onCancelEdit={() => setEditingResidencia(null)}
            />
          )}

          <div className="filter-bar">
            <label>
              Filtrar por persona
              <select value={personaFiltro} onChange={(e) => setPersonaFiltro(e.target.value)}>
                <option value="">Todas las personas</option>
                {personas.map((persona) => (
                  <option key={persona.personaId} value={String(persona.personaId)}>
                    {persona.nombre} {persona.apellido}
                  </option>
                ))}
              </select>
            </label>
            <span>Mostrando {residenciasVisibles.length} de {total}</span>
          </div>

          <ResidenciaTable
            residencias={residenciasVisibles}
            personas={personas}
            unidades={unidades}
            onEdit={handleEdit}
            onDelete={handleDelete}
            deletingId={deletingId}
          />
        </>
      )}
    </section>
  );
}
