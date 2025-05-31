import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Icon } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import reportsApi from '../api/reports';

const severityColors = {
  LOW: 'green',
  MEDIUM: 'yellow',
  HIGH: 'orange',
  EXTREME: 'red'
};

const customIcon = (severity) => new Icon({
  iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${severityColors[severity]}.png`,
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

const MapView = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const data = await reportsApi.getReports();
        setReports(data.content);
      } catch (err) {
        setError('Failed to load flood reports');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchReports();
  }, []);

  if (loading) return <div className="h-96 bg-gray-100 rounded-lg flex items-center justify-center">Loading map...</div>;
  if (error) return <div className="h-96 bg-red-50 rounded-lg flex items-center justify-center text-red-600">{error}</div>;

  return (
    <div className="rounded-lg shadow overflow-hidden h-96">
      <MapContainer center={[37.7749, -122.4194]} zoom={10} style={{ height: '100%', width: '100%' }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="&copy; OpenStreetMap contributors"
        />
        {reports.map(report => (
          <Marker
            key={report.id}
            position={[report.latitude, report.longitude]}
            icon={customIcon(report.severity)}
          >
            <Popup>
              <div className="p-2">
                <h3 className="font-bold">{report.title}</h3>
                <p className="text-sm">{report.description}</p>
                <p className="text-sm mt-2">
                  <span className="font-semibold">Severity:</span> {report.severity}
                </p>
                <p className="text-sm">
                  <span className="font-semibold">Status:</span> {report.status}
                </p>
                {report.imageUrl && (
                  <img src={report.imageUrl} alt="Flood report" className="mt-2 max-w-full h-auto" />
                )}
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default MapView; 