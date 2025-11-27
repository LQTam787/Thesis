// src/services/profileService.js

import api from './api'; // Import api instance (Backend chính với JWT)

// --- 1. Lấy Hồ sơ Người dùng Toàn diện ---

/**
 * Lấy toàn bộ dữ liệu hồ sơ: User, Đặc điểm cá nhân (DacDiemCaNhan), và Mục tiêu (MucTieuDinhDuong).
 * @returns {Promise<object>} Đối tượng hồ sơ toàn diện.
 */
const getProfile = async () => {
    try {
        // Giả định endpoint: GET /api/profile
        const response = await api.get('/profile');
        // Trả về một đối tượng chứa tất cả thông tin: { user, characteristics, goals }
        return response.data;
    } catch (error) {
        console.error('Lỗi khi tải hồ sơ người dùng:', error.response?.data || error.message);
        throw error;
    }
};

// --- 2. Cập nhật Đặc điểm Cá nhân (DacDiemCaNhan) ---

/**
 * Cập nhật thông tin sinh học, mức độ hoạt động, dị ứng.
 * @param {object} characteristicsData - Dữ liệu đặc điểm cá nhân.
 * @returns {Promise<object>} Đặc điểm đã cập nhật.
 */
const updateCharacteristics = async (characteristicsData) => {
    try {
        // Giả định endpoint: PUT /api/profile/characteristics
        const response = await api.put('/profile/characteristics', characteristicsData);
        return response.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật đặc điểm cá nhân:', error.response?.data || error.message);
        throw error;
    }
};

// --- 3. Cập nhật Mục tiêu Dinh dưỡng (MucTieuDinhDuong) ---

/**
 * Cập nhật mục tiêu sức khỏe và thời gian.
 * @param {object} goalsData - Dữ liệu mục tiêu dinh dưỡng.
 * @returns {Promise<object>} Mục tiêu đã cập nhật.
 */
const updateGoals = async (goalsData) => {
    try {
        // Giả định endpoint: PUT /api/profile/goals
        const response = await api.put('/profile/goals', goalsData);
        return response.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật mục tiêu dinh dưỡng:', error.response?.data || error.message);
        throw error;
    }
};


const profileService = {
    getProfile,
    updateCharacteristics,
    updateGoals,
    // Thêm updateBasicUser (name, email) nếu Backend cho phép
};

export default profileService;