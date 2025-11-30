// src/components/__tests__/RecipeDetail.test.jsx
import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter, useParams } from 'react-router-dom';
import RecipeDetail from '../RecipeDetail';
import planService from '../../services/planService';
import { vi } from 'vitest';

// --- MOCKING ---

// 1. Mock useParams để kiểm soát recipeId được truyền vào
const mockUseParams = vi.fn();

// SỬA LỖI: Sử dụng cú pháp async/await cho Partial Mocking
vi.mock('react-router-dom', async (importOriginal) => {
    // Lấy tất cả các export gốc (bao gồm BrowserRouter)
    const actual = await importOriginal();

    // Trả về tất cả các export gốc, và chỉ ghi đè useParams
    return {
        ...actual, // Giữ lại BrowserRouter, Link, v.v.
        useParams: () => mockUseParams(), // Ghi đè useParams
    };
});

// 2. Mock planService để kiểm soát kết quả API
vi.mock('../../services/planService', () => ({
    // Cung cấp default export để khớp với cách component import: import planService from '...'
    default: {
        getRecipeDetail: vi.fn(),
    },
}));

// Dữ liệu công thức mẫu (Mock Recipe Data)
const mockRecipeData = {
    recipeName: 'Ức Gà Xào Nấm Đơn Giản',
    description: 'Món ăn giàu protein, ít calo, phù hợp cho người ăn kiêng.',
    calories: 250,
    protein: 35,
    carb: 5,
    fat: 10,
    ingredients: [
        { quantity: 200, unit: 'g', name: 'Ức gà' },
        { quantity: 100, unit: 'g', name: 'Nấm hương' },
        { quantity: 1, unit: 'muỗng cà phê', name: 'Dầu ô liu' },
    ],
    steps: [
        'Bước 1: Thái ức gà thành miếng vừa ăn.',
        'Bước 2: Xào nấm và ức gà với dầu ô liu.',
        'Bước 3: Nêm gia vị vừa ăn và tắt bếp.',
    ],
};

// --- HELPER FUNCTION (Giúp việc render dễ hơn) ---

// Component Wrapper để bao bọc RecipeDetail trong Router context
const Wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

// --- TEST SUITE ---

describe('RecipeDetail Component', () => {

    beforeEach(() => {
        mockUseParams.mockReturnValue({ recipeId: '456' });
        vi.clearAllMocks();
    });

    // Test Case 1: Hiển thị trạng thái Loading
    test('Hiển thị trạng thái "Đang tải công thức..." khi đang fetch dữ liệu', () => {
        planService.getRecipeDetail.mockReturnValue(new Promise(() => {}));

        render(<RecipeDetail />, { wrapper: Wrapper });

        expect(screen.getByText('Đang tải công thức...')).toBeInTheDocument();
    });

    // Test Case 2: Hiển thị trạng thái Lỗi khi fetch thất bại
    test('Hiển thị thông báo lỗi khi không tìm thấy công thức', async () => {
        // Mock một lỗi Axios với cấu trúc phù hợp
        planService.getRecipeDetail.mockRejectedValue({
            response: {
                data: { message: 'Recipe not found' },
                status: 404
            },
            message: 'Request failed with status code 404'
        });

        render(<RecipeDetail />, { wrapper: Wrapper });

        await waitFor(() => {
            expect(screen.getByText(/Không tìm thấy công thức với ID: 456 hoặc lỗi tải dữ liệu./i)).toBeInTheDocument();
        });
    });

    // Test Case 3: Hiển thị đầy đủ thông tin công thức khi fetch thành công
    test('Hiển thị đầy đủ thông tin công thức khi fetch thành công', async () => {
        planService.getRecipeDetail.mockResolvedValue(mockRecipeData);

        render(<RecipeDetail />, { wrapper: Wrapper });

        await waitFor(() => {
            // Kiểm tra tên công thức
            expect(screen.getByRole('heading', { level: 1, name: mockRecipeData.recipeName })).toBeInTheDocument();
            // Kiểm tra mô tả
            expect(screen.getByText(mockRecipeData.description)).toBeInTheDocument();

            // Kiểm tra thông tin dinh dưỡng
            expect(screen.getByText('Calo (kcal)')).toBeInTheDocument();
            expect(screen.getByText(mockRecipeData.calories.toString())).toBeInTheDocument();
            expect(screen.getByText('Protein (g)')).toBeInTheDocument();
            expect(screen.getByText(mockRecipeData.protein.toString())).toBeInTheDocument();

            // Kiểm tra Nguyên liệu
            expect(screen.getByText(/Nguyên liệu/i)).toBeInTheDocument();
            mockRecipeData.ingredients.forEach(item => {
                expect(screen.getByText(`${item.quantity} ${item.unit} ${item.name}`)).toBeInTheDocument();
            });

            // Kiểm tra Các bước Nấu ăn
            expect(screen.getByText(/Các bước Nấu ăn/i)).toBeInTheDocument();
            mockRecipeData.steps.forEach(step => {
                expect(screen.getByText(step)).toBeInTheDocument();
                expect(screen.getByText((mockRecipeData.steps.indexOf(step) + 1).toString())).toBeInTheDocument();
            });
        });

        expect(planService.getRecipeDetail).toHaveBeenCalledTimes(1);
        expect(planService.getRecipeDetail).toHaveBeenCalledWith('456');
    });

    // Test Case 4: Hiển thị giá trị 'N/A' cho thông tin dinh dưỡng bị thiếu
    test('Hiển thị N/A cho thông tin dinh dưỡng bị thiếu (null/undefined)', async () => {
        const partialData = {
            ...mockRecipeData,
            calories: null, // Trường thiếu
            carb: undefined, // Trường thiếu
            protein: 0,
            fat: 10,
        };
        planService.getRecipeDetail.mockResolvedValue(partialData);

        render(<RecipeDetail />, { wrapper: Wrapper });

        await waitFor(() => {
            expect(screen.getAllByText('N/A')).toHaveLength(2); // Calo và Carb
            expect(screen.getByText(partialData.fat.toString())).toBeInTheDocument();
            expect(screen.getByText(partialData.protein.toString())).toBeInTheDocument();
        });
    });

    // Test Case 5: Hiển thị mô tả mặc định khi không có mô tả
    test('Hiển thị mô tả mặc định nếu description là null', async () => {
        const noDescriptionData = {
            ...mockRecipeData,
            description: null,
        };
        planService.getRecipeDetail.mockResolvedValue(noDescriptionData);

        render(<RecipeDetail />, { wrapper: Wrapper });

        await waitFor(() => {
            expect(screen.getByText('Mô tả ngắn gọn về món ăn.')).toBeInTheDocument();
        });
    });
});
