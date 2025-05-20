import { useState, useEffect } from 'react'
import axios from 'axios'

interface FloodReport {
  id: number
  location: {
    latitude: number
    longitude: number
    address: string
  }
  description: string
  imageUrl: string
  status: string
  reportedAt: string
  reporter: {
    id: number
    username: string
    email: string
  }
}

const AdminPanel = () => {
  const [reports, setReports] = useState<FloodReport[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedReport, setSelectedReport] = useState<FloodReport | null>(null)
  const [verificationNotes, setVerificationNotes] = useState('')

  useEffect(() => {
    fetchReports()
  }, [])

  const fetchReports = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/reports')
      setReports(response.data)
      setLoading(false)
    } catch (err) {
      setError('Failed to fetch reports')
      setLoading(false)
    }
  }

  const handleVerify = async (reportId: number, status: string) => {
    try {
      await axios.put(`http://localhost:8080/api/reports/${reportId}/verify`, {
        status,
        verificationNotes
      })
      fetchReports()
      setSelectedReport(null)
      setVerificationNotes('')
    } catch (err) {
      setError('Failed to verify report')
    }
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="text-center text-red-600">
        <p>{error}</p>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      <h1 className="text-3xl font-bold">Admin Panel</h1>

      {/* Reports List */}
      <div className="card">
        <h2 className="text-2xl font-semibold mb-4">Flood Reports</h2>
        <div className="space-y-4">
          {reports.map((report) => (
            <div
              key={report.id}
              className="border rounded-lg p-4 hover:bg-gray-50 cursor-pointer"
              onClick={() => setSelectedReport(report)}
            >
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="font-semibold">Report #{report.id}</h3>
                  <p className="text-gray-600">{report.location.address}</p>
                  <p className="text-sm text-gray-500">
                    Reported by {report.reporter.username} on{' '}
                    {new Date(report.reportedAt).toLocaleString()}
                  </p>
                </div>
                <span
                  className={`px-2 py-1 rounded text-sm ${
                    report.status === 'PENDING'
                      ? 'bg-yellow-100 text-yellow-800'
                      : report.status === 'VERIFIED'
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  {report.status}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Report Details Modal */}
      {selectedReport && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg max-w-2xl w-full p-6">
            <h2 className="text-2xl font-semibold mb-4">Report Details</h2>
            
            <div className="space-y-4">
              <div>
                <h3 className="font-semibold">Location</h3>
                <p>{selectedReport.location.address}</p>
                <p className="text-sm text-gray-500">
                  Coordinates: {selectedReport.location.latitude},{' '}
                  {selectedReport.location.longitude}
                </p>
              </div>

              <div>
                <h3 className="font-semibold">Description</h3>
                <p>{selectedReport.description}</p>
              </div>

              {selectedReport.imageUrl && (
                <div>
                  <h3 className="font-semibold">Image</h3>
                  <img
                    src={selectedReport.imageUrl}
                    alt="Flood report"
                    className="max-w-full h-auto rounded-lg"
                  />
                </div>
              )}

              <div>
                <h3 className="font-semibold">Verification Notes</h3>
                <textarea
                  value={verificationNotes}
                  onChange={(e) => setVerificationNotes(e.target.value)}
                  className="input h-32"
                  placeholder="Add verification notes..."
                />
              </div>

              <div className="flex justify-end space-x-4">
                <button
                  className="btn btn-secondary"
                  onClick={() => {
                    setSelectedReport(null)
                    setVerificationNotes('')
                  }}
                >
                  Cancel
                </button>
                <button
                  className="btn btn-primary"
                  onClick={() => handleVerify(selectedReport.id, 'VERIFIED')}
                >
                  Verify Report
                </button>
                <button
                  className="btn bg-red-600 text-white hover:bg-red-700"
                  onClick={() => handleVerify(selectedReport.id, 'REJECTED')}
                >
                  Reject Report
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default AdminPanel 