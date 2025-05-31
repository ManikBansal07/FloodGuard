import React from 'react';
import MapView from '../components/MapView';
import AlertStatus from '../components/AlertStatus';
import FinancialImpact from '../components/FinancialImpact';
import ReportsFeed from '../components/ReportsFeed';

const Dashboard = () => (
  <div className="space-y-8">
    <div className="bg-white rounded-2xl shadow-soft p-6">
      <h1 className="text-3xl font-bold text-primary-700 mb-6">Flood Risk Dashboard</h1>
      <div className="grid md:grid-cols-3 gap-8">
        <div className="md:col-span-2">
          <div className="bg-primary-50 rounded-xl p-4 shadow-inner">
            <MapView />
          </div>
        </div>
        <div className="space-y-6">
          <div className="bg-white rounded-xl shadow-soft p-6 transition-transform duration-300 hover:scale-[1.02]">
            <AlertStatus />
          </div>
          <div className="bg-white rounded-xl shadow-soft p-6 transition-transform duration-300 hover:scale-[1.02]">
            <FinancialImpact />
          </div>
        </div>
      </div>
    </div>

    <div className="bg-white rounded-2xl shadow-soft p-6">
      <h2 className="text-2xl font-bold text-primary-700 mb-6">Recent Community Reports</h2>
      <div className="bg-primary-50 rounded-xl p-4 shadow-inner">
        <ReportsFeed />
      </div>
    </div>
  </div>
);

export default Dashboard; 