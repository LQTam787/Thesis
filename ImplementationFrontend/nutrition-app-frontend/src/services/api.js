// src/services/api.js
import axios from 'axios';

// Định nghĩa Base URL cho Backend (API chính)
const API_BASE_URL = import.meta.env.VITE_BACKEND_API_URL || 'http://localhost:8080/api';

// Định nghĩa Base URL cho AI Service
export const AI_SERVICE_BASE_URL = import.meta.env.VITE_AI_SERVICE_API_URL || 'http://localhost:5000/api';

// 1. Instance cho Backend (API chính)
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Thêm Interceptor để đính kèm Token JWT vào mỗi Request
api.interceptors.request.use(
    (config) => {
        // Lấy token từ Local Storage
        const token = localStorage.getItem('token');

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Tạm thời export một instance AI Service cơ bản
// Bạn sẽ sử dụng cái này trong aiService.js
export const aiApi = axios.create({
    baseURL: AI_SERVICE_BASE_URL,
    // AI Service có thể cần Content-Type khác nhau (ví dụ: cho file ảnh)
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;