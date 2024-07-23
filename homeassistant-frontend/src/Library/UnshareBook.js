import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../components/AuthContext';

const UnshareBook = ({ sessionId, onUnshareSuccess }) => {
  const [borrowers, setBorrowers] = useState([]);
  const [selectedBorrowerId, setSelectedBorrowerId] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8080/home-assistant/api/users/borrowers', {
      params: { sessionId }
    })
      .then(response => {
        setBorrowers(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the borrowers!', error);
        setError('Failed to fetch borrowers');
      });
  }, [sessionId]);

  const handleUnshare = () => {
    axios.post('http://localhost:8080/home-assistant/api/users/unshare', null, {
      params: { borrowerId: selectedBorrowerId, sessionId }
    })
      .then(response => {
        setSuccess('Nie udostępniasz już książek tej osobie!');
        setSelectedBorrowerId('');
        if (onUnshareSuccess) onUnshareSuccess();
      })
      .catch(error => {
        setError('There was an error unsharing the books. Please try again.');
        console.error('There was an error unsharing the books!', error);
      });
  };

  return (
    <div className="unshare-book" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h2>Przestań udostępniać książki</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
      <select value={selectedBorrowerId} onChange={(e) => setSelectedBorrowerId(e.target.value)}>
        <option value="">Wybierz użytkownika</option>
        {borrowers.map(borrower => (
          <option key={borrower.id} value={borrower.id}>{borrower.username}</option>
        ))}
      </select>
      <button onClick={handleUnshare}>Przestań udostępniać</button>
    </div>
  );
};

export default UnshareBook;
