import React, { useState } from 'react';
import axios from 'axios';
import SearchBar from './SearchBarLibrary';
import BookDetails from './BookDetails';

const LibraryHome = () => {
  const [allBooks, setAllBooks] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [selectedBook, setSelectedBook] = useState(null);

  const fetchBooks = () => {
    axios.get('http://localhost:8080/books')
      .then(response => {
        setAllBooks(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the books!', error);
      });
  };

  const handleSearch = (query) => {
    axios.get(`http://localhost:8080/books/search/title?title=${query}`)
      .then(response => {
        setSearchResults(response.data);
      })
      .catch(error => {
        console.error('There was an error searching for books!', error);
      });
  };

  const handleSuggestionClick = (suggestion) => {
    axios.get(`http://localhost:8080/books/search/title?title=${suggestion}`)
      .then(response => {
        setSearchResults(response.data); 
      })
      .catch(error => {
        console.error('There was an error fetching the book details!', error);
      });
  };

  const renderTable = (books) => (
    <table>
      <thead>
        <tr>
          <th>Title</th>
          <th>Author</th>
          <th>Genre</th>
          <th>Location</th>
        </tr>
      </thead>
      <tbody>
        {books.map(book => (
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

  return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '5vh' }}>
      <div style={{ marginRight: '5vw' }}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <SearchBar onSearch={handleSearch} onSuggestionClick={handleSuggestionClick} />
        </div>
        <div>
          <h3>Search Results</h3>
          {renderTable(searchResults)}
        </div>
      </div>
      <div>
        <button onClick={fetchBooks} style={{  marginTop: '1vh' }}>Fetch All Books</button>
        <div style={{  marginTop: '3vh' }}>
          <h3>All Books</h3>
          {renderTable(allBooks)}
        </div>
      </div>
      {selectedBook && <BookDetails book={selectedBook} />}
    </div>
  );
};

export default LibraryHome;
