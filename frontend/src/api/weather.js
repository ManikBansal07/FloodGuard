import axios from 'axios';
import authApi from './auth';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_URL,
  timeout: 10000, // 10 second timeout
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include auth token
api.interceptors.request.use(
  (config) => {
    const token = authApi.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Add response interceptor for better error handling
api.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', {
      url: error.config?.url,
      method: error.config?.method,
      status: error.response?.status,
      data: error.response?.data,
      message: error.message,
    });
    
    // Handle authentication errors
    if (error.response?.status === 401 || error.response?.status === 403) {
      authApi.logout();
      window.location.href = '/login';
      throw new Error('Please log in to continue');
    }
    
    // Create a more detailed error object
    const enhancedError = new Error(
      error.response?.data?.message || 
      error.message || 
      'An unexpected error occurred'
    );
    enhancedError.status = error.response?.status;
    enhancedError.data = error.response?.data;
    throw enhancedError;
  }
);

const weatherApi = {
  async getCurrentWeather(latitude, longitude) {
    try {
      const response = await api.get('/weather/current', {
        params: { latitude, longitude }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching weather data:', error);
      throw error;
    }
  },

  async getFloodRisk(latitude, longitude) {
    try {
      const response = await api.get('/weather/flood-risk', {
        params: { latitude, longitude }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching flood risk:', error);
      throw error;
    }
  }
};

export default weatherApi; 