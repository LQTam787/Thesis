import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter, useNavigate } from 'react-router-dom';
import HomePage from '../HomePage';
import authService from '../../auth/services/authService';

// Mock useNavigate hook
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(() => jest.fn()), // Ensure it returns a callable mock function
}));

// Mock authService
jest.mock('../../auth/services/authService', () => ({
  logout: jest.fn(),
}));

// Mock NutritionalConsultation component
jest.mock('../../components/NutritionalConsultation', () => {
  return () => <div>NutritionalConsultation Mock</div>;
});

describe('HomePage', () => {
  const mockNavigate = useNavigate as jest.Mock;

  beforeEach(() => {
    // Reset mocks before each test
    mockNavigate.mockClear();
    (authService.logout as jest.Mock).mockClear();
  });

  test('renders NutritionalConsultation component', () => {
    render(
      <BrowserRouter>
        <HomePage />
      </BrowserRouter>
    );
    expect(screen.getByText('NutritionalConsultation Mock')).toBeInTheDocument();
  });

  test('calls logout and navigates to login on logout button click', () => {
    const navigateFunction = jest.fn();
    mockNavigate.mockReturnValue(navigateFunction); // Set the return value of useNavigate

    render(
      <BrowserRouter>
        <HomePage />
      </BrowserRouter>
    );

    fireEvent.click(screen.getByRole('button', { name: /Logout/i }));

    expect(authService.logout).toHaveBeenCalledTimes(1);
    expect(navigateFunction).toHaveBeenCalledWith('/login');
  });
});
