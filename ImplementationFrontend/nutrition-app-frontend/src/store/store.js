// src/store/store.js
import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        // Thêm các reducer khác tại đây (ví dụ: plan, log, community)
    },
});