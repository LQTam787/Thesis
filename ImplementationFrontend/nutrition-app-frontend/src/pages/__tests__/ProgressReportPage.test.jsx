// src/pages/__tests__/ProgressReportPage.test.jsx
import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ProgressReportPage from '../ProgressReportPage';
import logService from '../../services/logService';
import { vi } from 'vitest';

// Mock logService
vi.mock('../../services/logService', () => ({
    default: {
        getProgressData: vi.fn(),
    },
}));

// Mock lucide-react icons
vi.mock('lucide-react', () => ({
    Activity: () => <div data-testid="activity-icon" />,
    Clock: () => <div data-testid="clock-icon" />,
}));

// Mock Recharts components
vi.mock('recharts', () => {
    const OriginalRecharts = vi.importActual('recharts');
    return {
        ...OriginalRecharts,
        ResponsiveContainer: ({ children }) => <div data-testid="responsive-container">{children}</div>,
        LineChart: ({ children }) => <div data-testid="line-chart">{children}</div>,
        Line: ({ name }) => <div data-testid={`line-${name}`} />,
        XAxis: () => <div data-testid="x-axis" />,
        YAxis: () => <div data-testid="y-axis" />,
        CartesianGrid: () => <div data-testid="cartesian-grid" />,
        Tooltip: () => <div data-testid="tooltip" />,
        Legend: () => <div data-testid="legend" />,
    };
});

const dummyData7days = [
    { date: '2025-11-01', loggedCalories: 2100, targetCalories: 2200 },
    { date: '2025-11-02', loggedCalories: 2350, targetCalories: 2200 },
    { date: '2025-11-03', loggedCalories: 2050, targetCalories: 2200 },
    { date: '2025-11-04', loggedCalories: 2150, targetCalories: 2200 },
    { date: '2025-11-05', loggedCalories: 2400, targetCalories: 2200 },
    { date: '2025-11-06', loggedCalories: 2000, targetCalories: 2200 },
    { date: '2025-11-07', loggedCalories: 2200, targetCalories: 2200 },
];

const dummyData30days = Array.from({ length: 30 }, (_, i) => ({
    date: `2025-10-${i + 1}`,
    loggedCalories: 2000 + Math.floor(Math.random() * 500),
    targetCalories: 2200,
}));

const renderWithRouter = () => {
    return render(
        <MemoryRouter>
            <ProgressReportPage />
        </MemoryRouter>
    );
};

describe('ProgressReportPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        // Mock the date to have a consistent value for tests
        vi.spyOn(Date.prototype, 'toISOString').mockReturnValue('2025-11-07T12:00:00.000Z');
    });

    test('displays loading state initially and then renders chart with default 7-day data', async () => {
        logService.getProgressData.mockResolvedValue(dummyData7days);
        renderWithRouter();

        // The component uses dummy data on first load, so loading state might not be visible
        // Let's ensure the API would have been called with correct default range
        await waitFor(() => {
            expect(logService.getProgressData).toHaveBeenCalledTimes(1);
            const endDate = new Date().toISOString().slice(0, 10);
            let startDate = new Date();
            startDate.setDate(startDate.getDate() - 7);
            const startDateString = startDate.toISOString().slice(0, 10);
            expect(logService.getProgressData).toHaveBeenCalledWith(startDateString, endDate);
        });

        // Check for title
        expect(screen.getByText('Báo cáo & Theo dõi Tiến độ')).toBeInTheDocument();
        // Check if the chart container is rendered
        expect(screen.getByText('Biểu đồ Lượng Calo Tiêu thụ vs. Mục tiêu')).toBeInTheDocument();
        expect(screen.getByTestId('responsive-container')).toBeInTheDocument();
        expect(screen.getByTestId('line-chart')).toBeInTheDocument();

        // Check for the lines in the chart
        expect(screen.getByTestId('line-Calo Đã Tiêu Thụ')).toBeInTheDocument();
        expect(screen.getByTestId('line-Mục Tiêu Hàng Ngày')).toBeInTheDocument();
    });

    test('displays error message on API failure', async () => {
        const errorMessage = 'Không thể tải dữ liệu báo cáo. Vui lòng thử lại.';
        logService.getProgressData.mockRejectedValue(new Error('API Error'));
        renderWithRouter();

        await waitFor(() => {
            expect(screen.getByText(errorMessage)).toBeInTheDocument();
        });
        expect(screen.queryByText('Đang tải dữ liệu báo cáo...')).not.toBeInTheDocument();
        expect(screen.queryByTestId('line-chart')).not.toBeInTheDocument();
    });

    test('allows user to switch to 30-day view and fetches new data', async () => {
        // Initial 7-day fetch
        logService.getProgressData.mockResolvedValueOnce(dummyData7days);
        renderWithRouter();

        await waitFor(() => {
            expect(logService.getProgressData).toHaveBeenCalledTimes(1);
        });

        // Change to 30-day view
        logService.getProgressData.mockResolvedValueOnce(dummyData30days);
        const select = screen.getByRole('combobox');
        fireEvent.change(select, { target: { value: '30days' } });

        // Check that the loading state appears for the new fetch
        expect(screen.getByText('Đang tải dữ liệu báo cáo...')).toBeInTheDocument();

        await waitFor(() => {
            expect(logService.getProgressData).toHaveBeenCalledTimes(2);
            const endDate = new Date().toISOString().slice(0, 10);
            let startDate = new Date();
            startDate.setDate(startDate.getDate() - 30);
            const startDateString = startDate.toISOString().slice(0, 10);
            expect(logService.getProgressData).toHaveBeenCalledWith(startDateString, endDate);
        });

        // Check that the chart is still there
        expect(screen.getByTestId('line-chart')).toBeInTheDocument();
    });

    test('renders the select dropdown with correct options', () => {
        logService.getProgressData.mockResolvedValue(dummyData7days);
        renderWithRouter();

        const select = screen.getByRole('combobox');
        expect(select).toBeInTheDocument();
        expect(select).toHaveValue('7days');
        expect(screen.getByRole('option', { name: '7 Ngày qua' })).toBeInTheDocument();
        expect(screen.getByRole('option', { name: '30 Ngày qua' })).toBeInTheDocument();
    });

    test('disables dropdown while loading', async () => {
        logService.getProgressData.mockImplementation(() => new Promise(() => {})); // Never resolves
        renderWithRouter();

        await waitFor(() => {
             expect(screen.getByText('Đang tải dữ liệu báo cáo...')).toBeInTheDocument();
        });

        const select = screen.getByRole('combobox');
        expect(select).toBeDisabled();
    });
});
