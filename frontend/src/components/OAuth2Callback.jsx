import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const OAuth2Callback = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { handleOAuth2Login } = useAuth();

  useEffect(() => {
    const handleCallback = async () => {
      try {
        // Get the token from the URL hash or query parameters
        const params = new URLSearchParams(location.search);
        const token = params.get('token');

        if (token) {
          await handleOAuth2Login({ token });
        } else {
          // If no token is found, redirect to login
          navigate('/login');
        }
      } catch (error) {
        console.error('Error handling OAuth2 callback:', error);
        navigate('/login');
      }
    };

    handleCallback();
  }, [location, navigate, handleOAuth2Login]);

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <h2 className="text-2xl font-semibold mb-4">Processing login...</h2>
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
      </div>
    </div>
  );
};

export default OAuth2Callback; 