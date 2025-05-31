import React, { useState, useEffect } from 'react';
import LocationSelector from './LocationSelector';
import FinancialImpact from './FinancialImpact';
import weatherApi from '../api/weather';
import { useAuth } from '../contexts/AuthContext';

const Dashboard = () => {
  const { user } = useAuth();
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [weatherData, setWeatherData] = useState(null);
  const [floodRisk, setFloodRisk] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchLocationData = async (location) => {
    try {
      setLoading(true);
      setError(null);

      // Fetch weather and flood risk data in parallel
      const [weather, risk] = await Promise.all([
        weatherApi.getCurrentWeather(location.latitude, location.longitude),
        weatherApi.getFloodRisk(location.latitude, location.longitude)
      ]);

      setWeatherData(weather);
      setFloodRisk(risk);
    } catch (err) {
      console.error('Error fetching location data:', err);
      setError(err.message || 'Failed to fetch location data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (selectedLocation) {
      fetchLocationData(selectedLocation);
      // Set up real-time updates every 15 minutes
      const interval = setInterval(() => {
        fetchLocationData(selectedLocation);
      }, 15 * 60 * 1000);

      return () => clearInterval(interval);
    }
  }, [selectedLocation]);

  const handleLocationSelect = (location) => {
    setSelectedLocation(location);
  };

  if (loading && !selectedLocation) {
    return (
      <div className="animate-pulse space-y-4">
        <div className="h-32 bg-gray-200 rounded"></div>
        <div className="h-64 bg-gray-200 rounded"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">
          Welcome, {user?.name || 'User'}
        </h2>
        <p className="text-gray-600">
          Select a location to view real-time flood risk assessment and weather data.
        </p>
      </div>

      <LocationSelector onLocationSelect={handleLocationSelect} />

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded relative">
          <strong className="font-bold">Error: </strong>
          <span className="block sm:inline">{error}</span>
        </div>
      )}

      {selectedLocation && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-xl font-semibold text-gray-800 mb-4">Current Weather</h3>
            {weatherData ? (
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Temperature</span>
                  <span className="text-lg font-medium">{weatherData.temperature}°C</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Humidity</span>
                  <span className="text-lg font-medium">{weatherData.humidity}%</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Precipitation</span>
                  <span className="text-lg font-medium">{weatherData.precipitation} mm</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Wind Speed</span>
                  <span className="text-lg font-medium">{weatherData.windSpeed} m/s</span>
                </div>
              </div>
            ) : (
              <div className="animate-pulse space-y-4">
                <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                <div className="h-4 bg-gray-200 rounded w-1/2"></div>
              </div>
            )}
          </div>

          <FinancialImpact />
        </div>
      )}
    </div>
  );
};

export default Dashboard; 