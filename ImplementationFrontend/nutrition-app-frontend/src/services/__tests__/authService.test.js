// src/services/__tests__/authService.test.js

import { describe, it, expect, vi, beforeEach } from 'vitest';
import authService from '../authService';
import api from '../api';
import { store } from '../../store/store';
import { setCredentials, logout } from '../../store/authSlice';

// Mock 'api' module
vi.mock('../api', () => ({
    default: {
        post: vi.fn(),
    },
}));

// Mock Redux store's dispatch
vi.mock('../../store/store', () => ({
    store: {
        dispatch: vi.fn(),
    },
}));

// Mock authSlice actions
vi.mock('../../store/authSlice', () => ({
    setCredentials: vi.fn(),
    logout: vi.fn(),
}));

describe('authService', () => {
    beforeEach(() => {
        // Reset mocks before each test
        vi.clearAllMocks();
    });

    // Test suite for the login function
    describe('login', () => {
        it('should call api.post with correct credentials and dispatch setCredentials on success', async () => {
            const username = 'testuser';
            const password = 'password';
            const mockResponse = {
                data: {
                    token: 'jwt-token',
                    user: { id: 1, username: 'testuser', role: 'USER' },
                },
            };

            api.post.mockResolvedValue(mockResponse);

            const user = await authService.login(username, password);

            // Verify api.post was called correctly
            expect(api.post).toHaveBeenCalledWith('/auth/login', { username, password });

            // Verify setCredentials was dispatched with correct payload
            expect(store.dispatch).toHaveBeenCalledWith(setCredentials(mockResponse.data));

            // Verify the function returns the user object
            expect(user).toEqual(mockResponse.data.user);
        });

        it('should throw an error and log response data when api.post fails', async () => {
            const username = 'testuser';
            const password = 'password';
            const mockError = new Error('Đăng nhập thất bại');
            mockError.response = { data: 'Sai thông tin đăng nhập' };

            api.post.mockRejectedValue(mockError);

            // Spy on console.error
            const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

            await expect(authService.login(username, password)).rejects.toThrow('Đăng nhập thất bại');

            // Verify error was logged with response data
            expect(consoleErrorSpy).toHaveBeenCalledWith('Lỗi đăng nhập:', 'Sai thông tin đăng nhập');

            // Clean up spy
            consoleErrorSpy.mockRestore();
        });

        it('should throw an error and log error message when api.post fails without a response', async () => {
            const username = 'testuser';
            const password = 'password';
            const mockError = new Error('Network Error');

            api.post.mockRejectedValue(mockError);

            // Spy on console.error
            const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

            await expect(authService.login(username, password)).rejects.toThrow('Network Error');

            // Verify error was logged with the error message
            expect(consoleErrorSpy).toHaveBeenCalledWith('Lỗi đăng nhập:', 'Network Error');

            // Clean up spy
            consoleErrorSpy.mockRestore();
        });
    });

    // Test suite for the register function
    describe('register', () => {
        it('should call api.post with user data and return response data on success', async () => {
            const userData = { username: 'newuser', password: 'newpassword', email: 'new@example.com' };
            const mockResponse = {
                data: { id: 2, username: 'newuser', message: 'Đăng ký thành công' },
            };

            api.post.mockResolvedValue(mockResponse);

            const result = await authService.register(userData);

            // Verify api.post was called correctly
            expect(api.post).toHaveBeenCalledWith('/auth/register', userData);

            // Verify the function returns the response data
            expect(result).toEqual(mockResponse.data);
        });

        it('should throw an error and log response data when api.post fails', async () => {
            const userData = { username: 'newuser', password: 'newpassword', email: 'new@example.com' };
            const mockError = new Error('Đăng ký thất bại');
            mockError.response = { data: 'Tên người dùng đã tồn tại' };

            api.post.mockRejectedValue(mockError);

            // Spy on console.error
            const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

            await expect(authService.register(userData)).rejects.toThrow('Đăng ký thất bại');

            // Verify error was logged with response data
            expect(consoleErrorSpy).toHaveBeenCalledWith('Lỗi đăng ký:', 'Tên người dùng đã tồn tại');

            // Clean up spy
            consoleErrorSpy.mockRestore();
        });

        it('should throw an error and log error message when api.post fails without a response', async () => {
            const userData = { username: 'newuser', password: 'newpassword', email: 'new@example.com' };
            const mockError = new Error('Registration Failed');

            api.post.mockRejectedValue(mockError);

            // Spy on console.error
            const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

            await expect(authService.register(userData)).rejects.toThrow('Registration Failed');

            // Verify error was logged with the error message
            expect(consoleErrorSpy).toHaveBeenCalledWith('Lỗi đăng ký:', 'Registration Failed');

            // Clean up spy
            consoleErrorSpy.mockRestore();
        });
    });

    // Test suite for the performLogout function
    describe('performLogout', () => {
        it('should dispatch the logout action', () => {
            authService.performLogout();

            // Verify logout action was dispatched
            expect(store.dispatch).toHaveBeenCalledWith(logout());
        });
    });
});
