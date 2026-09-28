import type { Residencia } from '../types/Residencia';

interface ResidenciaCardProps {
  residencia: Residencia;
}

/** Tarjeta de una residencia. Todavía no se usa: se conectará a la API en guías posteriores. */
export function ResidenciaCard({ residencia }: ResidenciaCardProps) {
  return (
    <article>
      <h3>Unidad {residencia.unidadId}</h3>
      <p>
        {residencia.tipoResidencia} · {residencia.estado}
      </p>
    </article>
  );
}
