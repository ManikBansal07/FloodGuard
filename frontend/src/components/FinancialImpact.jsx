import React, { useState, useEffect } from 'react';
import weatherApi from '../api/weather';

const FinancialImpact = () => {
  const [riskLevel, setRiskLevel] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const calculateRiskLevel = async () => {
      try {
        setLoading(true);
        setError(null);
        
        // Get user's current location
        const position = await new Promise((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(resolve, reject);
        });
        
        const { latitude, longitude } = position.coords;
        
        // Get real-time flood risk data
        const floodRisk = await weatherApi.getFloodRisk(latitude, longitude);
        
        setRiskLevel({
          level: floodRisk.riskLevel,
          color: getRiskColor(floodRisk.riskLevel),
          message: getRiskMessage(floodRisk),
          factors: floodRisk.factors,
          score: floodRisk.riskScore
        });
      } catch (err) {
        console.error('Error calculating risk level:', err);
        setError(err.response?.data?.message || 'Failed to calculate risk level. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    calculateRiskLevel();
    // Refresh risk assessment every 15 minutes
    const interval = setInterval(calculateRiskLevel, 15 * 60 * 1000);
    return () => clearInterval(interval);
  }, []);

  const getRiskColor = (level) => {
    switch (level) {
      case 'EXTREME': return 'red';
      case 'HIGH': return 'orange';
      case 'MEDIUM': return 'yellow';
      case 'LOW': return 'green';
      default: return 'blue';
    }
  };

  const getRiskMessage = (floodRisk) => {
    const baseMessage = `Current flood risk level: ${floodRisk.riskLevel}`;
    if (floodRisk.factors && floodRisk.factors.length > 0) {
      return `${baseMessage} due to ${floodRisk.factors.join(', ')}`;
    }
    return baseMessage;
  };

  if (loading) {
    return (
      <div className="bg-white rounded shadow p-4">
        <div className="animate-pulse space-y-4">
          <div className="h-4 bg-gray-200 rounded w-1/4"></div>
          <div className="h-32 bg-gray-200 rounded"></div>
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
      <h3 className="font-bold text-lg mb-4 text-primary-700">Financial Risk Assessment</h3>
      <div className={`p-4 rounded-lg bg-${riskLevel.color}-50 border border-${riskLevel.color}-200`}>
        <div className="flex items-center justify-between">
          <div>
            <h4 className="font-semibold text-lg">Current Risk Level: {riskLevel.level}</h4>
            <p className="text-gray-600 mt-1">{riskLevel.message}</p>
            {riskLevel.factors && riskLevel.factors.length > 0 && (
              <div className="mt-2">
                <p className="text-sm font-medium text-gray-700">Risk Factors:</p>
                <ul className="mt-1 text-sm text-gray-600 list-disc list-inside">
                  {riskLevel.factors.map((factor, index) => (
                    <li key={index}>{factor}</li>
                  ))}
                </ul>
              </div>
            )}
          </div>
          <div className={`w-16 h-16 rounded-full bg-${riskLevel.color}-100 flex items-center justify-center`}>
            <span className={`text-${riskLevel.color}-800 text-2xl font-bold`}>
              {riskLevel.level === 'EXTREME' || riskLevel.level === 'HIGH' ? '!' : '✓'}
            </span>
          </div>
        </div>
        <div className="mt-4 text-sm text-gray-500">
          <p>Risk Score: {Math.round(riskLevel.score)}/100</p>
          <p>Last updated: {new Date().toLocaleString()}</p>
        </div>
      </div>
    </div>
  );
};

export default FinancialImpact; 