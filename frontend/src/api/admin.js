import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const adminApi = {
  async getAllReports(page = 0, size = 10) {
    const response = await axios.get(`${API_URL}/admin/reports`, {
      params: { page, size }
    });
    return response.data;
  },

  async approveReport(reportId) {
    const response = await axios.post(`${API_URL}/admin/reports/${reportId}/approve`);
    return response.data;
  },

  async rejectReport(reportId) {
    const response = await axios.post(`${API_URL}/admin/reports/${reportId}/reject`);
    return response.data;
  }
};

export default adminApi; 