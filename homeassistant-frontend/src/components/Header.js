import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from './AuthContext';
import './Footer.css';

const Header = ({ setView }) => {
  const location = useLocation();
  const { isAuthenticated, logout, sessionId } = useAuth();

  const renderMenuItems = () => {
    if (location.pathname.startsWith('/home-assistant/library')) {
      return (
        <>
          <li className="nav-item">
            <Link to="/home-assistant" className="nav-link">Home</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('start')}>Biblioteka</Link>
          </li>
        </>
      );
    } else {
      return (
        <>
          <li className="nav-item">
            <Link to="/home-assistant" className="nav-link">Home</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('start')}>Biblioteka</Link>
          </li>
        </>
      );
    }
  };

  const renderUserSection = () => {
    if (isAuthenticated) {
      return (
        <>
          <li className="nav-item">
            <Link to="#" className="nav-link" onClick={logout}>Wyloguj</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library/add" className="nav-link">Zarządzaj książkami</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('search')}>Szukaj</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/library" className="nav-link" onClick={() => setView('recommend')}>Zaproponuj</Link>
          </li>
        </>
      );
    } else {
      return (
        <>
          <li className="nav-item">
            <Link to="/home-assistant/register" className="nav-link">Zarejestruj się</Link>
          </li>
          <li className="nav-item">
            <Link to="/home-assistant/login" className="nav-link">Zaloguj się</Link>
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
                  {renderUserSection()}
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
