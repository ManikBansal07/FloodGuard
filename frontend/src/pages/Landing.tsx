import { Link } from 'react-router-dom'

const Landing = () => {
  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <div className="container mx-auto px-4 py-16">
        <div className="text-center mb-16">
          <h1 className="text-4xl md:text-5xl font-bold mb-6">
            Protect Your Community from Floods
          </h1>
          <p className="text-xl text-text-gray max-w-2xl mx-auto mb-8">
            FloodGuard uses advanced AI to predict and monitor flood risks, helping communities stay safe and prepared.
          </p>
          <Link to="/login" className="btn">
            Get Started
          </Link>
        </div>

        {/* Features Section */}
        <div className="mb-16">
          <h2 className="text-2xl font-bold mb-8 text-center">Our Magic Solution</h2>
          <div className="flex flex-wrap justify-center gap-4">
            <div className="feature-box">
              <div className="icon-box">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
              </div>
              <div className="feature-content">
                <h3 className="text-lg font-semibold">Real-time Monitoring</h3>
                <p className="text-sm text-text-gray">Instant flood condition updates</p>
              </div>
            </div>

            <div className="feature-box">
              <div className="icon-box">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </div>
              <div className="feature-content">
                <h3 className="text-lg font-semibold">Smart Predictions</h3>
                <p className="text-sm text-text-gray">AI-powered risk assessment</p>
              </div>
            </div>

            <div className="feature-box">
              <div className="icon-box">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
              </div>
              <div className="feature-content">
                <h3 className="text-lg font-semibold">Community Reports</h3>
                <p className="text-sm text-text-gray">Real-time community updates</p>
              </div>
            </div>
          </div>
        </div>

        {/* How It Works Section */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="card">
            <div className="icon-box mb-4">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold mb-2">Create an Account</h3>
            <p className="text-text-gray">
              Sign up for free and get access to all FloodGuard features.
            </p>
          </div>

          <div className="card">
            <div className="icon-box mb-4">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold mb-2">Monitor Your Area</h3>
            <p className="text-text-gray">
              Get real-time updates and predictions for your location.
            </p>
          </div>

          <div className="card">
            <div className="icon-box mb-4">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold mb-2">Stay Informed</h3>
            <p className="text-text-gray">
              Receive alerts and updates about potential flood risks.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Landing 