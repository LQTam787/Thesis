// src/services/api.js
import axios from 'axios';
// Cần truy cập store và action logout để xử lý state tập trung
import { store } from '../store/store';
import { logout } from '../store/authSlice';

// Lấy BASE_URL từ biến môi trường
// Đảm bảo bạn đã cấu hình biến này trong file .env (ví dụ: VITE_API_BASE_URL="http://localhost:8080/api")
const VITE_API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const api = axios.create({
    baseURL: VITE_API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// --- Interceptor Xử lý Lỗi API Tập trung ---
api.interceptors.response.use(
    (response) => {
        // Trả về response nếu không có lỗi
        return response;
    },
    (error) => {
        const status = error.response ? error.response.status : null;

        // Xử lý lỗi 401 (Unauthorized - Token hết hạn) hoặc 403 (Forbidden - Không có quyền)
        if (status === 401 || status === 403) {
            console.error(`Lỗi bảo mật ${status}: Truy cập bị từ chối hoặc token hết hạn.`, error.response);

            // 1. Dispatch action logout để xóa token khỏi Redux state và Local Storage
            store.dispatch(logout());

            // 2. Chuyển hướng người dùng về trang đăng nhập
            // Kiểm tra để tránh chuyển hướng liên tục nếu người dùng đã ở trang login
            if (window.location.pathname !== '/login') {
                // Dùng window.location.replace để thay thế lịch sử trình duyệt, giúp người dùng không bị mắc kẹt.
                window.location.replace('/login');
            }

            // Trả về một Promise bị reject để ngăn luồng nghiệp vụ API gọi bị lỗi tiếp tục
            return Promise.reject(error);
        }

        // Xử lý các lỗi khác (500, 404, v.v.)
        return Promise.reject(error);
    }
);

export default api;