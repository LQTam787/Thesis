// src/store/store.js (Đảm bảo có export này)
import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        // ... các reducers khác
    },
});
// Lưu ý: Các service như authService.js đã gợi ý việc này [cite: 1544]