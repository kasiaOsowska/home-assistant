import React, { useState, useEffect } from 'react';
import axios from 'axios';
import config from '../config'; // Importujemy config

const ShareBook = ({ sessionId, onShareSuccess }) => {
  const [users, setUsers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [userId, setUserId] = useState('');

  useEffect(() => {
    axios.get(`${config.apiUrl}/users/by-session`, {
      params: { sessionId }
    })
      .then(response => {
        setUserId(response.data.id);
        return axios.get(`${config.apiUrl}/users/all-except`, {
          params: { sessionId }
        });
      })
      .then(response => {
        setUsers(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the users!', error);
        setError('Failed to fetch users');
      });
  }, [sessionId]);

  const handleShare = () => {
    axios.post(`${config.apiUrl}/users/share`, null, {
      params: { targetUserId: selectedUserId, sessionId }
    })
      .then(response => {
        setSuccess('Udostępniono książki!');
        setSelectedUserId('');
        if (onShareSuccess) onShareSuccess();
      })
      .catch(error => {
        setError('There was an error sharing the books. Please try again.');
        console.error('There was an error sharing the books!', error);
      });
  };

  return (
    <div className="share-book" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h2>Udostępnianie książek</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
      <select value={selectedUserId} onChange={(e) => setSelectedUserId(e.target.value)}>
        <option value="">Wybierz użytkownika</option>
        {users.map(user => (
          <option key={user.id} value={user.id}>{user.username}</option>
        ))}
      </select>
      <button onClick={handleShare}>Udostępnij</button>
    </div>
  );
};

export default ShareBook;
