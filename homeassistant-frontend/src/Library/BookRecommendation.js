import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../components/AuthContext';

const BookRecommendation = () => {
  const [description, setDescription] = useState('');
  const [recommendation, setRecommendation] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { sessionId } = useAuth();

  const handleInputChange = (e) => {
    setDescription(e.target.value);
  };

  const handleSubmit = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await axios.post('http://localhost:8080/home-assistant/api/books/recommend', description, {
        headers: {
          'Content-Type': 'application/json'
        },
        params: {
          sessionId: sessionId
        }
      });
      setRecommendation(response.data);
    } catch (err) {
      if (err.response && err.response.status === 429) {
        setError('Too many requests. Please try again later.');
      } else {
        setError('An error occurred. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h3>Rekomendacje</h3>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
        <textarea
          value={description}
          onChange={handleInputChange}
          placeholder="Napisz o czym chcesz poczytać..."
        />
        <button style={{ marginLeft: '10px' }} onClick={handleSubmit} disabled={loading}>
          {loading ? 'Ładowanie...' : 'Zaproponuj książkę'}
        </button>
      </div>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {recommendation && (
        <div>
          <h4>Rekomendacja:</h4>
          <p className='output'>{recommendation}</p>
        </div>
      )}
    </div>
  );
};

export default BookRecommendation;
