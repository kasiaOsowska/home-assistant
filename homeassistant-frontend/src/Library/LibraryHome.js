import React, { useState, useEffect } from 'react';
import axios from 'axios';
import SearchBar from './SearchBarLibrary';
import BookRecommendation from './BookRecommendation';

const LibraryHome = ({ view }) => {
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false); // New state to track if a search is being performed

  const fetchAllBooks = () => {
    axios.get('http://localhost:8080/home-assistant/api/books')
      .then(response => {
        setSearchResults(response.data);
        setIsSearching(false); // Reset to indicate random books are being shown
      })
      .catch(error => {
        console.error('There was an error fetching all books!', error);
      });
  };

  const handleSearch = (query) => {
    if (!query) {
      fetchAllBooks();
    } else {
      setIsSearching(true); // Indicate that a search is being performed
      axios.get(`http://localhost:8080/home-assistant/api/books/search/title?title=${query}`)
        .then(response => {
          setSearchResults(response.data);
        })
        .catch(error => {
          console.error('There was an error searching for books!', error);
        });
    }
  };

  const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  };
  
  const fetchRandomBooks = () => {
    axios.get('http://localhost:8080/home-assistant/api/books')
      .then(response => {
        const allBooks = response.data;
        const shuffledBooks = shuffleArray(allBooks);
        const randomBooks = shuffledBooks.slice(0, 5);
        setSearchResults(randomBooks);
        setIsSearching(false); // Indicate that random books are being shown
      })
      .catch(error => {
        console.error('There was an error fetching random books!', error);
      });
  };

  const handleSuggestionClick = (suggestion) => {
    if (!suggestion) {
      fetchAllBooks();
    } else {
      setIsSearching(true); // Indicate that a search is being performed
      axios.get(`http://localhost:8080/home-assistant/api/books/search/title?title=${suggestion}`)
        .then(response => {
          setSearchResults(response.data);
        })
        .catch(error => {
          console.error('There was an error fetching the book details!', error);
        });
    }
  };

  useEffect(() => {
    fetchRandomBooks();
  }, []);

  const renderTable = (books) => {
    // Sort books alphabetically by title
    const sortedBooks = books.sort((a, b) => a.title.localeCompare(b.title));

    return (
      <table>
        <thead>
          <tr>
            <th>Tytuł</th>
            <th>Autor</th>
            <th>Gatunek</th>
            <th>Lokalizacja</th>
          </tr>
        </thead>
        <tbody>
          {sortedBooks.map(book => (
            <tr key={book.id}>
              <td>{book.title}</td>
              <td>{book.author}</td>
              <td>{book.genre}</td>
              <td>{book.storageLocation.name}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '5vh' }}>
      <div style={{ marginRight: '5vw' }}>
        {view === 'search' && (
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <SearchBar onSearch={handleSearch} onSuggestionClick={handleSuggestionClick} />
          </div>
        )}
        {view === 'recommend' && <BookRecommendation />}
        <div>
          <h3>{isSearching ? 'Wyniki wyszukiwania' : 'Losowe książki'}</h3> {/* Conditional heading */}
          {renderTable(searchResults)}
        </div>
      </div>
    </div>
  );
};

export default LibraryHome;
