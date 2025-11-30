// src/services/planService.js

import api from './api'; // Import api instance đã được cấu hình JWT Interceptor

// --- 1. Quản lý Kế hoạch Dinh dưỡng (Nutrition Plan) ---

// Lấy tất cả kế hoạch của người dùng
const getAllPlans = async () => {
    try {
        // Giả định endpoint: GET /api/plans
        const response = await api.get('/plans');
        return response.data; // Trả về danh sách kế hoạch
    } catch (error) {
        console.error('Lỗi khi lấy danh sách kế hoạch:', error.response?.data || error.message);
        throw error;
    }
};

// Lấy chi tiết một kế hoạch cụ thể
const getPlanDetails = async (planId) => {
    try {
        // Giả định endpoint: GET /api/plans/{planId}
        const response = await api.get(`/plans/${planId}`);
        return response.data; // Trả về chi tiết kế hoạch
    } catch (error) {
        console.error(`Lỗi khi lấy chi tiết kế hoạch ${planId}:`, error.response?.data || error.message);
        throw error;
    }
};

// Tạo kế hoạch mới
const createPlan = async (planData) => {
    try {
        // Giả định endpoint: POST /api/plans
        const response = await api.post('/plans', planData);
        return response.data; // Trả về kế hoạch đã tạo
    } catch (error) {
        console.error('Lỗi khi tạo kế hoạch:', error.response?.data || error.message);
        throw error;
    }
};

// --- 2. Quản lý Công thức nấu ăn (Recipe) ---

// Lấy chi tiết một công thức nấu ăn
const getRecipeDetail = async (recipeId) => {
    try {
        // Giả định endpoint: GET /api/recipes/{recipeId}
        const response = await api.get(`/recipes/${recipeId}`);
        return response.data; // Trả về chi tiết công thức
    } catch (error) {
        console.error(`Lỗi khi lấy chi tiết công thức ${recipeId}:`, error.response?.data || error.message);
        throw error;
    }
};


const planService = {
    getAllPlans,
    getPlanDetails,
    createPlan,
    getRecipeDetail,
};

export default planService;