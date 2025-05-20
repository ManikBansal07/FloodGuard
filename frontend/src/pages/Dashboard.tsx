import { useState, useEffect } from 'react'
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import axios from 'axios'

interface FloodPrediction {
  id: number
  location: {
    latitude: number
    longitude: number
  }
  riskLevel: number
  riskCategory: string
  estimatedPropertyDamage: number
  estimatedBusinessInterruption: number
  estimatedInsurancePayout: number
  predictionTime: string
  validUntil: string
}

const Dashboard = () => {
  const [predictions, setPredictions] = useState<FloodPrediction[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchPredictions = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/predictions')
        setPredictions(response.data)
        setLoading(false)
      } catch (err) {
        setError('Failed to fetch flood predictions')
        setLoading(false)
      }
    }

    fetchPredictions()
  }, [])

  const getRiskColor = (riskLevel: number) => {
    if (riskLevel < 0.2) return 'bg-risk-low'
    if (riskLevel < 0.4) return 'bg-risk-moderate'
    if (riskLevel < 0.6) return 'bg-risk-high'
    if (riskLevel < 0.8) return 'bg-risk-severe'
    return 'bg-risk-extreme'
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
      <h1 className="text-3xl font-bold">Flood Risk Dashboard</h1>

      {/* Risk Map */}
      <div className="card h-[600px]">
        <MapContainer
          center={[51.505, -0.09]}
          zoom={13}
          style={{ height: '100%', width: '100%' }}
        >
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          {predictions.map((prediction) => (
            <Marker
              key={prediction.id}
              position={[prediction.location.latitude, prediction.location.longitude]}
            >
              <Popup>
                <div className="p-2">
                  <h3 className="font-semibold mb-2">Flood Risk Assessment</h3>
                  <p>Risk Level: {prediction.riskLevel.toFixed(2)}</p>
                  <p>Category: {prediction.riskCategory}</p>
                  <p>Property Damage: ${prediction.estimatedPropertyDamage.toLocaleString()}</p>
                  <p>Business Impact: ${prediction.estimatedBusinessInterruption.toLocaleString()}</p>
                  <p>Insurance Payout: ${prediction.estimatedInsurancePayout.toLocaleString()}</p>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>

      {/* Risk Summary */}
      <div className="grid md:grid-cols-3 gap-6">
        {predictions.map((prediction) => (
          <div
            key={prediction.id}
            className={`card ${getRiskColor(prediction.riskLevel)}`}
          >
            <h3 className="text-xl font-semibold mb-2">Location Risk</h3>
            <p className="mb-4">Risk Level: {prediction.riskLevel.toFixed(2)}</p>
            <div className="space-y-2">
              <p>Property Damage: ${prediction.estimatedPropertyDamage.toLocaleString()}</p>
              <p>Business Impact: ${prediction.estimatedBusinessInterruption.toLocaleString()}</p>
              <p>Insurance Payout: ${prediction.estimatedInsurancePayout.toLocaleString()}</p>
            </div>
            <p className="mt-4 text-sm">
              Valid until: {new Date(prediction.validUntil).toLocaleString()}
            </p>
          </div>
        ))}
      </div>

      {/* Alert Status */}
      <div className="card">
        <h2 className="text-2xl font-semibold mb-4">Alert Status</h2>
        <div className="space-y-4">
          <div className="flex items-center justify-between p-4 bg-yellow-50 rounded-lg">
            <div>
              <h3 className="font-semibold">High Risk Alert</h3>
              <p className="text-gray-600">Location: Downtown Area</p>
            </div>
            <button className="btn btn-primary">View Details</button>
          </div>
          <div className="flex items-center justify-between p-4 bg-green-50 rounded-lg">
            <div>
              <h3 className="font-semibold">Low Risk Area</h3>
              <p className="text-gray-600">Location: Uptown District</p>
            </div>
            <button className="btn btn-secondary">View Details</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard 