import type { Persona } from '../types/Persona';

interface PersonaCardProps {
  persona: Persona;
}

/** Tarjeta de una persona. Todavía no se usa: se conectará a la API en guías posteriores. */
export function PersonaCard({ persona }: PersonaCardProps) {
  return (
    <article>
      <h3>
        {persona.nombre} {persona.apellido}
      </h3>
      <p>{persona.correoElectronico}</p>
    </article>
  );
}
