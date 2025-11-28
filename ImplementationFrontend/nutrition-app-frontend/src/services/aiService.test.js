// src/services/aiService.test.js

import { describe, it, expect, vi, afterEach, beforeEach } from 'vitest';
import api from './api';
import aiService from './aiService';

// Mock the entire 'api' module
vi.mock('./api');

describe('aiService', () => {
    afterEach(() => {
        vi.restoreAllMocks();
    });

    // Test suite for getNutritionAdvice (remains unchanged)
    describe('getNutritionAdvice', () => {
        it('should call the AI NLP API with the correct parameters and return data', async () => {
            const mockResponse = { data: { text: 'Eat more greens.', planSuggestion: [] } };
            api.post.mockResolvedValue(mockResponse);
            const message = 'What should I eat?';
            const userId = 'user123';
            const result = await aiService.getNutritionAdvice(message, userId);
            expect(api.post).toHaveBeenCalledWith('/advice/chat', { message, userId });
            expect(result).toEqual(mockResponse.data);
        });

        it('should throw an error if the API call fails', async () => {
            const errorMessage = 'Network Error';
            api.post.mockRejectedValue(new Error(errorMessage));
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
            // 1. Create a mock for the `append` method
            mockAppend = vi.fn();
            // 2. This is the object that our mock FormData constructor will return
            mockInstance = { append: mockAppend };

            // 3. Create a regular function that can be used as a constructor
            function MockFormData() {
                return mockInstance;
            }

            // 4. Replace the global FormData with a mock of our valid constructor
            global.FormData = vi.fn(MockFormData);
        });

        afterEach(() => {
            // Restore the original FormData constructor
            global.FormData = FormDataOriginal;
        });

        it('should call the AI Vision API with FormData and return data', async () => {
            const mockResponse = { data: { recognizedFood: 'Salad', calories: 250 } };
            api.post.mockResolvedValue(mockResponse);

            const imageFile = new File(['(⌐□_□)'], 'salad.png', { type: 'image/png' });
            const userId = 'user123';

            const result = await aiService.analyzeFoodImage(imageFile, userId);

            // Check that our mock FormData constructor was called
            expect(global.FormData).toHaveBeenCalledTimes(1);

            // Check that append was called with the correct values on our mock instance
            expect(mockAppend).toHaveBeenCalledWith('image', imageFile);
            expect(mockAppend).toHaveBeenCalledWith('userId', userId);
            expect(mockAppend).toHaveBeenCalledTimes(2);

            // Check that api.post was called with the mock instance
            expect(api.post).toHaveBeenCalledWith(
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
            api.post.mockRejectedValue(new Error(errorMessage));

            const imageFile = new File([], 'test.jpg');
            const userId = 'user123';

            // The constructor error is now fixed, allowing the test to check for the actual API error
            await expect(aiService.analyzeFoodImage(imageFile, userId)).rejects.toThrow(errorMessage);
            
            // Check that FormData was still instantiated before the API call failed
            expect(global.FormData).toHaveBeenCalledTimes(1);
        });
    });
});
