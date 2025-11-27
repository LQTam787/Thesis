// src/services/authService.js

import api from './api'; // Import api instance đã đính kèm Interceptor (Authorization Header)
import { setCredentials, logout } from '../store/authSlice';
import { store } from '../store/store'; // Truy cập trực tiếp store để dispatch action

// --- Hàm Đăng nhập ---
const login = async (username, password) => {
    try {
        const response = await api.post('/auth/login', { username, password });

        // Giả định API trả về { token: 'jwt-token', user: { id: 1, role: 'USER', ... } }
        const { token, user } = response.data;

        // Lưu Token và User vào Redux State và Local Storage thông qua Redux Toolkit
        store.dispatch(setCredentials({ token, user }));

        // Trả về thông tin người dùng (tùy chọn)
        return user;
    } catch (error) {
        // Xử lý lỗi API (ví dụ: 401 Unauthorized)
        console.error('Lỗi đăng nhập:', error.response?.data || error.message);
        throw error;
    }
};

// --- Hàm Đăng ký ---
const register = async (userData) => {
    try {
        const response = await api.post('/auth/register', userData);

        // Giả định API trả về thông báo thành công hoặc thông tin user mới
        return response.data;
    } catch (error) {
        console.error('Lỗi đăng ký:', error.response?.data || error.message);
        throw error;
    }
};

// --- Hàm Đăng xuất ---
const performLogout = () => {
    // Xóa Token và User khỏi Redux State và Local Storage
    store.dispatch(logout());
};


const authService = {
    login,
    register,
    performLogout,
};

export default authService;