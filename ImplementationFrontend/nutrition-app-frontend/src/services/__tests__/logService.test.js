// src/services/__tests__/logService.test.js

import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from '../api';
import logService from '../logService';

// Mock 'api' module
vi.mock('../api', () => ({
    default: {
        post: vi.fn(),
        get: vi.fn(),
    },
}));

describe('logService', () => {
    beforeEach(() => {
        // Reset mocks before each test
        vi.resetAllMocks();
    });

    // Test suite for createDailyLog
    describe('createDailyLog', () => {
        it('should create a daily log and return the created log data', async () => {
            const logData = {
                foodName: 'Apple',
                calories: 95,
                date: '2023-11-20',
                mealType: 'Snack',
                quantity: 1,
            };
            const mockResponse = { id: 1, ...logData };

            // Mock the API post request
            api.post.mockResolvedValue({ data: mockResponse });

            const result = await logService.createDailyLog(logData);

            // Assertions
            expect(api.post).toHaveBeenCalledWith('/logs', logData);
            expect(result).toEqual(mockResponse);
        });

        it('should throw an error if the API call fails', async () => {
            const logData = { foodName: 'Apple' };
            const errorMessage = 'Failed to create log';

            // Mock the API post request to reject
            api.post.mockRejectedValue(new Error(errorMessage));

            // Assertions
            await expect(logService.createDailyLog(logData)).rejects.toThrow(errorMessage);
            expect(api.post).toHaveBeenCalledWith('/logs', logData);
        });
    });

    // Test suite for getProgressData
    describe('getProgressData', () => {
        it('should fetch progress data for a given date range', async () => {
            const startDate = '2023-11-01';
            const endDate = '2023-11-20';
            const mockData = [
                { date: '2023-11-20', totalCalories: 2100 },
                { date: '2023-11-19', totalCalories: 1950 },
            ];

            // Mock the API get request
            api.get.mockResolvedValue({ data: mockData });

            const result = await logService.getProgressData(startDate, endDate);

            // Assertions
            expect(api.get).toHaveBeenCalledWith('/logs/report', {
                params: { startDate, endDate },
            });
            expect(result).toEqual(mockData);
        });

        it('should throw an error if the API call fails', async () => {
            const startDate = '2023-11-01';
            const endDate = '2023-11-20';
            const errorMessage = 'Failed to fetch progress data';

            // Mock the API get request to reject
            api.get.mockRejectedValue(new Error(errorMessage));

            // Assertions
            await expect(logService.getProgressData(startDate, endDate)).rejects.toThrow(errorMessage);
            expect(api.get).toHaveBeenCalledWith('/logs/report', {
                params: { startDate, endDate },
            });
        });
    });
});
