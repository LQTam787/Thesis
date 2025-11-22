import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/'; // Thay đổi thành URL backend của bạn

interface LoginResponse {
  accessToken: string;
  // Thêm các trường khác nếu có (ví dụ: user details, refreshToken)
}

interface RegisterResponse {
  message: string;
  // Thêm các trường khác nếu có
}

export const login = async (username: string, password: string): Promise<LoginResponse> => {
  try {
    const response = await axios.post<LoginResponse>(API_URL + 'login', {
      username,
      password,
    });
    if (response.data.accessToken) {
      localStorage.setItem('userToken', response.data.accessToken);
    }
    return response.data;
  } catch (error: any) {
    throw error.response?.data?.message || error.message;
  }
};

export const register = async (userData: any): Promise<RegisterResponse> => {
  try {
    const response = await axios.post<RegisterResponse>(API_URL + 'register', userData);
    return response.data;
  } catch (error: any) {
    throw error.response?.data?.message || error.message;
  }
};

export const logout = () => {
  localStorage.removeItem('userToken');
  // Clear Redux state if applicable
};
