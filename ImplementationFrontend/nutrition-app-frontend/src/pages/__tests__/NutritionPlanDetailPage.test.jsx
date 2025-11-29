// src/pages/__tests__/NutritionPlanDetailPage.test.jsx
import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import NutritionPlanDetailPage from '../NutritionPlanDetailPage';
import planService from '../../services/planService';
import { vi } from 'vitest';

// Mock planService
vi.mock('../../services/planService', () => ({
    default: {
        getPlanDetails: vi.fn(),
    },
}));

// Mock lucide-react icons for simplicity
vi.mock('lucide-react', () => ({
    ArrowLeft: () => <div data-testid="arrow-left-icon" />,
    Clock: () => <div data-testid="clock-icon" />,
    Zap: () => <div data-testid="zap-icon" />,
    Soup: () => <div data-testid="soup-icon" />,
    Utensils: () => <div data-testid="utensils-icon" />,
}));

// Mock data for a successful response
const mockPlan = {
    id: 'plan1',
    name: 'Kế hoạch Giảm Cân Cấp Tốc',
    description: 'Một kế hoạch ăn kiêng nghiêm ngặt trong 7 ngày để giảm cân nhanh chóng.',
    targetDailyCalories: 1500,
    durationDays: 7,
    goal: 'WEIGHT_LOSS',
    dailyPlans: [
        {
            day: 1,
            meals: [
                { name: 'Bữa sáng', recipe: { id: 'recipe1', name: 'Yến mạch hoa quả' } },
                { name: 'Bữa trưa', recipe: { id: 'recipe2', name: 'Salad ức gà' } },
                { name: 'Bữa tối', recipe: { id: 'recipe3', name: 'Cá hồi áp chảo' } },
            ],
        },
        {
            day: 2,
            meals: [
                { name: 'Bữa sáng', recipe: { id: 'recipe4', name: 'Trứng luộc và rau' } },
                { name: 'Bữa trưa', recipe: { id: 'recipe5', name: 'Thịt bò xào bông cải' } },
            ],
        },
    ],
};

// Helper function to render the component within a router context
const renderWithRouter = (planId) => {
    return render(
        <MemoryRouter initialEntries={[`/plans/${planId}`]}>
            <Routes>
                <Route path="/plans/:planId" element={<NutritionPlanDetailPage />} />
            </Routes>
        </MemoryRouter>
    );
};

describe('NutritionPlanDetailPage', () => {
    beforeEach(() => {
        // Reset mocks before each test
        vi.clearAllMocks();
    });

    test('displays loading state initially', () => {
        planService.getPlanDetails.mockReturnValue(new Promise(() => {})); // Never resolves
        renderWithRouter('plan1');
        expect(screen.getByText('Đang tải chi tiết kế hoạch...')).toBeInTheDocument();
    });

    test('displays error message on API failure', async () => {
        const planId = 'invalid-id';
        const errorMessage = `Không tìm thấy kế hoạch với ID: ${planId} hoặc lỗi tải dữ liệu.`;
        planService.getPlanDetails.mockRejectedValue(new Error('API Error'));

        renderWithRouter(planId);

        await waitFor(() => {
            expect(screen.getByText(errorMessage)).toBeInTheDocument();
        });
        expect(screen.queryByText('Đang tải chi tiết kế hoạch...')).not.toBeInTheDocument();
    });

    test('renders plan details successfully after loading', async () => {
        planService.getPlanDetails.mockResolvedValue(mockPlan);
        renderWithRouter('plan1');

        // Wait for loading to finish and content to appear
        await waitFor(() => {
            expect(screen.getByText('Kế hoạch Giảm Cân Cấp Tốc')).toBeInTheDocument();
        });

        // Check header and description
        expect(screen.getByText(mockPlan.name)).toBeInTheDocument();
        expect(screen.getByText(mockPlan.description)).toBeInTheDocument();

        // Check summary cards
        expect(screen.getByText('Tổng Calo Mục tiêu (hàng ngày)')).toBeInTheDocument();
        expect(screen.getByText(`${mockPlan.targetDailyCalories} kcal`)).toBeInTheDocument();
        expect(screen.getByText('Thời gian Kế hoạch')).toBeInTheDocument();
        expect(screen.getByText(`${mockPlan.durationDays} ngày`)).toBeInTheDocument();
        expect(screen.getByText('Mục tiêu Chính')).toBeInTheDocument();
        expect(screen.getByText('Giảm Cân')).toBeInTheDocument(); // WEIGHT_LOSS -> Giảm Cân
    });

    test('renders daily meal schedules correctly', async () => {
        planService.getPlanDetails.mockResolvedValue(mockPlan);
        renderWithRouter('plan1');

        await waitFor(() => {
            expect(screen.getByText('Ngày 1')).toBeInTheDocument();
        });

        // Check Day 1
        expect(screen.getByText('Ngày 1')).toBeInTheDocument();
        expect(screen.getByText('Yến mạch hoa quả')).toBeInTheDocument();
        expect(screen.getByText('Salad ức gà')).toBeInTheDocument();
        expect(screen.getByText('Cá hồi áp chảo')).toBeInTheDocument();

        // Check Day 2
        expect(screen.getByText('Ngày 2')).toBeInTheDocument();
        expect(screen.getByText('Trứng luộc và rau')).toBeInTheDocument();
        expect(screen.getByText('Thịt bò xào bông cải')).toBeInTheDocument();
    });

    test('renders correct links to recipes', async () => {
        planService.getPlanDetails.mockResolvedValue(mockPlan);
        renderWithRouter('plan1');

        await waitFor(() => {
            // Find all "Xem Công thức" links
            const recipeLinks = screen.getAllByText('Xem Công thức');
            expect(recipeLinks.length).toBe(5); // 3 meals on day 1 + 2 meals on day 2

            // Check href attributes
            expect(recipeLinks[0].closest('a')).toHaveAttribute('href', '/recipes/recipe1');
            expect(recipeLinks[1].closest('a')).toHaveAttribute('href', '/recipes/recipe2');
            expect(recipeLinks[2].closest('a')).toHaveAttribute('href', '/recipes/recipe3');
            expect(recipeLinks[3].closest('a')).toHaveAttribute('href', '/recipes/recipe4');
            expect(recipeLinks[4].closest('a')).toHaveAttribute('href', '/recipes/recipe5');
        });
    });

    test('renders "Quay lại" link correctly', async () => {
        planService.getPlanDetails.mockResolvedValue(mockPlan);
        renderWithRouter('plan1');

        await waitFor(() => {
            const backLink = screen.getByText('Quay lại Danh sách Kế hoạch');
            expect(backLink).toBeInTheDocument();
            expect(backLink.closest('a')).toHaveAttribute('href', '/plans');
        });
    });

    test('handles plan with different goal (e.g., Tăng Cơ)', async () => {
        const muscleGainPlan = { ...mockPlan, goal: 'MUSCLE_GAIN' };
        planService.getPlanDetails.mockResolvedValue(muscleGainPlan);
        renderWithRouter('plan-muscle');

        await waitFor(() => {
            expect(screen.getByText('Tăng Cơ')).toBeInTheDocument();
        });
    });

    test('handles plan with no specific description', async () => {
        const planWithoutDesc = { ...mockPlan, description: null };
        planService.getPlanDetails.mockResolvedValue(planWithoutDesc);
        renderWithRouter('plan-no-desc');

        await waitFor(() => {
            // Check for the default description text
            expect(screen.getByText('Chi tiết về kế hoạch dinh dưỡng cá nhân.')).toBeInTheDocument();
        });
    });
});
