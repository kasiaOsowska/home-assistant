import React, { useState, useEffect } from 'react';
import axios from 'axios';
import SearchBar from './SearchBarLibrary';
import BookRecommendation from './BookRecommendation';
import { useAuth } from '../components/AuthContext';
import config from '../config'; // Importujemy config

const LibraryHome = ({ view }) => {
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);
  const { sessionId } = useAuth();

  const fetchBooks = async (url) => {
    try {
      const response = await axios.get(url, {
        params: { sessionId }
      });
      const books = response.data;
      
      // Fetching the storage location names
      for (const book of books) {
        book.storageLocationName = await fetchStorageLocationName(book.storageLocationId);
      }
      
      setSearchResults(books);
      setIsSearching(false);
    } catch (error) {
      console.error('There was an error fetching the books!', error);
    }
  };

  const fetchAllBooks = () => {
    fetchBooks(`${config.apiUrl}/books/all`);
  };

  const fetchUserBooks = () => {
    fetchBooks(`${config.apiUrl}/books/by-user`);
  };

  const handleSearch = (query) => {
    if (!query) {
      fetchUserBooks();
    } else {
      setIsSearching(true);
      fetchBooks(`${config.apiUrl}/books/search/title?title=${query}`);
    }
  };

  const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  };

  const fetchStorageLocationName = async (id) => {
    try {
      const response = await axios.get(`${config.apiUrl}/storage-locations/${id}/name`);
      return response.data;
    } catch (error) {
      console.error('There was an error fetching the storage location name!', error);
      return '';
    }
  };

  const fetchRandomBooks = async () => {
    try {
      const response = await axios.get(`${config.apiUrl}/books/by-user`, {
        params: { sessionId }
      });
      const allBooks = response.data;
      const shuffledBooks = shuffleArray([...allBooks]);
      const randomBooks = shuffledBooks.slice(0, 5);

      for (const book of randomBooks) {
        book.storageLocationName = await fetchStorageLocationName(book.storageLocationId);
      }

      setSearchResults(randomBooks);
      setIsSearching(false);
    } catch (error) {
      console.error('There was an error fetching random books!', error);
    }
  };

  const handleSuggestionClick = (suggestion) => {
    if (!suggestion) {
      fetchUserBooks();
    } else {
      setIsSearching(true);
      fetchBooks(`${config.apiUrl}/books/search/title?title=${suggestion}`);
    }
  };

  useEffect(() => {
    fetchRandomBooks();
  }, []);

  const renderTable = (books) => {
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
            <tr key={book.title}>
              <td>{book.title}</td>
              <td>{book.author}</td>
              <td>{book.genre}</td>
              <td>{book.storageLocationName}</td>
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
          <h3>{isSearching ? 'Wyniki wyszukiwania' : 'Losowe książki'}</h3>
          {renderTable(searchResults)}
        </div>
      </div>
    </div>
  );
};

export default LibraryHome;
