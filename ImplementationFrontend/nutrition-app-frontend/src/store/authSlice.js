// src/store/authSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    user: null, // Thông tin người dùng (id, username, role,...)
    token: null, // Token JWT
    isAuthenticated: false,
    isLoading: true, // Để xử lý việc kiểm tra token lúc khởi động
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setCredentials: (state, action) => {
            // Lưu user và token sau khi đăng nhập thành công
            const { user, token } = action.payload;
            state.user = user;
            state.token = token;
            state.isAuthenticated = true;
            localStorage.setItem('token', token); // Lưu vào Local Storage
            // Thêm logic lưu thông tin user khác nếu cần
        },
        logout: (state) => {
            // Xóa thông tin đăng nhập
            state.user = null;
            state.token = null;
            state.isAuthenticated = false;
            localStorage.removeItem('token'); // Xóa khỏi Local Storage
        },
        setLoading: (state, action) => {
            state.isLoading = action.payload;
        }
    },
});

export const { setCredentials, logout, setLoading } = authSlice.actions;
export default authSlice.reducer;