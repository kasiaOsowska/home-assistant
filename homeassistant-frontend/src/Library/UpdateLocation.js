import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UpdateLocation = () => {
  const [locations, setLocations] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [locationName, setLocationName] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8080/home-assistant/api/storage-locations')
      .then(response => {
        setLocations(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the locations!', error);
      });
  }, []);

  const handleLocationSelect = (location) => {
    setSelectedLocation(location);
    setLocationName(location.name || '');
  };

  const handleUpdateLocation = () => {
    if (!selectedLocation) {
      alert('No location selected for update');
      return;
    }

    const locationData = { name: locationName };

    axios.put(`http://localhost:8080/home-assistant/api/storage-locations/${selectedLocation.id}`, locationData)
      .then(response => {
        alert('Location updated successfully');
        setSelectedLocation(null);
        setLocationName('');
        fetchLocations();
      })
      .catch(error => {
        console.error('There was an error updating the location!', error);
      });
  };

  const fetchLocations = () => {
    axios.get('http://localhost:8080/home-assistant/api/storage-locations')
      .then(response => {
        setLocations(response.data);
      });
  };

  return (
    <div>
      <h2>Update Location</h2>
      <select onChange={(e) => handleLocationSelect(locations.find(loc => loc.id === e.target.value))}>
        <option value="">Select Location to Update</option>
        {locations.map(location => (
          <option key={location.id} value={location.id}>
            {location.name}
          </option>
        ))}
      </select>
      <br />
      {selectedLocation && (
        <>
          <input
            type="text"
            value={locationName}
            onChange={(e) => setLocationName(e.target.value)}
            placeholder="Location Name"
          />
          <br />
          <button onClick={handleUpdateLocation}>Update Location</button>
        </>
      )}
    </div>
  );
};

export default UpdateLocation;
