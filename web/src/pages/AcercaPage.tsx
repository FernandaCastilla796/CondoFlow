// Práctica obligatoria de la Guía 02: página global /acerca dentro del layout principal.
export default function AcercaPage() {
  return (
    <section>
      <div className="page-heading">
        <div>
          <p className="page-heading__eyebrow">Proyecto PA-04</p>
          <h2>Acerca de CondoFlow</h2>
        </div>
      </div>
      <div className="placeholder-card">
        <h3>Administración operativa de condominio</h3>
        <p>
          CondoFlow registra a las personas del condominio y sus residencias en cada unidad.
          El frontend en React + TypeScript consume la API REST del backend Spring Boot,
          que guarda los datos en PostgreSQL.
        </p>
      </div>
    </section>
  );
}
