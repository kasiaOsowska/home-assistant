import React from 'react';
import { useLocation } from 'react-router-dom';

const Header = () => {
  const location = useLocation();

  const renderHeaderContent = () => {
    switch (location.pathname) {
      case '/library':
        return 'Biblioteka';
      default:
        return '';
    }
  };

  return (
    <header>
      <h3>Home Assistant</h3>
      <h1>{renderHeaderContent()}</h1>
    </header>
  );
};

export default Header;
