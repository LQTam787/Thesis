import axios from 'axios';

const AI_SERVICE_BASE_URL = 'http://localhost:5000/api'; // Thay đổi nếu AI Service chạy ở cổng khác

const aiApi = axios.create({
  baseURL: AI_SERVICE_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

interface NlpConsultPayload {
  prompt: string;
  history?: any[]; // Lịch sử trò chuyện, có thể là mảng các đối tượng { role: 'user' | 'assistant', content: string }
}

interface EditMealPlanPayload {
  currentPlan: any; // Cấu trúc thực đơn hiện tại
  editRequest: string; // Yêu cầu chỉnh sửa bằng ngôn ngữ tự nhiên
}

/**
 * Gửi yêu cầu tư vấn dinh dưỡng đến AI Service.
 * @param prompt Câu hỏi hoặc thông tin người dùng cung cấp.
 * @param history Lịch sử trò chuyện để AI có ngữ cảnh.
 * @returns Promise chứa phản hồi từ AI Service.
 */
export const getNutritionalAdvice = async (prompt: string, history?: any[]) => {
  try {
    const payload: NlpConsultPayload = { prompt, history };
    const response = await aiApi.post('/nlp/consult', payload);
    return response.data;
  } catch (error) {
    console.error('Error getting nutritional advice:', error);
    throw error;
  }
};

/**
 * Gửi yêu cầu chỉnh sửa thực đơn đến AI Service.
 * @param currentPlan Thực đơn hiện tại cần chỉnh sửa.
 * @param editRequest Yêu cầu chỉnh sửa bằng ngôn ngữ tự nhiên.
 * @returns Promise chứa thực đơn đã được AI chỉnh sửa.
 */
export const editMealPlan = async (currentPlan: any, editRequest: string) => {
  try {
    const payload: EditMealPlanPayload = { currentPlan, editRequest };
    const response = await aiApi.post('/nlp/edit-meal-plan', payload); // Giả định có endpoint này
    return response.data;
  } catch (error) {
    console.error('Error editing meal plan:', error);
    throw error;
  }
};

// Các service AI khác có thể được thêm vào đây sau này
