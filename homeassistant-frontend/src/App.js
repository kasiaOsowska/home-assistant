import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LibraryHome from './Library/LibraryHome';
import Home from './components/Home';
import Header from './components/Header';
import Footer from './components/Footer';
import AddBookAndLocation from './Library/AddBookAndLocation';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/library" element={<LibraryHome />} />
          <Route path="/library/add" element={<AddBookAndLocation />} />
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
