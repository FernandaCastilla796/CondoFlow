import { NavLink } from 'react-router';

const menuItems = [
  { to: '/', label: 'Inicio', end: true },
  { to: '/personas', label: 'Personas' },
  { to: '/residencias', label: 'Residencias' },
  { to: '/acerca', label: 'Acerca' },
];

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar__brand">
        <span className="sidebar__brand-mark">C</span>
        <div>
          <strong>CondoFlow</strong>
          <small>Administración</small>
        </div>
      </div>

      <nav className="sidebar__nav" aria-label="Navegación principal">
        {menuItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.end}
            className={({ isActive }) =>
              `sidebar__link${isActive ? ' sidebar__link--active' : ''}`
            }
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
