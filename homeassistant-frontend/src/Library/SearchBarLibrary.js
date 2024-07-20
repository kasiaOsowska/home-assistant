import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SearchBar = ({ onSearch, onSuggestionClick }) => {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState([]);

  useEffect(() => {
    if (query.length > 2) {
      axios.get(`http://localhost:8080/home-assistant/api/books/autocomplete?query=${query}`)
        .then(response => {
          setSuggestions(response.data);
        })
        .catch(error => {
          console.error('There was an error fetching the suggestions!', error);
        });
    } else {
      setSuggestions([]);
    }
  }, [query]);

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
