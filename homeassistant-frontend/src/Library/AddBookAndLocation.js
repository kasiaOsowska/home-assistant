import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../components/AuthContext';
import ShareBook from './ShareBook';
import UnshareBook from './UnshareBook';

const AddBookAndLocation = () => {
  const [bookTitle, setBookTitle] = useState('');
  const [bookAuthor, setBookAuthor] = useState('');
  const [bookGenre, setBookGenre] = useState('');
  const [storageLocationId, setStorageLocationId] = useState('');
  const [locations, setLocations] = useState([]);
  const [locationName, setLocationName] = useState('');
  const [selectedLocationId, setSelectedLocationId] = useState('');
  const [newLocationName, setNewLocationName] = useState('');
  const [showShareBook, setShowShareBook] = useState(false);
  const [showUnshareBook, setShowUnshareBook] = useState(false);
  const { sessionId } = useAuth();

  useEffect(() => {
    axios.get('http://localhost:8080/home-assistant/api/storage-locations')
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

    axios.post('http://localhost:8080/home-assistant/api/books', bookData, {
      params: { sessionId }
    })
      .then(response => {
        alert('Book added successfully');
        setBookTitle('');
        setBookAuthor('');
        setBookGenre('');
        setStorageLocationId('');
      })
      .catch(error => {
        if (error.response) {
          console.error('Error status:', error.response.status);
          console.error('Error details:', error.response.data);
        } else {
          console.error('Error:', error.message);
        }
      });
  };

  const handleAddLocation = () => {
    const locationData = { name: locationName };

    axios.post('http://localhost:8080/home-assistant/api/storage-locations', locationData)
      .then(response => {
        alert('Location added successfully');
        setLocationName('');
        axios.get('http://localhost:8080/home-assistant/api/storage-locations')
          .then(response => {
            setLocations(response.data);
          });
      })
      .catch(error => {
        console.error('There was an error adding the location!', error);
      });
  };

  const handleUpdateLocation = () => {
    const locationData = { name: newLocationName };

    axios.put(`http://localhost:8080/home-assistant/api/storage-locations/${selectedLocationId}`, locationData)
      .then(response => {
        alert('Location updated successfully');
        setNewLocationName('');
        setSelectedLocationId('');
        axios.get('http://localhost:8080/home-assistant/api/storage-locations')
          .then(response => {
            setLocations(response.data);
          });
      })
      .catch(error => {
        console.error('There was an error updating the location!', error);
      });
  };

  const handleShareSuccess = () => {
    setShowShareBook(false);
  };

  const handleUnshareSuccess = () => {
    setShowUnshareBook(false);
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '5vh' }}>
      <div>
        <h2>Dodaj nową książkę</h2>
        <input
          type="text"
          value={bookTitle}
          onChange={(e) => setBookTitle(e.target.value)}
          placeholder="Tytuł książki"
        />
        <br />
        <input
          type="text"
          value={bookAuthor}
          onChange={(e) => setBookAuthor(e.target.value)}
          placeholder="Autor książki"
        />
        <br />
        <input
          type="text"
          value={bookGenre}
          onChange={(e) => setBookGenre(e.target.value)}
          placeholder="Gatunek książki"
        />
        <br />
        <select
          value={storageLocationId}
          onChange={(e) => setStorageLocationId(e.target.value)}
        >
          <option value="">Wybierz lokalizację przechowywania</option>
          {locations.map(location => (
            <option key={location.id} value={location.id}>
              {location.name}
            </option>
          ))}
        </select>
        <br />
        <button onClick={handleAddBook}>Dodaj książkę</button>

        <h2>Dodaj nową lokalizację</h2>
        <input
          type="text"
          value={locationName}
          onChange={(e) => setLocationName(e.target.value)}
          placeholder="Nazwa lokalizacji"
        />
        <br />
        <button onClick={handleAddLocation}>Dodaj lokalizację</button>

        <h2>Aktualizuj lokalizację</h2>
        <select
          value={selectedLocationId}
          onChange={(e) => setSelectedLocationId(e.target.value)}
        >
          <option value="">Wybierz lokalizację do aktualizacji</option>
          {locations.map(location => (
            <option key={location.id} value={location.id}>
              {location.name}
            </option>
          ))}
        </select>
        <br />
        <input
          type="text"
          value={newLocationName}
          onChange={(e) => setNewLocationName(e.target.value)}
          placeholder="Nowa nazwa lokalizacji"
        />
        <br />
        <button onClick={handleUpdateLocation}>Aktualizuj lokalizację</button>
      </div>

      <div>
        {<ShareBook sessionId={sessionId} onShareSuccess={handleShareSuccess} />}
        
        {<UnshareBook sessionId={sessionId} onUnshareSuccess={handleUnshareSuccess} />}
      </div>
    </div>
  );
};

export default AddBookAndLocation;
