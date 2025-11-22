import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter, useNavigate } from 'react-router-dom';
import LoginPage from '../LoginPage';
import authService from '../../services/authService';
import { useAuth } from '@auth/AuthContext'; // Using alias

// Mock useNavigate hook
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

// Mock authService
jest.mock('../../services/authService', () => ({
  login: jest.fn(),
}));

// Mock useAuth hook
jest.mock('@auth/AuthContext', () => ({ // Using alias for mock
  useAuth: jest.fn(),
}));

describe('LoginPage', () => {
  const mockNavigate = useNavigate as jest.Mock;
  const mockAuthServiceLogin = authService.login as jest.Mock;
  const mockUseAuth = useAuth as jest.Mock;
  const mockLoginContext = jest.fn();
  const mockNavigateFunction = jest.fn(); // Define a single mock function for navigate

  beforeEach(() => {
    // Reset mocks before each test
    mockNavigate.mockClear();
    mockAuthServiceLogin.mockClear();
    mockLoginContext.mockClear();
    mockNavigateFunction.mockClear(); // Clear the navigate function mock as well

    // Default mock for useNavigate to return the consistent mock function
    mockNavigate.mockReturnValue(mockNavigateFunction);

    // Default mock for useAuth
    mockUseAuth.mockReturnValue({ login: mockLoginContext });

    // Mock window.alert to prevent it from blocking tests
    jest.spyOn(window, 'alert').mockImplementation(() => {});
  });

  afterEach(() => {
    // Restore original window.alert after each test
    (window.alert as jest.Mock).mockRestore();
  });

  test('renders login form', () => {
    render(
      <BrowserRouter>
        <LoginPage />
      </BrowserRouter>
    );

    expect(screen.getByLabelText(/Email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Login/i })).toBeInTheDocument();
  });

  test('handles successful login', async () => {
    mockAuthServiceLogin.mockResolvedValue({ token: 'mock-token', role: 'user' });

    render(
      <BrowserRouter>
        <LoginPage />
      </BrowserRouter>
    );

    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: 'password' } });
    fireEvent.click(screen.getByRole('button', { name: /Login/i }));

    await waitFor(() => {
      expect(mockAuthServiceLogin).toHaveBeenCalledWith({ email: 'test@example.com', password: 'password' });
      expect(mockLoginContext).toHaveBeenCalledWith('mock-token', 'user');
      expect(mockNavigateFunction).toHaveBeenCalledWith('/');
    });
  });

  test('handles failed login', async () => {
    const errorMessage = 'Invalid credentials';
    mockAuthServiceLogin.mockRejectedValue(new Error(errorMessage));

    render(
      <BrowserRouter>
        <LoginPage />
      </BrowserRouter>
    );

    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'wrong@example.com' } });
    fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: 'password' } });
    fireEvent.click(screen.getByRole('button', { name: /Login/i }));

    await waitFor(() => {
      expect(mockAuthServiceLogin).toHaveBeenCalledWith({ email: 'wrong@example.com', password: 'password' });
      expect(mockLoginContext).not.toHaveBeenCalled();
      expect(mockNavigateFunction).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith(errorMessage);
    });
  });
});
