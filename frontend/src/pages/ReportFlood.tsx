import { useState } from 'react'
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import axios from 'axios'

interface Location {
  latitude: number
  longitude: number
  address: string
}

const ReportFlood = () => {
  const [location, setLocation] = useState<Location>({
    latitude: 51.505,
    longitude: -0.09,
    address: ''
  })
  const [description, setDescription] = useState('')
  const [image, setImage] = useState<File | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState(false)

  const handleMapClick = (e: any) => {
    setLocation({
      ...location,
      latitude: e.latlng.lat,
      longitude: e.latlng.lng
    })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    setSuccess(false)

    try {
      const formData = new FormData()
      formData.append('location', JSON.stringify(location))
      formData.append('description', description)
      if (image) {
        formData.append('image', image)
      }

      await axios.post('http://localhost:8080/api/reports', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })

      setSuccess(true)
      setDescription('')
      setImage(null)
    } catch (err) {
      setError('Failed to submit report. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <h1 className="text-3xl font-bold">Report a Flood</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Map Selection */}
        <div className="card h-[400px]">
          <MapContainer
            center={[location.latitude, location.longitude]}
            zoom={13}
            style={{ height: '100%', width: '100%' }}
          >
            <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            />
            <Marker position={[location.latitude, location.longitude]} />
            <MapClickHandler onMapClick={handleMapClick} />
          </MapContainer>
        </div>

        {/* Location Details */}
        <div className="card">
          <h2 className="text-xl font-semibold mb-4">Location Details</h2>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Address
              </label>
              <input
                type="text"
                value={location.address}
                onChange={(e) => setLocation({ ...location, address: e.target.value })}
                className="input"
                placeholder="Enter the address or location description"
              />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Latitude
                </label>
                <input
                  type="number"
                  value={location.latitude}
                  onChange={(e) => setLocation({ ...location, latitude: parseFloat(e.target.value) })}
                  className="input"
                  step="any"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Longitude
                </label>
                <input
                  type="number"
                  value={location.longitude}
                  onChange={(e) => setLocation({ ...location, longitude: parseFloat(e.target.value) })}
                  className="input"
                  step="any"
                />
              </div>
            </div>
          </div>
        </div>

        {/* Description */}
        <div className="card">
          <h2 className="text-xl font-semibold mb-4">Flood Details</h2>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Description
              </label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="input h-32"
                placeholder="Describe the flood situation..."
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Upload Image
              </label>
              <input
                type="file"
                accept="image/*"
                onChange={(e) => setImage(e.target.files?.[0] || null)}
                className="input"
              />
            </div>
          </div>
        </div>

        {/* Submit Button */}
        <div className="flex justify-end">
          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? 'Submitting...' : 'Submit Report'}
          </button>
        </div>

        {/* Status Messages */}
        {error && (
          <div className="text-red-600 text-center">
            <p>{error}</p>
          </div>
        )}
        {success && (
          <div className="text-green-600 text-center">
            <p>Report submitted successfully!</p>
          </div>
        )}
      </form>
    </div>
  )
}

// Map click handler component
const MapClickHandler = ({ onMapClick }: { onMapClick: (e: any) => void }) => {
  useMapEvents({
    click: onMapClick
  })
  return null
}

export default ReportFlood 