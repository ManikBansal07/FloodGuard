import React from 'react';
import { Link } from 'react-router-dom';

const LandingPage = () => (
  <div className="space-y-24">
    {/* Hero Section with Wave Background */}
    <section className="relative overflow-hidden bg-gradient-primary text-white rounded-3xl shadow-soft">
      <div className="absolute inset-0 bg-black opacity-20"></div>
      <div className="absolute bottom-0 left-0 right-0">
        <svg className="w-full h-24 text-white" viewBox="0 0 1440 100" preserveAspectRatio="none">
          <path
            fill="currentColor"
            d="M0,32L48,37.3C96,43,192,53,288,58.7C384,64,480,64,576,58.7C672,53,768,43,864,42.7C960,43,1056,53,1152,53.3C1248,53,1344,43,1392,37.3L1440,32L1440,100L1392,100C1344,100,1248,100,1152,100C1056,100,960,100,864,100C768,100,672,100,576,100C480,100,384,100,288,100C192,100,96,100,48,100L0,100Z"
          />
        </svg>
      </div>
      <div className="relative px-8 py-24 sm:px-12 lg:px-16">
        <div className="max-w-3xl">
          <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold mb-6 animate-fadeIn">
            Protect Your Community from Flood Risks
          </h1>
          <p className="text-xl sm:text-2xl mb-8 text-primary-100 animate-fadeIn" style={{ animationDelay: '0.2s' }}>
            Real-time flood monitoring, AI-powered predictions, and instant alerts to keep you safe and informed.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 animate-fadeIn" style={{ animationDelay: '0.4s' }}>
            <Link
              to="/register"
              className="inline-flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-full text-primary-600 bg-white hover:bg-primary-50 transition-all duration-300 shadow-lg hover:shadow-xl hover:scale-105"
            >
              Get Started
            </Link>
            <Link
              to="/dashboard"
              className="inline-flex items-center justify-center px-8 py-3 border-2 border-white text-base font-medium rounded-full text-white hover:bg-white hover:text-primary-600 transition-all duration-300 hover:scale-105"
            >
              View Dashboard
            </Link>
          </div>
        </div>
      </div>
    </section>

    {/* The Issue Section */}
    <section className="bg-white rounded-2xl shadow-soft p-8 sm:p-12 transition-all duration-300 hover:scale-[1.02] hover:shadow-xl">
      <div className="max-w-3xl mx-auto">
        <div className="flex items-center space-x-3 mb-6">
          <div className="bg-primary-100 rounded-full p-2">
            <svg className="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          </div>
          <h2 className="text-3xl sm:text-4xl font-bold text-primary-700">The Issue</h2>
        </div>
        <p className="text-lg sm:text-xl text-gray-700 leading-relaxed">
          Floods are increasing in frequency and severity due to climate change, urbanization, and unpredictable weather. 
          Communities and businesses face growing risks to life, property, and economic stability.
        </p>
      </div>
    </section>

    {/* Our Solution Section */}
    <section className="bg-gradient-to-br from-primary-50 to-white rounded-2xl shadow-soft p-8 sm:p-12 transition-all duration-300 hover:scale-[1.02] hover:shadow-xl">
      <div className="max-w-3xl mx-auto">
        <div className="flex items-center space-x-3 mb-6">
          <div className="bg-primary-100 rounded-full p-2">
            <svg className="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
            </svg>
          </div>
          <h2 className="text-3xl sm:text-4xl font-bold text-primary-700">Our Solution</h2>
        </div>
        <p className="text-lg sm:text-xl text-gray-700 leading-relaxed">
          FloodGuard combines real-time data, AI-powered prediction, and community reporting to deliver accurate flood risk insights, 
          instant alerts, and actionable financial risk assessments.
        </p>
      </div>
    </section>

    {/* How It Works Section */}
    <section className="bg-white rounded-2xl shadow-soft p-8 sm:p-12">
      <div className="max-w-4xl mx-auto">
        <div className="flex items-center justify-center space-x-3 mb-12">
          <div className="bg-primary-100 rounded-full p-2">
            <svg className="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
            </svg>
          </div>
          <h2 className="text-3xl sm:text-4xl font-bold text-primary-700 text-center">How It Works</h2>
        </div>
        <div className="grid md:grid-cols-3 gap-8">
          <div className="flex flex-col items-center p-6 rounded-xl bg-primary-50 transition-all duration-300 hover:scale-105 hover:shadow-lg">
            <div className="bg-primary-500 text-white rounded-full w-16 h-16 flex items-center justify-center mb-6 text-2xl font-bold shadow-lg">
              1
            </div>
            <h3 className="text-xl font-semibold mb-4 text-primary-700">Real-time Monitoring</h3>
            <p className="text-gray-600 text-center">
              Advanced sensors and AI models analyze weather patterns and historical data to predict flood risks.
            </p>
          </div>
          <div className="flex flex-col items-center p-6 rounded-xl bg-primary-50 transition-all duration-300 hover:scale-105 hover:shadow-lg">
            <div className="bg-primary-500 text-white rounded-full w-16 h-16 flex items-center justify-center mb-6 text-2xl font-bold shadow-lg">
              2
            </div>
            <h3 className="text-xl font-semibold mb-4 text-primary-700">Instant Alerts</h3>
            <p className="text-gray-600 text-center">
              Receive immediate notifications via SMS or email when your location is at risk, enabling quick action.
            </p>
          </div>
          <div className="flex flex-col items-center p-6 rounded-xl bg-primary-50 transition-all duration-300 hover:scale-105 hover:shadow-lg">
            <div className="bg-primary-500 text-white rounded-full w-16 h-16 flex items-center justify-center mb-6 text-2xl font-bold shadow-lg">
              3
            </div>
            <h3 className="text-xl font-semibold mb-4 text-primary-700">Risk Assessment</h3>
            <p className="text-gray-600 text-center">
              Get detailed financial risk analysis including property damage estimates and insurance insights.
            </p>
          </div>
        </div>
      </div>
    </section>

    {/* CTA Section with Wave Background */}
    <section className="relative overflow-hidden bg-gradient-primary text-white rounded-2xl shadow-soft p-8 sm:p-12 text-center">
      <div className="absolute top-0 left-0 right-0">
        <svg className="w-full h-24 text-white transform rotate-180" viewBox="0 0 1440 100" preserveAspectRatio="none">
          <path
            fill="currentColor"
            d="M0,32L48,37.3C96,43,192,53,288,58.7C384,64,480,64,576,58.7C672,53,768,43,864,42.7C960,43,1056,53,1152,53.3C1248,53,1344,43,1392,37.3L1440,32L1440,100L1392,100C1344,100,1248,100,1152,100C1056,100,960,100,864,100C768,100,672,100,576,100C480,100,384,100,288,100C192,100,96,100,48,100L0,100Z"
          />
        </svg>
      </div>
      <div className="relative max-w-3xl mx-auto">
        <h2 className="text-3xl sm:text-4xl font-bold mb-6">Ready to Protect Your Community?</h2>
        <p className="text-xl mb-8 text-primary-100">
          Join FloodGuard today and stay one step ahead of flood risks.
        </p>
        <Link
          to="/register"
          className="inline-flex items-center justify-center px-8 py-3 border-2 border-white text-base font-medium rounded-full text-white hover:bg-white hover:text-primary-600 transition-all duration-300 hover:scale-105"
        >
          Get Started Now
        </Link>
      </div>
    </section>
  </div>
);

export default LandingPage; 