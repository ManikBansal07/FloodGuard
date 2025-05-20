import { Link } from 'react-router-dom'

const LandingPage = () => {
  return (
    <div className="space-y-24">
      {/* Hero Section */}
      <section className="text-center py-20">
        <h1 className="text-5xl font-bold text-gray-900 mb-6">
          Protect Your Community from Floods
        </h1>
        <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
          FloodGuard uses advanced AI to predict and monitor flood risks, helping you stay safe and prepared.
        </p>
        <Link to="/dashboard" className="btn btn-primary text-lg px-8 py-3">
          Get Started
        </Link>
      </section>

      {/* The Issue Section */}
      <section className="py-16 bg-white rounded-lg shadow-md">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">The Issue</h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="card">
              <h3 className="text-xl font-semibold mb-4">Increasing Frequency</h3>
              <p className="text-gray-600">
                Climate change has led to more frequent and severe flooding events worldwide.
              </p>
            </div>
            <div className="card">
              <h3 className="text-xl font-semibold mb-4">Economic Impact</h3>
              <p className="text-gray-600">
                Floods cause billions in property damage and business interruption annually.
              </p>
            </div>
            <div className="card">
              <h3 className="text-xl font-semibold mb-4">Lack of Early Warning</h3>
              <p className="text-gray-600">
                Many communities lack effective early warning systems for flood events.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Our Solution Section */}
      <section className="py-16">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">Our Magic Solution</h2>
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <h3 className="text-2xl font-semibold mb-4">Intelligent Flood Prediction</h3>
              <p className="text-gray-600 mb-6">
                FloodGuard combines real-time weather data, historical patterns, and machine learning to provide accurate flood predictions.
              </p>
              <ul className="space-y-4">
                <li className="flex items-center">
                  <svg className="h-6 w-6 text-primary-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                  </svg>
                  Real-time risk assessment
                </li>
                <li className="flex items-center">
                  <svg className="h-6 w-6 text-primary-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                  </svg>
                  Location-based alerts
                </li>
                <li className="flex items-center">
                  <svg className="h-6 w-6 text-primary-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                  </svg>
                  Financial impact analysis
                </li>
              </ul>
            </div>
            <div className="bg-gray-200 rounded-lg h-96 flex items-center justify-center">
              <span className="text-gray-500">Interactive Map Preview</span>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="py-16 bg-white rounded-lg shadow-md">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">How It Works</h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-primary-100 rounded-full h-16 w-16 flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-primary-600">1</span>
              </div>
              <h3 className="text-xl font-semibold mb-4">Prediction</h3>
              <p className="text-gray-600">
                Our AI models analyze weather data and historical patterns to predict flood risks.
              </p>
            </div>
            <div className="text-center">
              <div className="bg-primary-100 rounded-full h-16 w-16 flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-primary-600">2</span>
              </div>
              <h3 className="text-xl font-semibold mb-4">Alert</h3>
              <p className="text-gray-600">
                Receive timely alerts via SMS or email when risks are detected in your area.
              </p>
            </div>
            <div className="text-center">
              <div className="bg-primary-100 rounded-full h-16 w-16 flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-primary-600">3</span>
              </div>
              <h3 className="text-xl font-semibold mb-4">Financial Insight</h3>
              <p className="text-gray-600">
                Get detailed financial risk assessments to help with insurance and planning.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="text-center py-16">
        <h2 className="text-3xl font-bold mb-6">Ready to Protect Your Community?</h2>
        <p className="text-xl text-gray-600 mb-8">
          Join FloodGuard today and stay one step ahead of flood risks.
        </p>
        <Link to="/dashboard" className="btn btn-primary text-lg px-8 py-3">
          Get Started Now
        </Link>
      </section>
    </div>
  )
}

export default LandingPage 