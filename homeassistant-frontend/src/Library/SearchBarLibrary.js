import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../components/AuthContext';
import config from '../config'; // Importujemy config

const SearchBar = ({ onSearch, onSuggestionClick }) => {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const { sessionId } = useAuth();

  useEffect(() => {
    if (query.length > 2) {
      axios.get(`${config.apiUrl}/books/autocomplete`, {
        params: { query, sessionId }
      })
      .then(response => {
        setSuggestions(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the suggestions!', error);
      });
    } else {
      setSuggestions([]);
    }
  }, [query, sessionId]);

  const handleInputChange = (event) => {
    setQuery(event.target.value);
  };

  const handleSearch = () => {
    onSearch(query);
    setSuggestions([]);
  };

  const handleSuggestionClick = (suggestion) => {
    setQuery(suggestion);
    setSuggestions([]);
    onSuggestionClick(suggestion);
  };

  return (
    <div>
      <input
        type="text"
        value={query}
        onChange={handleInputChange}
        placeholder="Szukaj po tytule lub autorze..."
      />
      <button onClick={handleSearch}>Szukaj</button>
      <h3>Podpowiedź</h3>
      {suggestions.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Tytuł</th>
            </tr>
          </thead>
          <tbody>
            {suggestions.map((suggestion, index) => (
              <tr key={index} onClick={() => handleSuggestionClick(suggestion)} style={{ cursor: 'pointer' }}>
                <td>{suggestion}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default SearchBar;
