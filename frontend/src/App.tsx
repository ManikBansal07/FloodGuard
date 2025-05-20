import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { Suspense, lazy, useState, useEffect } from 'react'
import Navbar from './components/Navbar'
import LoadingSpinner from './components/LoadingSpinner'
import './styles/custom.css'

// Lazy load pages
const LandingPage = lazy(() => import('./pages/LandingPage'))
const Dashboard = lazy(() => import('./pages/Dashboard'))
const ReportFlood = lazy(() => import('./pages/ReportFlood'))
const AdminPanel = lazy(() => import('./pages/AdminPanel'))
const Login = lazy(() => import('./pages/Login'))

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem('token')
    setIsAuthenticated(!!token)
    setIsLoading(false)
  }, [])

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="loading-spinner"></div>
      </div>
    )
  }

  return (
    <Router>
      <div className="min-h-screen bg-water-white">
        <Navbar isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />
        <main className="container mx-auto px-4 py-8">
          <Suspense fallback={<LoadingSpinner />}>
            <Routes>
              <Route path="/" element={<LandingPage />} />
              <Route
                path="/login"
                element={
                  !isAuthenticated ? (
                    <Login setIsAuthenticated={setIsAuthenticated} />
                  ) : (
                    <Navigate to="/dashboard" replace />
                  )
                }
              />
              <Route
                path="/dashboard"
                element={
                  isAuthenticated ? (
                    <Dashboard />
                  ) : (
                    <Navigate to="/login" replace />
                  )
                }
              />
              <Route
                path="/report"
                element={
                  isAuthenticated ? (
                    <ReportFlood />
                  ) : (
                    <Navigate to="/login" replace />
                  )
                }
              />
              <Route
                path="/admin"
                element={
                  isAuthenticated ? (
                    <AdminPanel />
                  ) : (
                    <Navigate to="/login" replace />
                  )
                }
              />
            </Routes>
          </Suspense>
        </main>
      </div>
    </Router>
  )
}

export default App
