import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LibraryHome from './Library/LibraryHome';
import Home from './components/Home';
import Header from './components/Header';
import Footer from './components/Footer';
import AddBookAndLocation from './Library/AddBookAndLocation';
import './App.css';
import Login from './components/login';
import { AuthProvider } from './components/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  const [view, setView] = useState('search');

  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header setView={setView} />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/library" element={<LibraryHome view={view} />} />
            <Route path="/library/add" element={<ProtectedRoute element={<AddBookAndLocation />} />} />
            <Route path="/login" element={<Login />} />
          </Routes>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;