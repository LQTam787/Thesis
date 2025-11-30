// src/services/__tests__/api.test.js
import axios from 'axios';
import { vi, describe, it, expect, beforeEach, beforeAll } from 'vitest';
import { store } from '../../store/store';
import { logout } from '../../store/authSlice';

// Mock axios and other dependencies BEFORE importing the module that uses them.
// Vitest hoists vi.mock calls, so this is guaranteed.
vi.mock('axios', () => ({
  default: {
    create: vi.fn(() => ({
      interceptors: {
        response: {
          use: vi.fn(),
        },
      },
    })),
  },
}));

vi.mock('../../store/store', () => ({
  store: {
    dispatch: vi.fn(),
  },
}));

vi.mock('../../store/authSlice', () => ({
  logout: vi.fn(() => ({ type: 'auth/logout' })), // Return a mock action object
}));

// Import the module under test. This will trigger the mocked axios.create and .use calls.
import '../api';

// Mock window.location
// We need to do this before describe block because it's a global object modification
if (global.window) {
    const { location } = global.window;
    delete global.window.location;
    global.window.location = {
      ...location,
      replace: vi.fn(),
      pathname: '/',
    };
}


describe('API Interceptor', () => {
  let successInterceptor;
  let errorInterceptor;

  // This is the crucial part. We need to extract the interceptor functions
  // that were passed to the mock `use` function when the `api.js` module was loaded.
  beforeAll(() => {
    // Get the mock for the `use` function.
    // `axios.create` was called once when `api.js` was imported.
    // `mock.results[0].value` gives us the returned object from that call.
    const mockApiInstance = axios.create.mock.results[0].value;
    const useMock = mockApiInstance.interceptors.response.use;

    if (useMock.mock.calls.length === 0) {
      throw new Error('Interceptor was not set up. Check that api.js calls api.interceptors.response.use()');
    }

    // The first argument to `use` is the success handler, the second is the error handler.
    successInterceptor = useMock.mock.calls[0][0];
    errorInterceptor = useMock.mock.calls[0][1];
  });

  beforeEach(() => {
    // Reset only the mocks that accumulate state across tests.
    // Do NOT use vi.clearAllMocks() as it would clear the interceptor registration call.
    store.dispatch.mockClear();
    if (global.window) {
        window.location.replace.mockClear();
        // Reset pathname for each test
        window.location.pathname = '/dashboard';
    }
  });

  it('should return response for successful requests', () => {
    const response = { status: 200, data: 'Success' };
    const result = successInterceptor(response);
    expect(result).toEqual(response);
  });

  it('should dispatch logout and redirect to /login on 401 error', async () => {
    const error = { response: { status: 401 } };

    await expect(errorInterceptor(error)).rejects.toEqual(error);

    expect(store.dispatch).toHaveBeenCalledWith(logout());
    expect(window.location.replace).toHaveBeenCalledWith('/login');
  });

  it('should dispatch logout and redirect to /login on 403 error', async () => {
    const error = { response: { status: 403 } };

    await expect(errorInterceptor(error)).rejects.toEqual(error);

    expect(store.dispatch).toHaveBeenCalledWith(logout());
    expect(window.location.replace).toHaveBeenCalledWith('/login');
  });

  it('should not redirect if already on /login page', async () => {
    window.location.pathname = '/login';
    const error = { response: { status: 401 } };

    await expect(errorInterceptor(error)).rejects.toEqual(error);

    expect(store.dispatch).toHaveBeenCalledWith(logout());
    expect(window.location.replace).not.toHaveBeenCalled();
  });

  it('should reject for other errors without logging out or redirecting', async () => {
    const error = { response: { status: 500 } };

    await expect(errorInterceptor(error)).rejects.toEqual(error);

    expect(store.dispatch).not.toHaveBeenCalled();
    expect(window.location.replace).not.toHaveBeenCalled();
  });

  it('should handle errors with no response object', async () => {
    const error = new Error('Network Error');

    await expect(errorInterceptor(error)).rejects.toEqual(error);

    expect(store.dispatch).not.toHaveBeenCalled();
    expect(window.location.replace).not.toHaveBeenCalled();
  });
});
