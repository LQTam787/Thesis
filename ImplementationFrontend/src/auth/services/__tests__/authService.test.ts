import authService from '../authService';

describe('authService', () => {
  const MOCK_TOKEN = 'test-token';
  const MOCK_ROLE = 'user';

  beforeEach(() => {
    // Clear localStorage and reset mocks before each test
    localStorage.clear();
    jest.restoreAllMocks();
    // Mock fetch API
    global.fetch = jest.fn((url, options) => {
      if (url.endsWith('/login') || url.endsWith('/register')) {
        if (JSON.parse(options.body).email === 'test@example.com') {
          return Promise.resolve({
            ok: true,
            json: () => Promise.resolve({ token: MOCK_TOKEN, role: MOCK_ROLE }),
          });
        } else {
          return Promise.resolve({
            ok: false,
            json: () => Promise.resolve({ message: 'Invalid credentials' }),
          });
        }
      }
      return Promise.reject(new Error('not found'));
    }) as jest.Mock;
  });

  afterEach(() => {
    // Restore original fetch after all tests
    (global.fetch as jest.Mock).mockRestore();
  });

  test('login successfully and store token/role', async () => {
    const credentials = { email: 'test@example.com', password: 'password' };
    const response = await authService.login(credentials);

    expect(response).toEqual({ token: MOCK_TOKEN, role: MOCK_ROLE });
    expect(localStorage.getItem('userToken')).toBe(MOCK_TOKEN);
    expect(localStorage.getItem('userRole')).toBe(MOCK_ROLE);
  });

  test('login fails with incorrect credentials', async () => {
    const credentials = { email: 'wrong@example.com', password: 'password' };
    await expect(authService.login(credentials)).rejects.toThrow('Invalid credentials');
    expect(localStorage.getItem('userToken')).toBeNull();
    expect(localStorage.getItem('userRole')).toBeNull();
  });

  test('register successfully and store token/role', async () => {
    const credentials = { email: 'test@example.com', password: 'password' };
    const response = await authService.register(credentials);

    expect(response).toEqual({ token: MOCK_TOKEN, role: MOCK_ROLE });
    expect(localStorage.getItem('userToken')).toBe(MOCK_TOKEN);
    expect(localStorage.getItem('userRole')).toBe(MOCK_ROLE);
  });

  test('register fails with incorrect credentials', async () => {
    const credentials = { email: 'wrong@example.com', password: 'password' };
    await expect(authService.register(credentials)).rejects.toThrow('Invalid credentials');
    expect(localStorage.getItem('userToken')).toBeNull();
    expect(localStorage.getItem('userRole')).toBeNull();
  });

  test('logout removes token and role from localStorage', () => {
    localStorage.setItem('userToken', MOCK_TOKEN);
    localStorage.setItem('userRole', MOCK_ROLE);

    authService.logout();

    expect(localStorage.getItem('userToken')).toBeNull();
    expect(localStorage.getItem('userRole')).toBeNull();
  });

  test('getToken returns stored token', () => {
    localStorage.setItem('userToken', MOCK_TOKEN);
    expect(authService.getToken()).toBe(MOCK_TOKEN);
  });

  test('getToken returns null if no token is stored', () => {
    expect(authService.getToken()).toBeNull();
  });

  test('getUserRole returns stored role', () => {
    localStorage.setItem('userRole', MOCK_ROLE);
    expect(authService.getUserRole()).toBe(MOCK_ROLE);
  });

  test('getUserRole returns null if no role is stored', () => {
    expect(authService.getUserRole()).toBeNull();
  });

  test('isAuthenticated returns true if token and role are stored', () => {
    localStorage.setItem('userToken', MOCK_TOKEN);
    localStorage.setItem('userRole', MOCK_ROLE);
    expect(authService.isAuthenticated()).toBe(true);
  });

  test('isAuthenticated returns false if token is missing', () => {
    localStorage.setItem('userRole', MOCK_ROLE);
    expect(authService.isAuthenticated()).toBe(false);
  });

  test('isAuthenticated returns false if role is missing', () => {
    localStorage.setItem('userToken', MOCK_TOKEN);
    expect(authService.isAuthenticated()).toBe(false);
  });

  test('isAuthenticated returns false if both token and role are missing', () => {
    expect(authService.isAuthenticated()).toBe(false);
  });
});
