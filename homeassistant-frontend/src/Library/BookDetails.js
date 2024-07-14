import React from 'react';

const BookDetails = ({ book }) => {
  return (
    <div>
      <h3>{book.title}</h3>
      <p><strong>Author:</strong> {book.author}</p>
      <p><strong>Genre:</strong> {book.genre}</p>
      <p><strong>Storage Location:</strong> {book.storageLocation.name}</p>
    </div>
  );
};

export default BookDetails;
