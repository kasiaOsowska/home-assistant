import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      const response = await axios.post(
        'http://localhost:8080/home-assistant/api/users/register',
        new URLSearchParams({
          username,
          password
        }),
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }
      );
      setSuccess('Registration successful! You can now log in.');
      setUsername('');
      setPassword('');
      setError('');
      // Optionally navigate to login page after successful registration
      navigate('/home-assistant/login');
    } catch (error) {
      setError('Registration failed. Please try again.');
      setSuccess('');
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <input
        type="text"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        placeholder="Username"
      />
      <br />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
      />
      <br />
      <button onClick={handleRegister}>Register</button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
    </div>
  );
};

export default Register;
