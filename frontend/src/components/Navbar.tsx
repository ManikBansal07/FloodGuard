import { Link } from 'react-router-dom'
import { useState } from 'react'

interface NavbarProps {
  isAuthenticated: boolean
  setIsAuthenticated: (value: boolean) => void
}

const Navbar = ({ isAuthenticated, setIsAuthenticated }: NavbarProps) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false)

  const handleLogout = () => {
    localStorage.removeItem('token')
    setIsAuthenticated(false)
  }

  return (
    <nav className="navbar">
      <div className="container">
        <div className="flex justify-between items-center">
          <Link to="/" className="text-2xl font-bold text-water-blue">
            FloodGuard
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <Link to="/dashboard" className="nav-link">
                  Dashboard
                </Link>
                <Link to="/report" className="nav-link">
                  Report Flood
                </Link>
                <Link to="/admin" className="nav-link">
                  Admin Panel
                </Link>
                <button
                  onClick={handleLogout}
                  className="btn btn-secondary"
                >
                  Logout
                </button>
              </>
            ) : (
              <Link to="/login" className="btn btn-primary">
                Login
              </Link>
            )}
          </div>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
          >
            <svg
              className="icon"
              fill="none"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              {isMenuOpen ? (
                <path d="M6 18L18 6M6 6l12 12" />
              ) : (
                <path d="M4 6h16M4 12h16M4 18h16" />
              )}
            </svg>
          </button>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="md:hidden py-4">
            {isAuthenticated ? (
              <div className="flex flex-col space-y-4">
                <Link to="/dashboard" className="nav-link">
                  Dashboard
                </Link>
                <Link to="/report" className="nav-link">
                  Report Flood
                </Link>
                <Link to="/admin" className="nav-link">
                  Admin Panel
                </Link>
                <button
                  onClick={handleLogout}
                  className="btn btn-secondary w-full"
                >
                  Logout
                </button>
              </div>
            ) : (
              <Link to="/login" className="btn btn-primary w-full">
                Login
              </Link>
            )}
          </div>
        )}
      </div>
    </nav>
  )
}

export default Navbar 