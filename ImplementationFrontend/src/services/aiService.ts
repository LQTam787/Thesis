import axios from 'axios';

const AI_SERVICE_BASE_URL = 'http://localhost:5000/api';

export const identifyFood = async (imageFile: File) => {
  try {
    const formData = new FormData();
    formData.append('image', imageFile);

    const response = await axios.post(`${AI_SERVICE_BASE_URL}/vision/recognize`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error identifying food:', error);
    throw error;
  }
};

export const getNutritionalAdvice = async (prompt: string, history?: any[]) => {
  try {
    const response = await axios.post(`${AI_SERVICE_BASE_URL}/nlp/consult`, { prompt, history });
    return response.data;
  } catch (error) {
    console.error('Error getting nutritional advice:', error);
    throw error;
  }
};

export const editMealPlan = async (currentPlan: any, editRequest: string) => {
  try {
    const response = await axios.post(`${AI_SERVICE_BASE_URL}/nlp/edit-meal-plan`, { currentPlan, editRequest });
    return response.data;
  } catch (error) {
    console.error('Error editing meal plan:', error);
    throw error;
  }
};
