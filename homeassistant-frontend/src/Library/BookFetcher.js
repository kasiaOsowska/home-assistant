import React, { useState } from 'react';
import axios from 'axios';

const BookFetcher = () => {
  const [books, setBooks] = useState([]);

  const fetchBooks = () => {
    axios.get('http://localhost:8080/books')
      .then(response => {
        setBooks(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the books!', error);
      });
  };

  return (
    <div>
      <button onClick={fetchBooks}>Fetch Books</button>
      <ul>
        {books.map(book => (
          <li key={book.id}>{book.title} by {book.author}</li>
        ))}
      </ul>
    </div>
  );
};

export default BookFetcher;
