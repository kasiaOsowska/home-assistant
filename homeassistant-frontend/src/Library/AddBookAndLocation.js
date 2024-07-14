import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AddBookAndLocation = () => {
  const [bookTitle, setBookTitle] = useState('');
  const [bookAuthor, setBookAuthor] = useState('');
  const [bookGenre, setBookGenre] = useState('');
  const [storageLocationId, setStorageLocationId] = useState('');
  const [locations, setLocations] = useState([]);
  const [locationName, setLocationName] = useState('');

  useEffect(() => {
    // Fetch available storage locations from backend
    axios.get('http://localhost:8080/storage-locations')
      .then(response => {
        setLocations(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the locations!', error);
      });
  }, []);

  const handleAddBook = () => {
    const bookData = {
      title: bookTitle,
      author: bookAuthor,
      genre: bookGenre,
      storageLocationId: storageLocationId
    };

    axios.post('http://localhost:8080/books', bookData)
      .then(response => {
        alert('Book added successfully');
        setBookTitle('');
        setBookAuthor('');
        setBookGenre('');
        setStorageLocationId('');
      })
      .catch(error => {
        console.error('There was an error adding the book!', error);
      });
  };

  const handleAddLocation = () => {
    const locationData = { name: locationName };

    axios.post('http://localhost:8080/storage-locations', locationData)
      .then(response => {
        alert('Location added successfully');
        setLocationName('');
        // Optionally, fetch the updated list of locations
        axios.get('http://localhost:8080/storage-locations')
          .then(response => {
            setLocations(response.data);
          });
      })
      .catch(error => {
        console.error('There was an error adding the location!', error);
      });
  };

return (
    <div>
        <h2>Add New Book</h2>
        <input
            type="text"
            value={bookTitle}
            onChange={(e) => setBookTitle(e.target.value)}
            placeholder="Book Title"
        />
        <br />
        <input
            type="text"
            value={bookAuthor}
            onChange={(e) => setBookAuthor(e.target.value)}
            placeholder="Book Author"
        />
        <br />
        <input
            type="text"
            value={bookGenre}
            onChange={(e) => setBookGenre(e.target.value)}
            placeholder="Book Genre"
        />
        <br />
        <select
            value={storageLocationId}
            onChange={(e) => setStorageLocationId(e.target.value)}
        >
            <option value="">Select Storage Location</option>
            {locations.map(location => (
                <option key={location.id} value={location.id}>
                    {location.name}
                </option>
            ))}
        </select>
        <br />
        <button onClick={handleAddBook}>Add Book</button>

        <h2>Add New Location</h2>
        <input
            type="text"
            value={locationName}
            onChange={(e) => setLocationName(e.target.value)}
            placeholder="Location Name"
        />
        <br />
        <button onClick={handleAddLocation}>Add Location</button>
    </div>
);
};

export default AddBookAndLocation;
