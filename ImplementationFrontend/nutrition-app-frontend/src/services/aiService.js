// src/services/aiService.js

import api from './api'; // Import api instance

// --- Hàm Tư vấn Dinh dưỡng bằng Văn bản (NLP) ---
const getNutritionAdvice = async (message, userId) => {
    try {
        // Gọi API của AI/NLP Service
        const response = await api.post('/advice/chat', {
            message,
            userId, // Gửi userId để AI cá nhân hóa phản hồi
        });

        // Giả định API trả về một đối tượng phản hồi có cấu trúc (structured response)
        // Ví dụ: { text: "Lời khuyên...", planSuggestion: [...] }
        return response.data;
    } catch (error) {
        console.error('Lỗi khi gọi AI Service (NLP):', error.response?.data || error.message);
        throw error;
    }
};

// --- Hàm Nhận dạng Món ăn bằng Hình ảnh (Vision) ---
const analyzeFoodImage = async (imageFile, userId) => {
    try {
        // Sử dụng FormData để gửi file ảnh
        const formData = new FormData();
        formData.append('image', imageFile);
        formData.append('userId', userId);

        // Cần ghi đè Content-Type để gửi FormData
        const response = await api.post('/vision/analyze', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });

        // Giả định API trả về thông tin về món ăn được nhận dạng, lượng calo, v.v.
        // Ví dụ: { recognizedFood: "Gà luộc", calories: 350, protein: 40 }
        return response.data;
    } catch (error) {
        console.error('Lỗi khi gọi AI Service (Vision):', error.response?.data || error.message);
        throw error;
    }
};

const aiService = {
    getNutritionAdvice,
    analyzeFoodImage,
};

export default aiService;
