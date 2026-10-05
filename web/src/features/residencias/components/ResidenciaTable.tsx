import type { Persona } from '../../personas/models/Persona';
import type { Unidad } from '../../unidades/models/Unidad';
import { formatDate } from '../../../utils/formatDate';
import type { Residencia } from '../models/Residencia';

interface ResidenciaTableProps {
  residencias: Residencia[];
  personas: Persona[];
  unidades: Unidad[];
}

export default function ResidenciaTable({ residencias, personas, unidades }: ResidenciaTableProps) {
  // find() busca la persona cuyo id coincide con residencia.personaId (relación 1:N).
  const obtenerNombrePersona = (personaId: number) => {
    const persona = personas.find((item) => item.personaId === personaId);
    return persona ? `${persona.nombre} ${persona.apellido}` : 'Sin persona asociada';
  };

  const obtenerNumeroUnidad = (unidadId: number) =>
    unidades.find((item) => item.unidadId === unidadId)?.numeroUnidad ?? `#${unidadId}`;

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
            </tr>
          </thead>
          <tbody>
            {residencias.map((residencia) => (
              <tr key={residencia.residenciaId}>
                <td>{residencia.residenciaId}</td>
                <td>{obtenerNombrePersona(residencia.personaId)}</td>
                <td className="unit-cell">{obtenerNumeroUnidad(residencia.unidadId)}</td>
                <td>{residencia.tipoResidencia === 'PROPIETARIO' ? 'Propietario' : 'Inquilino'}</td>
                <td>{formatDate(residencia.fechaInicio)}</td>
                <td>{formatDate(residencia.fechaFin)}</td>
                <td>
                  <span className={`status-badge ${residencia.estado === 'VIGENTE' ? 'status-active' : 'status-inactive'}`}>
                    {residencia.estado === 'VIGENTE' ? 'Vigente' : 'Finalizada'}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
