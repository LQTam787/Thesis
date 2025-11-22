interface AuthResponse {
  token: string;
  role: string; // Add role to AuthResponse
}

interface UserCredentials {
  email: string;
  password: string;
}

const API_BASE_URL = 'http://localhost:8080/api/auth'; // Adjust this to your backend API URL

const authService = {
  login: async (credentials: UserCredentials): Promise<AuthResponse> => {
    const response = await fetch(`${API_BASE_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Login failed');
    }

    const data: AuthResponse = await response.json();
    localStorage.setItem('userToken', data.token);
    localStorage.setItem('userRole', data.role); // Store user role
    return data;
  },

  register: async (credentials: UserCredentials): Promise<AuthResponse> => {
    const response = await fetch(`${API_BASE_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Registration failed');
    }

    const data: AuthResponse = await response.json();
    localStorage.setItem('userToken', data.token);
    localStorage.setItem('userRole', data.role); // Store user role
    return data;
  },

  logout: () => {
    localStorage.removeItem('userToken');
    localStorage.removeItem('userRole'); // Remove user role on logout
  },

  getToken: (): string | null => {
    return localStorage.getItem('userToken');
  },

  getUserRole: (): string | null => {
    return localStorage.getItem('userRole');
  },

  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('userToken') && !!localStorage.getItem('userRole');
  },
};

export default authService;
