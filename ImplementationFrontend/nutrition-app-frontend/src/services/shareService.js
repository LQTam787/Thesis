// src/services/shareService.js

import api from './api'; // Import api instance (Backend chính với JWT)

// --- 1. Quản lý Bài đăng (Post) ---

/**
 * Lấy danh sách các bài đăng trên News Feed.
 * @returns {Promise<Array<object>>} Danh sách bài đăng.
 */
const getCommunityFeed = async () => {
    try {
        // Giả định endpoint: GET /api/posts/feed
        const response = await api.get('/posts/feed');
        return response.data;
    } catch (error) {
        console.error('Lỗi khi tải News Feed:', error.response?.data || error.message);
        throw error;
    }
};

/**
 * Tạo một bài đăng mới.
 * @param {object} postData - Dữ liệu bài đăng: { content, planId (optional), logId (optional), imageUrl (optional) }
 * @returns {Promise<object>} Bài đăng đã tạo.
 */
const createPost = async (postData) => {
    try {
        // Giả định endpoint: POST /api/posts
        const response = await api.post('/posts', postData);
        return response.data;
    } catch (error) {
        console.error('Lỗi khi tạo bài đăng:', error.response?.data || error.message);
        throw error;
    }
};

// --- 2. Tương tác (Likes và Comments) ---

/**
 * Thao tác Thích (Like) hoặc Bỏ thích (Unlike) một bài đăng.
 * @param {string} postId - ID của bài đăng.
 * @returns {Promise<object>} Trạng thái Like mới.
 */
const toggleLike = async (postId) => {
    try {
        // Giả định endpoint: POST /api/posts/{postId}/like
        // Backend sẽ tự động xác định là Like hay Unlike
        const response = await api.post(`/posts/${postId}/like`);
        return response.data;
    } catch (error) {
        console.error(`Lỗi khi thao tác Like bài đăng ${postId}:`, error.response?.data || error.message);
        throw error;
    }
};

/**
 * Thêm một bình luận mới vào bài đăng.
 * @param {string} postId - ID của bài đăng.
 * @param {string} content - Nội dung bình luận.
 * @returns {Promise<object>} Bình luận đã tạo.
 */
const addComment = async (postId, content) => {
    try {
        // Giả định endpoint: POST /api/posts/{postId}/comments
        const response = await api.post(`/posts/${postId}/comments`, { content });
        return response.data;
    } catch (error) {
        console.error(`Lỗi khi thêm bình luận vào bài đăng ${postId}:`, error.response?.data || error.message);
        throw error;
    }
};


const shareService = {
    getCommunityFeed,
    createPost,
    toggleLike,
    addComment,
};

export default shareService;