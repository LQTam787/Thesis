import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter, useNavigate } from 'react-router-dom';
import RegisterPage from '../RegisterPage';
import authService from '../../services/authService';
import { useAuth } from '@auth/AuthContext';

// Mock useNavigate hook
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

// Mock authService
jest.mock('../../services/authService', () => ({
  register: jest.fn(),
}));

// Mock useAuth hook
jest.mock('@auth/AuthContext', () => ({
  useAuth: jest.fn(),
}));

describe('RegisterPage', () => {
  const mockNavigate = useNavigate as jest.Mock;
  const mockAuthServiceRegister = authService.register as jest.Mock;
  const mockUseAuth = useAuth as jest.Mock;
  const mockLoginContext = jest.fn();
  const mockNavigateFunction = jest.fn(); // Consistent mock for navigate

  beforeEach(() => {
    // Reset mocks before each test
    mockNavigate.mockClear();
    mockAuthServiceRegister.mockClear();
    mockLoginContext.mockClear();
    mockNavigateFunction.mockClear();

    // Default mock for useNavigate
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

  test('renders registration form', () => {
    render(
      <BrowserRouter>
        <RegisterPage />
      </BrowserRouter>
    );

    expect(screen.getByLabelText(/Email/i)).toBeInTheDocument();
    expect(screen.getByLabelText('Password')).toBeInTheDocument(); // Use exact string
    expect(screen.getByLabelText('Confirm Password')).toBeInTheDocument(); // Use exact string
    expect(screen.getByRole('button', { name: /Register/i })).toBeInTheDocument();
  });

  test('shows error if passwords do not match', async () => {
    render(
      <BrowserRouter>
        <RegisterPage />
      </BrowserRouter>
    );

    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByLabelText('Password'), { target: { value: 'password123' } });
    fireEvent.change(screen.getByLabelText('Confirm Password'), { target: { value: 'different' } });
    fireEvent.click(screen.getByRole('button', { name: /Register/i }));

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith('Passwords do not match!');
      expect(mockAuthServiceRegister).not.toHaveBeenCalled();
      expect(mockLoginContext).not.toHaveBeenCalled();
      expect(mockNavigateFunction).not.toHaveBeenCalled();
    });
  });

  test('handles successful registration', async () => {
    mockAuthServiceRegister.mockResolvedValue({ token: 'mock-token', role: 'user' });

    render(
      <BrowserRouter>
        <RegisterPage />
      </BrowserRouter>
    );

    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByLabelText('Password'), { target: { value: 'password123' } });
    fireEvent.change(screen.getByLabelText('Confirm Password'), { target: { value: 'password123' } });
    fireEvent.click(screen.getByRole('button', { name: /Register/i }));

    await waitFor(() => {
      expect(mockAuthServiceRegister).toHaveBeenCalledWith({ email: 'test@example.com', password: 'password123' });
      expect(mockLoginContext).toHaveBeenCalledWith('mock-token', 'user');
      expect(mockNavigateFunction).toHaveBeenCalledWith('/');
    });
  });

  test('handles failed registration', async () => {
    const errorMessage = 'Registration failed';
    mockAuthServiceRegister.mockRejectedValue(new Error(errorMessage));

    render(
      <BrowserRouter>
        <RegisterPage />
      </BrowserRouter>
    );

    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'fail@example.com' } });
    fireEvent.change(screen.getByLabelText('Password'), { target: { value: 'password123' } });
    fireEvent.change(screen.getByLabelText('Confirm Password'), { target: { value: 'password123' } });
    fireEvent.click(screen.getByRole('button', { name: /Register/i }));

    await waitFor(() => {
      expect(mockAuthServiceRegister).toHaveBeenCalledWith({ email: 'fail@example.com', password: 'password123' });
      expect(mockLoginContext).not.toHaveBeenCalled();
      expect(mockNavigateFunction).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith(errorMessage);
    });
  });
});
