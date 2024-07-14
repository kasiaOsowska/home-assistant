import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SearchBar = ({ onSearch, onSuggestionClick }) => {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState([]);

  useEffect(() => {
    if (query.length > 2) {
      axios.get(`http://localhost:8080/books/autocomplete?query=${query}`)
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
      <ul>
        {suggestions.map((suggestion, index) => (
          <a key={index} onClick={() => handleSuggestionClick(suggestion)}style={{cursor: 'pointer'}}>
            {suggestion}
          </a>
        ))}
      </ul>
    </div>
  );
};

export default SearchBar;
