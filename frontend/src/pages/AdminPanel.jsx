import React, { useState, useEffect, useCallback } from 'react';
import adminApi from '../api/admin';

const AdminPanel = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const pageSize = 10;

  const fetchReports = useCallback(async () => {
    try {
      const data = await adminApi.getAllReports(page, pageSize);
      setReports(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      setError('Failed to load reports');
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [page, pageSize]);

  useEffect(() => {
    fetchReports();
  }, [fetchReports]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleApprove = async (reportId) => {
    try {
      await adminApi.approveReport(reportId);
      fetchReports(); // Refresh the list
    } catch (err) {
      setError('Failed to approve report');
      console.error(err);
    }
  };

  const handleReject = async (reportId) => {
    try {
      await adminApi.rejectReport(reportId);
      fetchReports(); // Refresh the list
    } catch (err) {
      setError('Failed to reject report');
      console.error(err);
    }
  };

  if (loading) return <div className="max-w-4xl mx-auto bg-white p-8 rounded shadow mt-10">Loading reports...</div>;
  if (error) return <div className="max-w-4xl mx-auto bg-white p-8 rounded shadow mt-10 text-red-600">{error}</div>;

  return (
    <div className="max-w-4xl mx-auto bg-white p-8 rounded shadow mt-10">
      <h2 className="text-2xl font-bold mb-6 text-primary-700">Admin Panel</h2>
      <div className="mb-4">Manage community flood reports</div>
      
      <div className="space-y-4">
        {reports.map(report => (
          <div key={report.id} className="border rounded-lg p-4">
            <div className="flex items-start justify-between">
              <div>
                <h3 className="font-semibold text-lg">{report.title}</h3>
                <p className="text-gray-600 mt-1">{report.description}</p>
                <div className="flex items-center space-x-4 mt-2 text-sm">
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
                  <span className="text-gray-500">
                    Reported by: {report.reporterUsername}
                  </span>
                </div>
              </div>
              {report.status === 'PENDING' && (
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleApprove(report.id)}
                    className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                  >
                    Approve
                  </button>
                  <button
                    onClick={() => handleReject(report.id)}
                    className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                  >
                    Reject
                  </button>
                </div>
              )}
            </div>
            {report.imageUrl && (
              <img src={report.imageUrl} alt="Flood report" className="mt-4 max-w-sm rounded" />
            )}
          </div>
        ))}
      </div>

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
    </div>
  );
};

export default AdminPanel; 