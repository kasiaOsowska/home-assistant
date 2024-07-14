import React from 'react';
import './Footer.css';
import './git-icon.png';

const Footer = () => {
  return (
    <footer className="footer">
      <p>&copy; 2024 Katarzyna Osowska.&nbsp;
        <a href="https://github.com/kasiaOsowska" target="_blank" rel="noopener noreferrer">
          Kod źródłowy&nbsp;
          <img src={require('./git-icon.png')} alt="Git Icon" style={{ width: '30px', height: '30px' }} />
        </a>
      </p>
    </footer>
  );
};

export default Footer;
