import { useEffect, useState } from 'react';
import type { Persona } from '../../personas/models/Persona';
import { personaService } from '../../personas/services/personaService';
import type { Unidad } from '../../unidades/models/Unidad';
import { unidadService } from '../../unidades/services/unidadService';
import ResidenciaForm from '../components/ResidenciaForm';
import ResidenciaTable from '../components/ResidenciaTable';
import type { Residencia } from '../models/Residencia';
import { residenciaService } from '../services/residenciaService';

export default function ResidenciasPage() {
  const [residencias, setResidencias] = useState<Residencia[]>([]);
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [unidades, setUnidades] = useState<Unidad[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [mostrarFormulario, setMostrarFormulario] = useState(false);

  useEffect(() => {
    const controller = new AbortController();

    const cargar = async () => {
      try {
        setLoading(true);
        setError('');
        // Residencias, personas (padre) y unidades se piden una sola vez y en paralelo:
        // la tabla resuelve los nombres en memoria, sin una petición por fila.
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
        setError(err instanceof Error ? err.message : 'Error inesperado');
      } finally {
        if (!controller.signal.aborted) setLoading(false);
      }
    };

    void cargar();
    return () => controller.abort();
  }, []);

  const total = residencias.length;
  const vigentes = residencias.filter((residencia) => residencia.estado === 'VIGENTE').length;

  return (
    <section className="feature-page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">GESTIÓN DE RESIDENCIAS</p>
          <h1>Residencias</h1>
          <p>Residencias asociadas a personas mediante personaId.</p>
        </div>
        <button
          type="button"
          className="btn-primary"
          onClick={() => setMostrarFormulario((prev) => !prev)}
        >
          {mostrarFormulario ? 'Cerrar formulario' : '+ Nueva residencia'}
        </button>
      </div>

      {loading && <div className="state-card">Cargando residencias...</div>}
      {error && <div className="state-card error">{error}</div>}

      {!loading && !error && (
        <>
          <div className="stats-grid">
            <article className="stat-card"><span>Total</span><strong>{total}</strong></article>
            <article className="stat-card"><span>Vigentes</span><strong>{vigentes}</strong></article>
            <article className="stat-card"><span>Finalizadas</span><strong>{total - vigentes}</strong></article>
          </div>

          {mostrarFormulario && (
            <ResidenciaForm
              personas={personas}
              unidades={unidades}
              onCreated={(nueva) => setResidencias((prev) => [...prev, nueva])}
            />
          )}
          <ResidenciaTable residencias={residencias} personas={personas} unidades={unidades} />
        </>
      )}
    </section>
  );
}
