// src/services/logService.js

import api from './api'; // Import api instance (Backend chính)

// --- 1. Ghi nhật ký hàng ngày (Daily Log) ---

/**
 * Ghi lại một mục nhật ký mới (ví dụ: một bữa ăn).
 * @param {object} logData - Dữ liệu nhật ký, bao gồm: { foodName, calories, date, mealType, quantity }
 * @returns {Promise<object>} Mục nhật ký đã tạo.
 */
const createDailyLog = async (logData) => {
    try {
        // Giả định endpoint: POST /api/logs
        const response = await api.post('/logs', logData);
        return response.data; // Trả về log đã tạo
    } catch (error) {
        console.error('Lỗi khi ghi nhật ký hàng ngày:', error.response?.data || error.message);
        throw error;
    }
};

// --- 2. Lấy dữ liệu Báo cáo và Biểu đồ ---

/**
 * Lấy dữ liệu nhật ký cho một khoảng thời gian cụ thể (dùng để hiển thị biểu đồ).
 * @param {string} startDate - Ngày bắt đầu (YYYY-MM-DD).
 * @param {string} endDate - Ngày kết thúc (YYYY-MM-DD).
 * @returns {Promise<Array<object>>} Danh sách các mục nhật ký hoặc dữ liệu tóm tắt.
 */
const getProgressData = async (startDate, endDate) => {
    try {
        // Giả định endpoint: GET /api/logs/report?startDate=...&endDate=...
        const response = await api.get(`/logs/report`, {
            params: { startDate, endDate },
        });
        // Trả về dữ liệu tóm tắt (ví dụ: [{date: '2023-11-01', totalCalories: 2000, target: 2200}, ...])
        return response.data;
    } catch (error) {
        console.error('Lỗi khi lấy dữ liệu báo cáo:', error.response?.data || error.message);
        throw error;
    }
};


const logService = {
    createDailyLog,
    getProgressData,
    // Thêm các hàm updateLog, deleteLog, getLogsByDate nếu cần
};

export default logService;