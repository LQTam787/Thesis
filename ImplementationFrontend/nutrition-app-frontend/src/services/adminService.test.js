// src/services/adminService.test.js

import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { http, HttpResponse } from 'msw';
import { server } from '../mocks/server'; // MSW server
import adminService from './adminService';
import api from './api'; // api instance

// Mock the console.error to avoid polluting the test output
vi.spyOn(console, 'error').mockImplementation(() => {});

describe('adminService', () => {
    afterEach(() => {
        vi.restoreAllMocks(); // Restore mocks after each test
    });

    // --- 1. User Management ---
    describe('User Management', () => {
        it('getAllUsers should fetch all users successfully', async () => {
            const mockUsers = [{ id: '1', username: 'user1' }, { id: '2', username: 'user2' }];
            server.use(
                http.get('*/admin/users', () => {
                    return HttpResponse.json(mockUsers);
                })
            );

            const users = await adminService.getAllUsers();
            expect(users).toEqual(mockUsers);
        });

        it('getAllUsers should throw an error if the API call fails', async () => {
            server.use(
                http.get('*/admin/users', () => {
                    return new HttpResponse(null, { status: 500, statusText: 'Internal Server Error' });
                })
            );

            await expect(adminService.getAllUsers()).rejects.toThrow('Request failed with status code 500');
        });

        it('getUserDetails should fetch a single user successfully', async () => {
            const mockUser = { id: '1', username: 'user1' };
            server.use(
                http.get('*/admin/users/1', () => {
                    return HttpResponse.json(mockUser);
                })
            );

            const user = await adminService.getUserDetails('1');
            expect(user).toEqual(mockUser);
        });

        it('getUserDetails should throw an error if the user is not found', async () => {
            server.use(
                http.get('*/admin/users/1', () => {
                    return new HttpResponse(null, { status: 404, statusText: 'Not Found' });
                })
            );

            await expect(adminService.getUserDetails('1')).rejects.toThrow('Request failed with status code 404');
        });

        it('toggleUserLockStatus should update the user lock status', async () => {
            const updatedUser = { id: '1', isLocked: true };
            server.use(
                http.put('*/admin/users/1/lock', async ({ request }) => {
                    const body = await request.json();
                    expect(body.isLocked).toBe(true);
                    return HttpResponse.json(updatedUser);
                })
            );

            const result = await adminService.toggleUserLockStatus('1', true);
            expect(result).toEqual(updatedUser);
        });

        it('toggleUserLockStatus should throw an error on failure', async () => {
            server.use(
                http.put('*/admin/users/1/lock', () => {
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(adminService.toggleUserLockStatus('1', true)).rejects.toThrow('Request failed with status code 500');
        });
    });

    // --- 2. Food Data Management ---
    describe('Food Data Management', () => {
        it('getAllFoods should fetch all foods successfully', async () => {
            const mockFoods = [{ id: 'f1', foodName: 'Apple' }, { id: 'f2', foodName: 'Banana' }];
            server.use(
                http.get('*/admin/foods', () => {
                    return HttpResponse.json(mockFoods);
                })
            );

            const foods = await adminService.getAllFoods();
            expect(foods).toEqual(mockFoods);
        });

        it('getAllFoods should throw an error on failure', async () => {
            server.use(
                http.get('*/admin/foods', () => {
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(adminService.getAllFoods()).rejects.toThrow('Request failed with status code 500');
        });

        it('createFood should create a new food item', async () => {
            const newFood = { foodName: 'Cherry', calories: 50 };
            const createdFood = { id: 'f3', ...newFood };
            server.use(
                http.post('*/admin/foods', async ({ request }) => {
                    const body = await request.json();
                    expect(body).toEqual(newFood);
                    return HttpResponse.json(createdFood, { status: 201 });
                })
            );

            const result = await adminService.createFood(newFood);
            expect(result).toEqual(createdFood);
        });

        it('createFood should throw an error on failure', async () => {
            server.use(
                http.post('*/admin/foods', () => {
                    return new HttpResponse(null, { status: 400 });
                })
            );

            await expect(adminService.createFood({})).rejects.toThrow('Request failed with status code 400');
        });

        it('updateFood should update an existing food item', async () => {
            const foodUpdate = { calories: 55 };
            const updatedFood = { id: 'f1', foodName: 'Apple', calories: 55 };
            server.use(
                http.put('*/admin/foods/f1', async ({ request }) => {
                    const body = await request.json();
                    expect(body).toEqual(foodUpdate);
                    return HttpResponse.json(updatedFood);
                })
            );

            const result = await adminService.updateFood('f1', foodUpdate);
            expect(result).toEqual(updatedFood);
        });

        it('updateFood should throw an error on failure', async () => {
            server.use(
                http.put('*/admin/foods/f1', () => {
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(adminService.updateFood('f1', {})).rejects.toThrow('Request failed with status code 500');
        });

        it('deleteFood should delete a food item', async () => {
            const spy = vi.spyOn(api, 'delete');
            server.use(
                http.delete('*/admin/foods/f1', () => {
                    return new HttpResponse(null, { status: 204 });
                })
            );

            await adminService.deleteFood('f1');
            // Check that the correct endpoint was called
            expect(spy).toHaveBeenCalledWith('/admin/foods/f1');
        });

        it('deleteFood should throw an error on failure', async () => {
            server.use(
                http.delete('*/admin/foods/f1', () => {
                    return new HttpResponse(null, { status: 500 });
                })
            );

            await expect(adminService.deleteFood('f1')).rejects.toThrow('Request failed with status code 500');
        });
    });

    // --- 3. AI Management ---
    describe('AI Management', () => {
        it('triggerAIRetraining should send a request to retrain the AI', async () => {
            const mockResponse = { status: 'STARTED', jobId: 'ai-train-123' };
            server.use(
                http.post('*/admin/ai/retrain', () => {
                    return HttpResponse.json(mockResponse);
                })
            );

            const result = await adminService.triggerAIRetraining();
            expect(result).toEqual(mockResponse);
        });

        it('triggerAIRetraining should throw an error on failure', async () => {
            server.use(
                http.post('*/admin/ai/retrain', () => {
                    return new HttpResponse(null, { status: 503 }); // Service Unavailable
                })
            );

            await expect(adminService.triggerAIRetraining()).rejects.toThrow('Request failed with status code 503');
        });
    });
});
