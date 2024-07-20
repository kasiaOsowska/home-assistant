import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Footer.css';

const Header = ({ setView }) => {
  const location = useLocation();

  const renderMenuItems = () => {
    if (location.pathname === '/home-assistant/library') {
      return (
        <>
          <li className="nav-item">
            <Link to="/home-assistant" className="nav-link">Home</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('search')}>Szukaj</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('recommend')}>Zaproponuj</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/login" className="nav-link">Zarządzaj książkami</Link>
          </li>
        </>
      );
    } else {
      return (
        <>
          <li className="nav-item">
            <Link to="/home-assistant/" className="nav-link">Home</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('start')}>Biblioteka</Link>
          </li>
        </>
      );
    }
  };

  return (
    <header className="navigation-wrap">
      <div>
        <div>
          <div>
            <nav className="navbar">
              <h3 style={{ marginLeft: "20px" }}>home-assistant</h3>
              <div className="navbar-collapse">
                <ul className="navbar-nav">
                  {renderMenuItems()}
                </ul>
              </div>
            </nav>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
