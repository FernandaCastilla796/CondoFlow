// Convierte una fecha ISO de la API ('2026-09-01') al formato local '01/09/2026'.
// Se separa el texto en lugar de usar new Date() para no correr la fecha por la zona horaria.
export function formatDate(fecha: string | null): string {
  if (!fecha) return '—';
  const [anio, mes, dia] = fecha.split('-');
  return `${dia}/${mes}/${anio}`;
}
