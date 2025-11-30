// test/FoodDataManagementPage.test.jsx
import React from 'react';
import { render, fireEvent, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import FoodDataManagementPage from "./FoodDataManagementPage.jsx";
import adminService from "../../services/adminService.js";
import userEvent from "@testing-library/user-event";

vi.mock('../../services/adminService.js', () => ({
    default: {
        getAllFoods: vi.fn(),
        createFood: vi.fn(),
        updateFood: vi.fn(),
        deleteFood: vi.fn(),
    }
}));

const mockFoods = [
    { id: 'f1', foodName: 'Gạo trắng', calories: 130, protein: 2.7, carb: 28, fat: 0.3, description: '100g cơm trắng' },
    { id: 'f2', foodName: 'Thịt bò (thăn)', calories: 250, protein: 26, carb: 0, fat: 17, description: '100g thịt bò nạc' },
    { id: 'f3', foodName: 'Trứng gà', calories: 155, protein: 13, carb: 1.1, fat: 11, description: '100g trứng (khoảng 2 quả)' },
];

describe('FoodDataManagementPage', () => {
    beforeEach(() => {
        adminService.getAllFoods.mockReset();
        adminService.createFood.mockReset();
        adminService.updateFood.mockReset();
        adminService.deleteFood.mockReset();
        vi.spyOn(window, 'confirm').mockReturnValue(true);
        vi.spyOn(window, 'alert').mockImplementation(() => {});
    });

    afterEach(() => {
        vi.restoreAllMocks();
    });

    it('displays initial list of foods', async () => {
        adminService.getAllFoods.mockResolvedValue(mockFoods);

        render(<FoodDataManagementPage />);

        expect(screen.getByText('Đang tải danh sách thực phẩm...')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.queryByText('Đang tải danh sách thực phẩm...')).not.toBeInTheDocument();
            expect(screen.getByText('Quản lý Dữ liệu Thực phẩm (Admin)')).toBeInTheDocument();
            expect(screen.getByText('Gạo trắng')).toBeInTheDocument();
            expect(screen.getByText('Thịt bò (thăn)')).toBeInTheDocument();
            expect(screen.getByText('Trứng gà')).toBeInTheDocument();
        });
    });

    it('shows loading and error messages', async () => {
        adminService.getAllFoods.mockRejectedValue(new Error('Unable to fetch'));

        render(<FoodDataManagementPage />);

        expect(screen.getByText('Đang tải danh sách thực phẩm...')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByText('Lỗi: Không thể tải danh sách Thực phẩm. Bạn có phải là Admin không?')).toBeInTheDocument();
            expect(screen.getByText('Gạo trắng')).toBeInTheDocument();
            expect(screen.getByText('Thịt bò (thăn)')).toBeInTheDocument();
            expect(screen.getByText('Trứng gà')).toBeInTheDocument();
        });
    });

    it('filters foods by search term', async () => {
        adminService.getAllFoods.mockResolvedValue(mockFoods);
        render(<FoodDataManagementPage />);

        await waitFor(() => expect(screen.getByText('Gạo trắng')).toBeInTheDocument());

        const searchInput = screen.getByPlaceholderText('Tìm kiếm theo Tên hoặc Mô tả...');
        await userEvent.type(searchInput, 'Gạo');
        
        expect(screen.getByText('Gạo trắng')).toBeInTheDocument();
        expect(screen.queryByText('Thịt bò (thăn)')).not.toBeInTheDocument();
        expect(screen.queryByText('Trứng gà')).not.toBeInTheDocument();

        await userEvent.clear(searchInput);
        await userEvent.type(searchInput, 'Trứng');
        expect(screen.queryByText('Gạo trắng')).not.toBeInTheDocument();
        expect(screen.queryByText('Thịt bò (thăn)')).not.toBeInTheDocument();
        expect(screen.getByText('Trứng gà')).toBeInTheDocument();
    });

    it('opens and closes the create food modal', async () => {
        adminService.getAllFoods.mockResolvedValue(mockFoods);
        render(<FoodDataManagementPage />);

        await waitFor(() => expect(screen.getByText('Gạo trắng')).toBeInTheDocument());

        fireEvent.click(screen.getByText('Thêm Thực phẩm Mới'));
        expect(screen.getByText('Thêm Dữ liệu Thực phẩm')).toBeInTheDocument();

        fireEvent.click(screen.getByRole('button', { name: /Đóng/i }));
        expect(screen.queryByText('Thêm Dữ liệu Thực phẩm')).not.toBeInTheDocument();
    });

    it('creates a new food item', async () => {
        adminService.getAllFoods.mockResolvedValue(mockFoods);
        adminService.createFood.mockResolvedValue({ id: 'f4', foodName: 'New Food', calories: 100, protein: 10, carb: 10, fat: 10 });
        render(<FoodDataManagementPage />);

        await waitFor(() => expect(screen.getByText('Gạo trắng')).toBeInTheDocument());

        fireEvent.click(screen.getByText('Thêm Thực phẩm Mới'));
        
        await userEvent.type(screen.getByLabelText('Tên Thực phẩm'), 'New Food');
        await userEvent.type(screen.getByLabelText('Mô tả/Đơn vị (VD: 100g, 1 bát)'), 'Mô tả mới');
        await userEvent.type(screen.getByLabelText('Calo (kcal)'), '100');
        await userEvent.type(screen.getByLabelText('Protein (g)'), '10');
        await userEvent.type(screen.getByLabelText('Carb (g)'), '10');
        await userEvent.type(screen.getByLabelText('Fat (Chất béo) (g)'), '10');
        
        fireEvent.click(screen.getByText('Thêm Thực phẩm'));
        
        await waitFor(() => {
            expect(screen.getByText('Đã thêm thực phẩm mới: New Food')).toBeInTheDocument();
            expect(adminService.createFood).toHaveBeenCalledWith({
                foodName: 'New Food',
                description: 'Mô tả mới',
                calories: 100,
                protein: 10,
                carb: 10,
                fat: 10,
            });
        });
    });

    // it('edits an existing food item', async () => {
    //     // Setup: Initial load will return original foods
    //     const updatedFoods = [
    //         { id: 'f1', foodName: 'Updated Food', calories: 200, protein: 2.7, carb: 28, fat: 0.3, description: '100g cơm trắng' },
    //         mockFoods[1],
    //         mockFoods[2],
    //     ];
        
    //     // First call: initial load
    //     // Second call: after update
    //     adminService.getAllFoods
    //         .mockResolvedValueOnce(mockFoods)
    //         .mockResolvedValueOnce(updatedFoods);
        
    //     adminService.updateFood.mockResolvedValue(updatedFoods[0]);
        
    //     render(<FoodDataManagementPage />);

    //     await waitFor(() => expect(screen.getByText('Gạo trắng')).toBeInTheDocument());

    //     fireEvent.click(screen.getAllByText('Sửa')[0]);

    //     await userEvent.clear(screen.getByLabelText('Tên Thực phẩm'));
    //     await userEvent.type(screen.getByLabelText('Tên Thực phẩm'), 'Updated Food');
    //     await userEvent.clear(screen.getByLabelText('Calo (kcal)'));
    //     await userEvent.type(screen.getByLabelText('Calo (kcal)'), '200');

    //     fireEvent.click(screen.getByText('Lưu Thay Đổi'));

    //     await waitFor(() => {
    //         expect(screen.getByText('Đã cập nhật thực phẩm: Updated Food')).toBeInTheDocument();
    //     }, { timeout: 3000 });
        
    //     expect(adminService.updateFood).toHaveBeenCalledWith(mockFoods[0].id, {
    //         foodName: 'Updated Food',
    //         description: '100g cơm trắng',
    //         calories: 200,
    //         protein: 2.7,
    //         carb: 28,
    //         fat: 0.3,
    //     });
    // });

    it('deletes a food item', async () => {
        adminService.getAllFoods.mockResolvedValue(mockFoods);
        adminService.deleteFood.mockResolvedValue(mockFoods[0]);
        render(<FoodDataManagementPage />);

        await waitFor(() => expect(screen.getByText('Gạo trắng')).toBeInTheDocument());

        window.confirm = vi.fn(() => true);
        fireEvent.click(screen.getAllByText('Xóa')[0]);
        await waitFor(() => expect(adminService.deleteFood).toHaveBeenCalledWith(mockFoods[0].id));
        expect(screen.getByText('Đã xóa thực phẩm: Gạo trắng')).toBeInTheDocument();
    });

    it('handles delete cancellation', async () => {
        adminService.getAllFoods.mockResolvedValue(mockFoods);
        adminService.deleteFood.mockResolvedValue(mockFoods[0]);
        render(<FoodDataManagementPage />);

        await waitFor(() => expect(screen.getByText('Gạo trắng')).toBeInTheDocument());

        window.confirm = vi.fn(() => false);
        fireEvent.click(screen.getAllByText('Xóa')[0]);
        await waitFor(() => expect(adminService.deleteFood).not.toHaveBeenCalled());
        expect(screen.getByText('Gạo trắng')).toBeInTheDocument();
    });
});