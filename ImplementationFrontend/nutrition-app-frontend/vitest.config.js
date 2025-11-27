import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

export default defineConfig({
    plugins: [react()],
    test: {
        // 1. Sử dụng môi trường JSDOM để mô phỏng trình duyệt
        environment: 'jsdom',
        // 2. Định nghĩa các tập tin setup global (cho Testing Library, MSW, jest-dom)
        setupFiles: ['./src/setupTests.js'],
        // 3. Cấu hình Coverage
        coverage: {
            provider: 'v8', // Công cụ thống kê coverage (hoặc 'istanbul')
            reporter: ['text', 'json', 'html'], // Định dạng báo cáo đầu ra
            include: ['src//\*.{js,jsx,ts,tsx}'], // Các file cần tính coverage
            exclude: [
                'src/main.jsx', // File khởi tạo
                '/*.d.ts',
                '/__tests__/',
                'src/mocks/' // Loại trừ file mocks
            ],
            all: true // Tính cả các file chưa có test vào báo cáo
        },
        // 4. Các tùy chọn khác
        globals: true, // Cho phép sử dụng các global API như 'describe', 'test', 'expect'
    },
});