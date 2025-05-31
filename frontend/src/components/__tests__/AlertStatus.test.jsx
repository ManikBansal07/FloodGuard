import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import AlertStatus from '../AlertStatus';
import reportsApi from '../../api/reports';

// Mock the reports API
jest.mock('../../api/reports');

describe('AlertStatus', () => {
    const mockReports = {
        content: [
            {
                id: 1,
                title: 'Severe Flooding',
                description: 'Major flooding in downtown area',
                severity: 'HIGH',
                status: 'VERIFIED',
                createdAt: '2024-03-20T10:00:00Z',
                imageUrl: 'test.jpg'
            },
            {
                id: 2,
                title: 'Extreme Flood Warning',
                description: 'Critical flooding situation',
                severity: 'EXTREME',
                status: 'VERIFIED',
                createdAt: '2024-03-20T11:00:00Z'
            }
        ]
    };

    beforeEach(() => {
        reportsApi.getReports.mockResolvedValue(mockReports);
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('renders loading state initially', () => {
        render(<AlertStatus />);
        expect(screen.getByText('Loading alerts...')).toBeInTheDocument();
    });

    it('renders alerts when data is loaded', async () => {
        render(<AlertStatus />);

        await waitFor(() => {
            expect(screen.getByText('Active Alerts')).toBeInTheDocument();
            expect(screen.getByText('Severe Flooding')).toBeInTheDocument();
            expect(screen.getByText('Extreme Flood Warning')).toBeInTheDocument();
        });
    });

    it('renders no alerts message when there are no high severity reports', async () => {
        reportsApi.getReports.mockResolvedValue({ content: [] });
        render(<AlertStatus />);

        await waitFor(() => {
            expect(screen.getByText('No active flood alerts in your area')).toBeInTheDocument();
        });
    });

    it('handles API error gracefully', async () => {
        reportsApi.getReports.mockRejectedValue(new Error('API Error'));
        render(<AlertStatus />);

        await waitFor(() => {
            expect(screen.getByText('Failed to load alerts')).toBeInTheDocument();
        });
    });

    it('displays correct severity styling', async () => {
        render(<AlertStatus />);

        await waitFor(() => {
            const highSeverityAlert = screen.getByText('Severe Flooding').closest('div');
            const extremeSeverityAlert = screen.getByText('Extreme Flood Warning').closest('div');

            expect(highSeverityAlert).toHaveClass('bg-orange-50');
            expect(extremeSeverityAlert).toHaveClass('bg-red-50');
        });
    });

    it('displays image when available', async () => {
        render(<AlertStatus />);

        await waitFor(() => {
            const image = screen.getByAltText('Flood alert');
            expect(image).toBeInTheDocument();
            expect(image).toHaveAttribute('src', 'test.jpg');
        });
    });
}); 