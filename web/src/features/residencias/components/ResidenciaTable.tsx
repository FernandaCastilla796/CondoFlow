import type { Persona } from '../../personas/models/Persona';
import type { Unidad } from '../../unidades/models/Unidad';
import { formatDate } from '../../../utils/formatDate';
import type { Residencia } from '../models/Residencia';

interface ResidenciaTableProps {
  residencias: Residencia[];
  personas: Persona[];
  unidades: Unidad[];
  onEdit: (id: number) => void;
  onDelete: (residencia: Residencia) => void;
  deletingId?: number | null;
}

export default function ResidenciaTable({
  residencias,
  personas,
  unidades,
  onEdit,
  onDelete,
  deletingId,
}: ResidenciaTableProps) {
  // Un Map por id resuelve la persona y la unidad de cada fila en memoria:
  // la tabla no hace un GET por fila (evita el N+1 desde el navegador) y no tiene efectos secundarios.
  const personaPorId = new Map(personas.map((persona) => [persona.personaId, persona]));
  const unidadPorId = new Map(unidades.map((unidad) => [unidad.unidadId, unidad]));

  if (residencias.length === 0) {
    return <div className="empty-state">No hay residencias registradas.</div>;
  }

  return (
    <div className="table-card">
      <div className="table-responsive">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Persona</th>
              <th>Unidad</th>
              <th>Tipo</th>
              <th>Inicio</th>
              <th>Fin</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {residencias.map((residencia) => {
              const persona = personaPorId.get(residencia.personaId);
              const unidad = unidadPorId.get(residencia.unidadId);

              return (
                <tr key={residencia.residenciaId}>
                  <td>{residencia.residenciaId}</td>
                  <td>{persona ? `${persona.nombre} ${persona.apellido}` : `Persona #${residencia.personaId}`}</td>
                  <td className="unit-cell">{unidad ? unidad.numeroUnidad : `#${residencia.unidadId}`}</td>
                  <td>{residencia.tipoResidencia === 'PROPIETARIO' ? 'Propietario' : 'Inquilino'}</td>
                  <td>{formatDate(residencia.fechaInicio)}</td>
                  <td>{formatDate(residencia.fechaFin)}</td>
                  <td>
                    <span className={`status-badge ${residencia.estado === 'VIGENTE' ? 'status-active' : 'status-inactive'}`}>
                      {residencia.estado === 'VIGENTE' ? 'Vigente' : 'Finalizada'}
                    </span>
                  </td>
                  <td className="actions-cell">
                    <button
                      type="button"
                      className="btn-secondary btn-small"
                      onClick={() => onEdit(residencia.residenciaId)}
                    >
                      Editar
                    </button>
                    <button
                      type="button"
                      className="btn-danger btn-small"
                      onClick={() => onDelete(residencia)}
                      disabled={deletingId === residencia.residenciaId}
                    >
                      {deletingId === residencia.residenciaId ? 'Eliminando...' : 'Eliminar'}
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}
