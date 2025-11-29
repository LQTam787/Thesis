import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { vi } from 'vitest';
import RegisterPage from '../RegisterPage';
import authService from '../../services/authService';

// Mock authService
vi.mock('../../services/authService', () => ({
    default: {
        register: vi.fn(),
    },
}));

// Mock react-router-dom's useNavigate
const mockedUsedNavigate = vi.fn();
vi.mock('react-router-dom', async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockedUsedNavigate,
    };
});

describe('RegisterPage Integration Tests', () => {
    beforeEach(() => {
        // Reset mocks before each test
        authService.register.mockReset();
        mockedUsedNavigate.mockReset();
    });

    test('should successfully register a user and navigate to login', async () => {
        authService.register.mockResolvedValueOnce({}); // Mock successful registration

        render(
            <MemoryRouter>
                <RegisterPage />
            </MemoryRouter>
        );

        // Fill out the form
        fireEvent.change(screen.getByLabelText(/Tên người dùng/i), { target: { value: 'testuser' } });
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'test@example.com' } });
        fireEvent.change(screen.getByLabelText(/Mật khẩu/i), { target: { value: 'password123' } });

        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /Đăng Ký/i }));

        // Expect loading state
        expect(screen.getByRole('button', { name: /Đang xử lý.../i })).toBeInTheDocument();

        // Wait for the registration to complete and success message to appear
        await waitFor(() => {
            expect(authService.register).toHaveBeenCalledWith({
                username: 'testuser',
                email: 'test@example.com',
                password: 'password123',
            });
            // Use findByText which waits for the element to appear
            expect(screen.getByText(/Đăng ký thành công! Đang chuyển hướng đến trang Đăng nhập.../i)).toBeInTheDocument();
        });

        // Wait for navigation to login page
        await waitFor(() => {
            expect(mockedUsedNavigate).toHaveBeenCalledWith('/login');
        });
    });

    test('should display an error message on failed registration', async () => {
        const errorMessage = 'Tên người dùng đã tồn tại.';
        authService.register.mockRejectedValueOnce({
            response: { data: { message: errorMessage } },
        });

        render(
            <MemoryRouter>
                <RegisterPage />
            </MemoryRouter>
        );

        // Fill out the form
        fireEvent.change(screen.getByLabelText(/Tên người dùng/i), { target: { value: 'existinguser' } });
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'existing@example.com' } });
        fireEvent.change(screen.getByLabelText(/Mật khẩu/i), { target: { value: 'password123' } });

        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /Đăng Ký/i }));

        // Expect loading state
        expect(screen.getByRole('button', { name: /Đang xử lý.../i })).toBeInTheDocument();

        // Wait for the error message to appear
        await waitFor(() => {
            expect(screen.getByText(errorMessage)).toBeInTheDocument();
        });

        // Expect button to be re-enabled
        expect(screen.getByRole('button', { name: /Đăng Ký/i })).toBeInTheDocument();
        expect(screen.queryByText(/Đang xử lý.../i)).not.toBeInTheDocument();
        expect(mockedUsedNavigate).not.toHaveBeenCalled();
    });

    test('should display a generic error message on unexpected registration failure', async () => {
        authService.register.mockRejectedValueOnce(new Error('Network error'));

        render(
            <MemoryRouter>
                <RegisterPage />
            </MemoryRouter>
        );

        // Fill out the form
        fireEvent.change(screen.getByLabelText(/Tên người dùng/i), { target: { value: 'anyuser' } });
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'any@example.com' } });
        fireEvent.change(screen.getByLabelText(/Mật khẩu/i), { target: { value: 'password123' } });

        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /Đăng Ký/i }));

        // Wait for the error message to appear
        await waitFor(() => {
            expect(screen.getByText(/Đăng ký thất bại. Vui lòng thử lại./i)).toBeInTheDocument();
        });
        expect(mockedUsedNavigate).not.toHaveBeenCalled();
    });

    test('should navigate to login page when "Đăng nhập" link is clicked', () => {
        render(
            <MemoryRouter>
                <RegisterPage />
            </MemoryRouter>
        );

        const loginLink = screen.getByRole('link', { name: /Đăng nhập/i });
        expect(loginLink).toHaveAttribute('href', '/login');
    });
});
