import type { Persona } from '../models/Persona';

interface PersonaTableProps {
  personas: Persona[];
  onEdit: (id: number) => void;
  onDelete: (persona: Persona) => void;
  deletingId?: number | null;
}

// La tabla sólo muestra y avisa: pide acciones con callbacks y no importa personaService.
export default function PersonaTable({ personas, onEdit, onDelete, deletingId }: PersonaTableProps) {
  if (personas.length === 0) {
    return <div className="empty-state">No hay personas registradas.</div>;
  }

  return (
    <div className="table-card">
      <div className="table-responsive">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Persona</th>
              <th>Documento</th>
              <th>Teléfono</th>
              <th>Correo electrónico</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {personas.map((persona) => (
              <tr key={persona.personaId}>
                <td>{persona.personaId}</td>
                <td>{persona.nombre} {persona.apellido}</td>
                <td>{persona.documento}</td>
                <td>{persona.telefono}</td>
                <td>{persona.correoElectronico}</td>
                <td>
                  <span className={`status-badge ${persona.estado === 'ACTIVO' ? 'status-active' : 'status-inactive'}`}>
                    {persona.estado === 'ACTIVO' ? 'Activo' : 'Inactivo'}
                  </span>
                </td>
                <td className="actions-cell">
                  <button type="button" className="btn-secondary btn-small" onClick={() => onEdit(persona.personaId)}>
                    Editar
                  </button>
                  <button
                    type="button"
                    className="btn-danger btn-small"
                    onClick={() => onDelete(persona)}
                    disabled={deletingId === persona.personaId}
                  >
                    {deletingId === persona.personaId ? 'Eliminando...' : 'Eliminar'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
