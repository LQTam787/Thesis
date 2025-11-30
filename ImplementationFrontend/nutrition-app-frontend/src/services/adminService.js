// src/services/adminService.js

import api from './api'; // Import api instance (Backend chính với JWT)

// --- 1. Quản lý Người dùng (User Management) ---

/**
 * Lấy danh sách tất cả người dùng trong hệ thống (chỉ dành cho Admin).
 * @returns {Promise<Array<object>>} Danh sách người dùng.
 */
const getAllUsers = async () => {
    try {
        // Giả định endpoint: GET /api/admin/users
        const response = await api.get('/admin/users');
        // Giả định mỗi người dùng trả về: { id, username, email, fullName, roles, isLocked, createdAt }
        return response.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách người dùng (Admin):', error.response?.data || error.message);
        // Nếu lỗi 403 (Forbidden) hoặc 401 (Unauthorized) sẽ được xử lý bởi Interceptor (đã thiết lập trong api.js)
        throw error;
    }
};

/**
 * Lấy chi tiết một người dùng cụ thể.
 * @param {string} userId - ID của người dùng.
 * @returns {Promise<object>} Chi tiết người dùng.
 */
const getUserDetails = async (userId) => {
    try {
        // Giả định endpoint: GET /api/admin/users/{userId}
        const response = await api.get(`/admin/users/${userId}`);
        return response.data;
    } catch (error) {
        console.error(`Lỗi khi tải chi tiết người dùng ${userId} (Admin):`, error.response?.data || error.message);
        throw error;
    }
};

/**
 * Khóa hoặc mở khóa tài khoản người dùng.
 * @param {string} userId - ID của người dùng.
 * @param {boolean} lockStatus - true để khóa, false để mở khóa.
 * @returns {Promise<object>} Trạng thái người dùng đã cập nhật.
 */
const toggleUserLockStatus = async (userId, lockStatus) => {
    try {
        // Giả định endpoint: PUT /api/admin/users/{userId}/lock
        const response = await api.put(`/admin/users/${userId}/lock`, { isLocked: lockStatus });
        return response.data;
    } catch (error) {
        console.error(`Lỗi khi thay đổi trạng thái khóa của người dùng ${userId} (Admin):`, error.response?.data || error.message);
        throw error;
    }
};

// --- 2. Quản lý Dữ liệu Thực phẩm (Food Data Management) ---

/**
 * Lấy danh sách tất cả thực phẩm (chỉ dành cho Admin).
 * @returns {Promise<Array<object>>} Danh sách thực phẩm.
 */
const getAllFoods = async () => {
    try {
        // Giả định endpoint: GET /api/admin/foods
        const response = await api.get('/admin/foods');
        return response.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách Thực phẩm (Admin):', error.response?.data || error.message);
        throw error;
    }
};

/**
 * Tạo một mục thực phẩm mới.
 * @param {object} foodData - Dữ liệu thực phẩm: { foodName, calories, protein, carb, fat }
 * @returns {Promise<object>} Thực phẩm đã tạo.
 */
const createFood = async (foodData) => {
    try {
        // Giả định endpoint: POST /api/admin/foods
        const response = await api.post('/admin/foods', foodData);
        return response.data;
    } catch (error) {
        console.error('Lỗi khi tạo Thực phẩm (Admin):', error.response?.data || error.message);
        throw error;
    }
};

/**
 * Cập nhật một mục thực phẩm.
 * @param {string} foodId - ID của thực phẩm.
 * @param {object} foodData - Dữ liệu cập nhật.
 * @returns {Promise<object>} Thực phẩm đã cập nhật.
 */
const updateFood = async (foodId, foodData) => {
    try {
        // Giả định endpoint: PUT /api/admin/foods/{foodId}
        const response = await api.put(`/admin/foods/${foodId}`, foodData);
        return response.data;
    } catch (error) {
        console.error(`Lỗi khi cập nhật Thực phẩm ${foodId} (Admin):`, error.response?.data || error.message);
        throw error;
    }
};

/**
 * Xóa một mục thực phẩm.
 * @param {string} foodId - ID của thực phẩm.
 * @returns {Promise<void>}
 */
const deleteFood = async (foodId) => {
    try {
        // Giả định endpoint: DELETE /api/admin/foods/{foodId}
        await api.delete(`/admin/foods/${foodId}`);
    } catch (error) {
        console.error(`Lỗi khi xóa Thực phẩm ${foodId} (Admin):`, error.response?.data || error.message);
        throw error;
    }
};

// --- 3. Quản lý AI (AI Management) ---

/**
 * Kích hoạt quá trình huấn luyện lại mô hình AI (chỉ dành cho Admin).
 * @returns {Promise<object>} Trạng thái của quá trình huấn luyện (ví dụ: { status: 'STARTED', jobId: 'ai-train-123' }).
 */
const triggerAIRetraining = async () => {
    try {
        // Giả định endpoint: POST /api/admin/ai/retrain (Quá trình này thường là bất đồng bộ)
        const response = await api.post('/admin/ai/retrain');
        return response.data;
    } catch (error) {
        console.error('Lỗi khi kích hoạt huấn luyện lại AI (Admin):', error.response?.data || error.message);
        throw error;
    }
};


const adminService = {
    // User Management
    getAllUsers,
    getUserDetails,
    toggleUserLockStatus,

    // Food Management
    getAllFoods,
    createFood,
    updateFood,
    deleteFood,

    // AI Management
    triggerAIRetraining, // THÊM HÀM NÀY
};

export default adminService;