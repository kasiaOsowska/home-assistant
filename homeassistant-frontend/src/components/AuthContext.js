import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(!!sessionStorage.getItem('sessionId'));
  const [sessionId, setSessionId] = useState(sessionStorage.getItem('sessionId') || '');

  const login = (session) => {
    setIsAuthenticated(true);
    setSessionId(session);
    sessionStorage.setItem('sessionId', session);
  };

  const logout = () => {
    setIsAuthenticated(false);
    setSessionId('');
    sessionStorage.removeItem('sessionId');
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout, sessionId }}>
      {children}
    </AuthContext.Provider>
  );
};
