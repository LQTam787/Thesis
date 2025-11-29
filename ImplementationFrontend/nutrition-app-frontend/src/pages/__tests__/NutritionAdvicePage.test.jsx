// src/pages/__tests__/NutritionAdvicePage.test.jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import NutritionAdvicePage from '../NutritionAdvicePage';
import aiService from '../../services/aiService';
import { vi } from 'vitest';

// Mock aiService
vi.mock('../../services/aiService', () => ({
    default: {
        getNutritionAdvice: vi.fn(),
        analyzeFoodImage: vi.fn(),
    },
}));

// Mock lucide-react icons
vi.mock('lucide-react', () => ({
    Send: () => <div data-testid="send-icon" />,
    Camera: () => <div data-testid="camera-icon" />,
    Image: () => <div data-testid="image-icon" />,
    X: () => <div data-testid="x-icon" />,
}));

// Mock Redux store
const mockStore = configureStore([]);
const initialState = {
    auth: {
        user: { id: 'user123' },
    },
};
let store;

const renderWithProviders = (component) => {
    store = mockStore(initialState);
    return render(
        <Provider store={store}>
            {component}
        </Provider>
    );
};

describe('NutritionAdvicePage', () => {
    beforeAll(() => {
        // JSDOM doesn't implement scrollIntoView, so we mock it.
        window.HTMLElement.prototype.scrollIntoView = vi.fn();
    });

    beforeEach(() => {
        // Reset mocks before each test
        vi.clearAllMocks();
        // Default mock implementations
        aiService.getNutritionAdvice.mockResolvedValue({ text: 'Đây là lời khuyên cho bạn.' });
        aiService.analyzeFoodImage.mockResolvedValue({ recognizedFood: 'Phở Bò', calories: 450 });
    });

    test('renders initial welcome message from AI', () => {
        renderWithProviders(<NutritionAdvicePage />);
        expect(screen.getByText('Trò chuyện với AI Dinh Dưỡng')).toBeInTheDocument();
        expect(screen.getByText(/Chào bạn! Tôi là AI tư vấn dinh dưỡng của bạn./i)).toBeInTheDocument();
    });

    test('allows user to send a text message and receive a response', async () => {
        let resolveGetNutritionAdvicePromise;
        aiService.getNutritionAdvice.mockImplementation(() => {
            return new Promise(resolve => {
                resolveGetNutritionAdvicePromise = () => resolve({ text: 'Đây là lời khuyên cho bạn.' });
            });
        });

        renderWithProviders(<NutritionAdvicePage />);

        const input = screen.getByPlaceholderText(/Nhập câu hỏi hoặc mô tả bữa ăn của bạn.../i);
        const sendButton = screen.getByTestId('send-icon').closest('button');

        // Type a message
        fireEvent.change(input, { target: { value: 'Tôi nên ăn gì cho bữa sáng?' } });
        expect(input.value).toBe('Tôi nên ăn gì cho bữa sáng?');

        // Send the message
        fireEvent.click(sendButton);

        // Check if user message appears and input is cleared
        await waitFor(() => {
            expect(screen.getByText('Tôi nên ăn gì cho bữa sáng?')).toBeInTheDocument();
            expect(input.value).toBe('');
        });

        // Check for loading indicator and service call
        await waitFor(() => {
            expect(screen.getByText((content, element) => content.includes('AI đang xử lý...'))).toBeInTheDocument();
            expect(aiService.getNutritionAdvice).toHaveBeenCalledTimes(1);
            expect(aiService.getNutritionAdvice).toHaveBeenCalledWith('Tôi nên ăn gì cho bữa sáng?', 'user123');
        });

        // Resolve the promise to simulate AI response
        resolveGetNutritionAdvicePromise();

        // Check if AI response appears after the delay
        await waitFor(() => {
            expect(screen.getByText('Đây là lời khuyên cho bạn.')).toBeInTheDocument();
        });

        // Loading indicator should be gone
        expect(screen.queryByText((content, element) => content.includes('AI đang xử lý...'))).not.toBeInTheDocument();
    });


    test('handles error when sending a text message', async () => {
        aiService.getNutritionAdvice.mockRejectedValueOnce(new Error('AI service failed'));
        renderWithProviders(<NutritionAdvicePage />);

        const input = screen.getByPlaceholderText(/Nhập câu hỏi hoặc mô tả bữa ăn của bạn.../i);
        const sendButton = screen.getByTestId('send-icon').closest('button');

        fireEvent.change(input, { target: { value: 'Câu hỏi bị lỗi' } });
        fireEvent.click(sendButton);

        // Wait for the error message to be displayed
        await waitFor(() => {
            expect(screen.getByText('Xin lỗi, đã có lỗi xảy ra khi kết nối với AI.')).toBeInTheDocument();
        });
        expect(screen.queryByText((content, element) => content.includes('AI đang xử lý...'))).not.toBeInTheDocument();
    });

    test('allows user to upload an image for analysis and receive a response', async () => {
        let resolveAnalyzeFoodImagePromise;
        aiService.analyzeFoodImage.mockImplementation(() => {
            return new Promise(resolve => {
                resolveAnalyzeFoodImagePromise = () => resolve({ recognizedFood: 'Phở Bò', calories: 450 });
            });
        });

        renderWithProviders(<NutritionAdvicePage />);

        const file = new File(['(⌐□_□)'], 'pho.png', { type: 'image/png' });
        const fileInput = screen.getByTestId('file-input');
        const sendButton = screen.getByTestId('send-icon').closest('button');

        // Simulate file selection
        fireEvent.change(fileInput, { target: { files: [file] } });

        // Check for image preview
        await waitFor(() => {
            expect(screen.getByText(/pho.png - Sẵn sàng phân tích./i)).toBeInTheDocument();
        });

        // Input should be disabled when an image is selected
        const textInput = screen.getByPlaceholderText(/Nhập câu hỏi hoặc mô tả bữa ăn của bạn.../i);
        expect(textInput).toBeDisabled();

        // Click the button to analyze the image
        fireEvent.click(sendButton);

        // Check for user's upload message and that preview is cleared
        await waitFor(() => {
            expect(screen.getByText('Đang phân tích hình ảnh: pho.png')).toBeInTheDocument();
            expect(screen.queryByText(/pho.png - Sẵn sàng phân tích./i)).not.toBeInTheDocument();
        });

        // Check for loading indicator and service call
        await waitFor(() => {
            expect(screen.getByText((content, element) => content.includes('AI đang xử lý...'))).toBeInTheDocument();
            expect(aiService.analyzeFoodImage).toHaveBeenCalledTimes(1);
            expect(aiService.analyzeFoodImage).toHaveBeenCalledWith(file, 'user123');
        });

        // Resolve the promise to simulate AI response
        resolveAnalyzeFoodImagePromise();

        // Check if AI analysis result appears
        await waitFor(() => {
            expect(screen.getByText(/Kết quả nhận dạng: Phở Bò. Ước tính Calories: 450 kcal./i)).toBeInTheDocument();
        });
        expect(screen.queryByText((content, element) => content.includes('AI đang xử lý...'))).not.toBeInTheDocument();
    });

    test('handles error during image analysis', async () => {
        aiService.analyzeFoodImage.mockRejectedValueOnce(new Error('Vision API failed'));
        renderWithProviders(<NutritionAdvicePage />);

        const file = new File(['(⌐□_□)'], 'food.jpg', { type: 'image/jpeg' });
        const fileInput = screen.getByTestId('file-input');
        const sendButton = screen.getByTestId('send-icon').closest('button');

        fireEvent.change(fileInput, { target: { files: [file] } });
        await waitFor(() => fireEvent.click(sendButton));

        // Wait for the error message
        await waitFor(() => {
            expect(screen.getByText('Xin lỗi, không thể phân tích hình ảnh này.')).toBeInTheDocument();
        });
        expect(screen.queryByText((content, element) => content.includes('AI đang xử lý...'))).not.toBeInTheDocument();
    });

    test('send button is disabled when input is empty and no image is selected', () => {
        renderWithProviders(<NutritionAdvicePage />);
        const sendButton = screen.getByTestId('send-icon').closest('button');
        expect(sendButton).toBeDisabled();
    });

    test('selecting an image clears the text input', async () => {
        renderWithProviders(<NutritionAdvicePage />);
        const input = screen.getByPlaceholderText(/Nhập câu hỏi hoặc mô tả bữa ăn của bạn.../i);
        const fileInput = screen.getByTestId('file-input');

        fireEvent.change(input, { target: { value: 'Some text' } });
        expect(input.value).toBe('Some text');

        const file = new File([''], 'image.png', { type: 'image/png' });
        fireEvent.change(fileInput, { target: { files: [file] } });

        await waitFor(() => {
            expect(input.value).toBe('');
        });
    });

    test('typing in the input clears the selected image', async () => {
        renderWithProviders(<NutritionAdvicePage />);
        const input = screen.getByPlaceholderText(/Nhập câu hỏi hoặc mô tả bữa ăn của bạn.../i);
        const fileInput = screen.getByTestId('file-input');

        const file = new File([''], 'image.png', { type: 'image/png' });
        fireEvent.change(fileInput, { target: { files: [file] } });

        await waitFor(() => {
            expect(screen.getByText(/image.png - Sẵn sàng phân tích./i)).toBeInTheDocument();
        });

        fireEvent.change(input, { target: { value: 'a' } });

        await waitFor(() => {
            expect(screen.queryByText(/image.png - Sẵn sàng phân tích./i)).not.toBeInTheDocument();
        });
    });

    test('clicking the "X" button removes the image preview', async () => {
        renderWithProviders(<NutritionAdvicePage />);
        const fileInput = screen.getByTestId('file-input');

        const file = new File([''], 'image.png', { type: 'image/png' });
        fireEvent.change(fileInput, { target: { files: [file] } });

        await waitFor(() => {
            expect(screen.getByText(/image.png - Sẵn sàng phân tích./i)).toBeInTheDocument();
        });

        const removeButton = screen.getByTestId('remove-image-button');
        fireEvent.click(removeButton);

        await waitFor(() => {
            expect(screen.queryByText(/image.png - Sẵn sàng phân tích./i)).not.toBeInTheDocument();
        });
    });
});
