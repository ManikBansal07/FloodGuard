import React, { useState, useEffect } from 'react';
import reportsApi from '../api/reports';

const ReportsFeed = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const pageSize = 10;

  useEffect(() => {
    const fetchReports = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await reportsApi.getReports(page, pageSize);
        setReports(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
      } catch (err) {
        console.error('Error fetching reports:', err);
        setError(err.response?.data?.message || 'Failed to load reports. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchReports();
  }, [page]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  if (loading) {
    return (
      <div className="bg-white rounded shadow p-4">
        <div className="animate-pulse space-y-4">
          <div className="h-4 bg-gray-200 rounded w-1/4"></div>
          {[...Array(3)].map((_, i) => (
            <div key={i} className="flex space-x-4">
              <div className="w-20 h-20 bg-gray-200 rounded"></div>
              <div className="flex-1 space-y-2">
                <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                <div className="h-4 bg-gray-200 rounded w-1/4"></div>
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-white rounded shadow p-4">
        <div className="text-red-600 flex items-center space-x-2">
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span>{error}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded shadow p-4">
      <h3 className="font-bold text-lg mb-4 text-primary-700">Community Reports</h3>
      {reports.length === 0 ? (
        <div className="text-gray-500">No reports yet. Be the first to report a flood in your area!</div>
      ) : (
        <>
          <ul className="divide-y divide-gray-200">
            {reports.map(report => (
              <li key={report.id} className="py-4">
                <div className="flex items-start space-x-4">
                  {report.imageUrl && (
                    <img src={report.imageUrl} alt="Flood report" className="w-20 h-20 object-cover rounded" />
                  )}
                  <div className="flex-1">
                    <h4 className="font-semibold">{report.title}</h4>
                    <p className="text-gray-600 text-sm mt-1">{report.description}</p>
                    <div className="flex items-center space-x-4 mt-2 text-sm text-gray-500">
                      <span className={`px-2 py-1 rounded ${
                        report.severity === 'LOW' ? 'bg-green-100 text-green-800' :
                        report.severity === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                        report.severity === 'HIGH' ? 'bg-orange-100 text-orange-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {report.severity}
                      </span>
                      <span className={`px-2 py-1 rounded ${
                        report.status === 'VERIFIED' ? 'bg-green-100 text-green-800' :
                        report.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {report.status}
                      </span>
                      <span>{new Date(report.createdAt).toLocaleDateString()}</span>
                    </div>
                  </div>
                </div>
              </li>
            ))}
          </ul>
          
          {/* Pagination */}
          <div className="mt-6 flex items-center justify-between">
            <div className="text-sm text-gray-500">
              Showing {page * pageSize + 1} to {Math.min((page + 1) * pageSize, totalElements)} of {totalElements} reports
            </div>
            <div className="flex space-x-2">
              <button
                onClick={() => handlePageChange(page - 1)}
                disabled={page === 0}
                className={`px-3 py-1 rounded ${
                  page === 0
                    ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                    : 'bg-primary-100 text-primary-700 hover:bg-primary-200'
                }`}
              >
                Previous
              </button>
              <button
                onClick={() => handlePageChange(page + 1)}
                disabled={page === totalPages - 1}
                className={`px-3 py-1 rounded ${
                  page === totalPages - 1
                    ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                    : 'bg-primary-100 text-primary-700 hover:bg-primary-200'
                }`}
              >
                Next
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default ReportsFeed; 