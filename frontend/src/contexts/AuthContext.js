import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as apiLogin, register as apiRegister, handleOAuth2Response } from '../services/authService';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // Check if user and token are stored in localStorage
    const storedUser = localStorage.getItem('user');
    const storedToken = localStorage.getItem('token');
    if (storedUser && storedToken) {
      setUser(JSON.parse(storedUser));
    } else {
      // If either is missing, clear both
      localStorage.removeItem('user');
      localStorage.removeItem('token');
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await apiLogin(email, password);
      const { token, ...userData } = response.data;
      setUser(userData);
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('token', token);
      navigate('/dashboard');
      return response;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  };

  const register = async (userData) => {
    try {
      const response = await apiRegister(userData);
      const { token, ...userInfo } = response.data;
      setUser(userInfo);
      localStorage.setItem('user', JSON.stringify(userInfo));
      localStorage.setItem('token', token);
      navigate('/dashboard');
      return response;
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  };

  const handleOAuth2Login = async (response) => {
    try {
      const userData = await handleOAuth2Response(response);
      const { token, ...userInfo } = userData;
      setUser(userInfo);
      localStorage.setItem('user', JSON.stringify(userInfo));
      localStorage.setItem('token', token);
      navigate('/dashboard');
      return userData;
    } catch (error) {
      console.error('OAuth2 login error:', error);
      throw error;
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    navigate('/login');
  };

  const value = {
    user,
    loading,
    login,
    register,
    logout,
    handleOAuth2Login,
    isAuthenticated: !!user && !!localStorage.getItem('token'),
    isAdmin: user?.role === 'ADMIN'
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}; 