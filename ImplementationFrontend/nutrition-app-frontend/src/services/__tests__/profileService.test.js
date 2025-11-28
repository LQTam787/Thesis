// src/services/__tests__/profileService.test.js

import { http, HttpResponse } from 'msw';
import { server } from '../../mocks/server';
import profileService from '../profileService';

// Mock the console.error to avoid polluting the test output
const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

describe('profileService', () => {
    afterEach(() => {
        consoleErrorSpy.mockClear();
        server.resetHandlers();
    });

    // --- Tests for getProfile ---
    describe('getProfile', () => {
        it('should return profile data on successful API call', async () => {
            const mockProfileData = {
                user: { name: 'Test User' },
                characteristics: { height: 175 },
                goals: { targetWeight: 70 },
            };
            server.use(
                http.get('*/profile', () => {
                    return HttpResponse.json(mockProfileData);
                })
            );

            const data = await profileService.getProfile();

            expect(data).toEqual(mockProfileData);
        });

        it('should throw an error and log response data when API call fails with a body', async () => {
            server.use(
                http.get('*/profile', () => {
                    return HttpResponse.json({ message: 'Internal Server Error' }, { status: 500 });
                })
            );

            await expect(profileService.getProfile()).rejects.toThrow();
            expect(consoleErrorSpy).toHaveBeenCalled();
        });

        it('should throw an error and log error message when API call fails without a body', async () => {
            server.use(
                http.get('*/profile', () => {
                    // Simulate a network error or a response without a body
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(profileService.getProfile()).rejects.toThrow();
            expect(consoleErrorSpy).toHaveBeenCalled();
        });
    });

    // --- Tests for updateCharacteristics ---
    describe('updateCharacteristics', () => {
        it('should return updated characteristics data on successful API call', async () => {
            const mockCharacteristics = { height: 180, weight: 75 };
            server.use(
                http.put('*/profile/characteristics', () => {
                    return HttpResponse.json(mockCharacteristics);
                })
            );

            const data = await profileService.updateCharacteristics(mockCharacteristics);

            expect(data).toEqual(mockCharacteristics);
        });

        it('should throw an error and log response data when API call fails with a body', async () => {
            const mockCharacteristics = { height: 180, weight: 75 };
            server.use(
                http.put('*/profile/characteristics', () => {
                    return HttpResponse.json({ message: 'Bad Request' }, { status: 400 });
                })
            );

            await expect(profileService.updateCharacteristics(mockCharacteristics)).rejects.toThrow();
            expect(consoleErrorSpy).toHaveBeenCalled();
        });

        it('should throw an error and log error message when API call fails without a body', async () => {
            const mockCharacteristics = { height: 180, weight: 75 };
            server.use(
                http.put('*/profile/characteristics', () => {
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(profileService.updateCharacteristics(mockCharacteristics)).rejects.toThrow();
            expect(consoleErrorSpy).toHaveBeenCalled();
        });
    });

    // --- Tests for updateGoals ---
    describe('updateGoals', () => {
        it('should return updated goals data on successful API call', async () => {
            const mockGoals = { target: 'muscle_gain' };
            server.use(
                http.put('*/profile/goals', () => {
                    return HttpResponse.json(mockGoals);
                })
            );

            const data = await profileService.updateGoals(mockGoals);

            expect(data).toEqual(mockGoals);
        });

        it('should throw an error and log response data when API call fails with a body', async () => {
            const mockGoals = { target: 'muscle_gain' };
            server.use(
                http.put('*/profile/goals', () => {
                    return HttpResponse.json({ message: 'Server Error' }, { status: 500 });
                })
            );

            await expect(profileService.updateGoals(mockGoals)).rejects.toThrow();
            expect(consoleErrorSpy).toHaveBeenCalled();
        });

        it('should throw an error and log error message when API call fails without a body', async () => {
            const mockGoals = { target: 'muscle_gain' };
            server.use(
                http.put('*/profile/goals', () => {
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(profileService.updateGoals(mockGoals)).rejects.toThrow();
            expect(consoleErrorSpy).toHaveBeenCalled();
        });
    });
});
