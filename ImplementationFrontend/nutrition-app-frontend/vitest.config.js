// vitest.config.js
import { defineConfig } from 'vitest/config';

export default defineConfig({
    test: {
        environment: 'jsdom', // Cấu hình môi trường DOM ảo
        globals: true, // Cho phép sử dụng describe, test mà không cần import
        // ĐẢM BẢO setupGlobalMocks.js CHẠY ĐẦU TIÊN
        setupFiles: [
            './src/mocks/setupGlobalMocks.js', // <-- PHẢI CHẠY ĐẦU TIÊN
            './src/setupTests.js'
        ],
        coverage: {
            provider: 'v8', // Công cụ thống kê coverage
            reporter: ['text', 'json', 'html'],
            include: ['src/**/*.{js,jsx,ts,tsx}'], // Chỉ bao gồm các file trong src/
            thresholds: {
                statements: 90, // Mục tiêu 90%
                branches: 90,
                functions: 90,
                lines: 90,
            },
        },
    },
});