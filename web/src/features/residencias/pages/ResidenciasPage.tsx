import { useState } from 'react';
import { personasMock } from '../../personas/data/personas.mock';
import ResidenciaForm from '../components/ResidenciaForm';
import ResidenciaTable from '../components/ResidenciaTable';
import { residenciasMock } from '../data/residencias.mock';

export default function ResidenciasPage() {
  const [mostrarFormulario, setMostrarFormulario] = useState(false);

  const total = residenciasMock.length;
  const vigentes = residenciasMock.filter((residencia) => residencia.estado === 'VIGENTE').length;

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

      <div className="stats-grid">
        <article className="stat-card"><span>Total</span><strong>{total}</strong></article>
        <article className="stat-card"><span>Vigentes</span><strong>{vigentes}</strong></article>
        <article className="stat-card"><span>Finalizadas</span><strong>{total - vigentes}</strong></article>
      </div>

      {mostrarFormulario && <ResidenciaForm />}
      <ResidenciaTable residencias={residenciasMock} personas={personasMock} />
    </section>
  );
}
