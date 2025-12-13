// src/services/aiService.test.js

import { describe, it, expect, vi, afterEach, beforeEach } from 'vitest';
// Import the named export `aiApi` which is used by `aiService`
import { aiApi } from './api';
import aiService from './aiService';

// Mock the entire 'api' module. Vitest will automatically handle named exports.
vi.mock('./api');

describe('aiService', () => {
    afterEach(() => {
        // Clear all mocks after each test
        vi.restoreAllMocks();
    });

    // Test suite for getNutritionAdvice
    describe('getNutritionAdvice', () => {
        it('should call the AI NLP API with the correct parameters and return data', async () => {
            const mockResponse = { data: { text: 'Eat more greens.', planSuggestion: [] } };
            // Mock the post method on the `aiApi` object
            aiApi.post.mockResolvedValue(mockResponse);

            const message = 'What should I eat?';
            const userId = 'user123';

            const result = await aiService.getNutritionAdvice(message, userId);

            // Expect the `aiApi.post` method to have been called correctly
            expect(aiApi.post).toHaveBeenCalledWith('/advice/chat', { message, userId });
            expect(result).toEqual(mockResponse.data);
        });

        it('should throw an error if the API call fails', async () => {
            const errorMessage = 'Network Error';
            aiApi.post.mockRejectedValue(new Error(errorMessage));

            const message = 'What should I eat?';
            const userId = 'user123';

            await expect(aiService.getNutritionAdvice(message, userId)).rejects.toThrow(errorMessage);
        });
    });

    // Test suite for analyzeFoodImage
    describe('analyzeFoodImage', () => {
        const FormDataOriginal = global.FormData;
        let mockAppend;
        let mockInstance;

        beforeEach(() => {
            mockAppend = vi.fn();
            mockInstance = { append: mockAppend };
            global.FormData = vi.fn(() => mockInstance);
        });

        afterEach(() => {
            global.FormData = FormDataOriginal;
        });

        it('should call the AI Vision API with FormData and return data', async () => {
            const mockResponse = { data: { recognizedFood: 'Salad', calories: 250 } };
            // Mock the post method on the `aiApi` object
            aiApi.post.mockResolvedValue(mockResponse);

            const imageFile = new File(['(⌐□_□)'], 'salad.png', { type: 'image/png' });
            const userId = 'user123';

            const result = await aiService.analyzeFoodImage(imageFile, userId);

            expect(global.FormData).toHaveBeenCalledTimes(1);
            expect(mockAppend).toHaveBeenCalledWith('image', imageFile);
            expect(mockAppend).toHaveBeenCalledWith('userId', userId);

            // Expect the `aiApi.post` method to have been called with the FormData
            expect(aiApi.post).toHaveBeenCalledWith(
                '/vision/analyze',
                mockInstance,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                }
            );

            expect(result).toEqual(mockResponse.data);
        });

        it('should throw an error if the API call fails', async () => {
            const errorMessage = 'Image processing failed';
            aiApi.post.mockRejectedValue(new Error(errorMessage));

            const imageFile = new File([], 'test.jpg');
            const userId = 'user123';

            await expect(aiService.analyzeFoodImage(imageFile, userId)).rejects.toThrow(errorMessage);
            
            expect(global.FormData).toHaveBeenCalledTimes(1);
        });
    });
});
