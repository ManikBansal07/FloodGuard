import React, { useState, useEffect } from 'react';
import reportsApi from '../api/reports';

const AlertStatus = () => {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAlerts = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await reportsApi.getReports();
        // Filter for high and extreme severity reports
        const highSeverityReports = data.content.filter(
          report => report.severity === 'HIGH' || report.severity === 'EXTREME'
        );
        setAlerts(highSeverityReports);
      } catch (err) {
        console.error('Error fetching alerts:', err);
        setError(err.response?.data?.message || 'Failed to load alerts. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchAlerts();
    // Refresh alerts every 5 minutes
    const interval = setInterval(fetchAlerts, 5 * 60 * 1000);
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="bg-white rounded shadow p-4">
        <div className="animate-pulse space-y-4">
          <div className="h-4 bg-gray-200 rounded w-1/4"></div>
          <div className="h-20 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-white rounded shadow p-4">
        <div className="text-red-600 flex items-center space-x-2">
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span>{error}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded shadow p-4">
      <h3 className="font-bold text-lg mb-4 text-primary-700">Active Alerts</h3>
      {alerts.length === 0 ? (
        <div className="text-green-600 font-medium">No active flood alerts in your area</div>
      ) : (
        <div className="space-y-4">
          {alerts.map(alert => (
            <div
              key={alert.id}
              className={`p-4 rounded-lg ${
                alert.severity === 'EXTREME'
                  ? 'bg-red-50 border border-red-200'
                  : 'bg-orange-50 border border-orange-200'
              }`}
            >
              <div className="flex items-start justify-between">
                <div>
                  <h4 className="font-semibold text-lg">{alert.title}</h4>
                  <p className="text-gray-600 mt-1">{alert.description}</p>
                  <div className="flex items-center space-x-4 mt-2 text-sm">
                    <span className={`px-2 py-1 rounded ${
                      alert.severity === 'EXTREME'
                        ? 'bg-red-100 text-red-800'
                        : 'bg-orange-100 text-orange-800'
                    }`}>
                      {alert.severity}
                    </span>
                    <span className="text-gray-500">
                      Reported {new Date(alert.createdAt).toLocaleString()}
                    </span>
                  </div>
                </div>
                {alert.imageUrl && (
                  <img
                    src={alert.imageUrl}
                    alt="Flood alert"
                    className="w-20 h-20 object-cover rounded"
                  />
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AlertStatus; 