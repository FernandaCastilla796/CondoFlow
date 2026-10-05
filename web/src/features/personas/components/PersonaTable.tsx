import type { Persona } from '../models/Persona';

interface PersonaTableProps {
  personas: Persona[];
}

// La tabla sólo muestra: recibe las personas por props y no sabe de dónde salen los datos.
export default function PersonaTable({ personas }: PersonaTableProps) {
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
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
