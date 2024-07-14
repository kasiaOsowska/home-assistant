import React from 'react';
import { useNavigate } from 'react-router-dom';

const LibraryHome = () => {
  const navigate = useNavigate();

  const goToLibrary = () => {
    navigate('/library');
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
      <nav>
        <button onClick={goToLibrary}>Biblioteka</button>
        <span style={{ margin: '0 10px' }}></span>
        <button onClick={goToLibrary}>Biblioteka</button>
      </nav>
    </div>
  );
};

export default LibraryHome;
