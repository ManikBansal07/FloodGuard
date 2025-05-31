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

const reportsApi = {
  async submitReport(reportData) {
    const formData = new FormData();
    Object.keys(reportData).forEach(key => {
      if (key === 'image' && reportData[key]) {
        formData.append('image', reportData[key]);
      } else {
        formData.append(key, reportData[key]);
      }
    });
    
    const response = await api.post('/reports', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  async getReports(page = 0, size = 10) {
    try {
      const response = await api.get('/reports', {
        params: { page, size }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching reports:', error);
      throw error;
    }
  },

  async getMyReports(page = 0, size = 10) {
    try {
      const response = await api.get('/reports/my', {
        params: { page, size }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching my reports:', error);
      throw error;
    }
  },

  async getNearbyReports(latitude, longitude, radius = 5000) {
    try {
      const response = await api.get('/reports/nearby', {
        params: { latitude, longitude, radius }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching nearby reports:', error);
      throw error;
    }
  }
};

export default reportsApi; 