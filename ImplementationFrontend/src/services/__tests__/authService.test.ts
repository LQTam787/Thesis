import axios from 'axios';
import { login, logout, register } from '../authService';

jest.mock('axios');

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('authService localStorage interactions', () => {
  const MOCK_TOKEN = 'mock-access-token';

  beforeEach(() => {
    localStorage.clear();
    jest.restoreAllMocks();
    // Mock localStorage methods
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: jest.fn(() => null),
        setItem: jest.fn(() => {}),
        removeItem: jest.fn(() => {}),
        clear: jest.fn(() => {}),
      },
      writable: true,
    });
  });

  test('login should set userToken in localStorage on successful login', async () => {
    mockedAxios.post.mockResolvedValueOnce({
      data: { accessToken: MOCK_TOKEN },
    });

    await login('testuser', 'password');

    expect(window.localStorage.setItem).toHaveBeenCalledWith('userToken', MOCK_TOKEN);
  });

  test('login should not set userToken if accessToken is missing', async () => {
    mockedAxios.post.mockResolvedValueOnce({
      data: { accessToken: null }, // Explicitly set accessToken to null
    });

    await login('testuser', 'password');

    expect(window.localStorage.setItem).not.toHaveBeenCalled();
  });

  test('logout should remove userToken from localStorage', () => {
    // Simulate a token being in localStorage before logout
    (window.localStorage.getItem as jest.Mock).mockReturnValue(MOCK_TOKEN);
    localStorage.setItem('userToken', MOCK_TOKEN); // Set it for real to be removed

    logout();

    expect(window.localStorage.removeItem).toHaveBeenCalledWith('userToken');
  });

  test('login should throw error on API failure', async () => {
    const errorMessage = 'Network Error';
    mockedAxios.post.mockRejectedValueOnce({ response: { data: { message: errorMessage } } });

    await expect(login('testuser', 'password')).rejects.toThrow(errorMessage);
    expect(window.localStorage.setItem).not.toHaveBeenCalled();
  });

  test('login should throw default error message on API failure without specific message', async () => {
    const errorMessage = 'Something went wrong';
    mockedAxios.post.mockRejectedValueOnce(new Error(errorMessage));

    await expect(login('testuser', 'password')).rejects.toThrow(errorMessage);
    expect(window.localStorage.setItem).not.toHaveBeenCalled();
  });

  test('register should handle API success', async () => {
    const successMessage = 'Registration successful';
    mockedAxios.post.mockResolvedValueOnce({ data: { message: successMessage } });

    const result = await register({ username: 'newuser', password: 'password' });

    expect(result).toEqual({ message: successMessage });
    expect(window.localStorage.setItem).not.toHaveBeenCalled(); // Register does not set token
  });

  test('register should throw error on API failure', async () => {
    const errorMessage = 'Registration failed';
    mockedAxios.post.mockRejectedValueOnce({ response: { data: { message: errorMessage } } });

    await expect(register({ username: 'newuser', password: 'password' })).rejects.toThrow(errorMessage);
    expect(window.localStorage.setItem).not.toHaveBeenCalled();
  });

  test('register should throw default error message on API failure without specific message', async () => {
    const errorMessage = 'Something went wrong';
    mockedAxios.post.mockRejectedValueOnce(new Error(errorMessage));

    await expect(register({ username: 'newuser', password: 'password' })).rejects.toThrow(errorMessage);
    expect(window.localStorage.setItem).not.toHaveBeenCalled();
  });
});
