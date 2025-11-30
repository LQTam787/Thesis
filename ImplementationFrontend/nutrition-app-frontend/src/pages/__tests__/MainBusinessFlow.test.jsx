import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import DashboardPage from '../DashboardPage';
import DailyLogInputPage from '../DailyLogInputPage';
import NutritionPlanPage from '../NutritionPlanPage';
import authService from '../../services/authService';
import logService from '../../services/logService';
import planService from '../../services/planService';
import aiService from '../../services/aiService';
import { vi } from 'vitest';

// Mock authService
vi.mock('../../services/authService', () => ({
    default: {
        performLogout: vi.fn(),
    },
}));

// Mock logService
vi.mock('../../services/logService', () => ({
    default: {
        createDailyLog: vi.fn(() => Promise.resolve({})),
        getDailyLogs: vi.fn(() => Promise.resolve([])),
    },
}));

// Mock planService
vi.mock('../../services/planService', () => ({
    default: {
        getAllPlans: vi.fn(() => Promise.resolve([])),
        createMealPlan: vi.fn(() => Promise.resolve({})),
        updateMealPlan: vi.fn(() => Promise.resolve({})),
        deleteMealPlan: vi.fn(() => Promise.resolve({})),
    },
}));

// Mock aiService
vi.mock('../../services/aiService', () => ({
    default: {
        analyzeFoodImage: vi.fn(() => Promise.resolve({ recognizedFood: 'mocked food', calories: 100 })),
    },
}));


// Mock Redux store
const mockStore = configureStore([]);
const initialState = {
    auth: {
        user: { id: 'user123', username: 'testuser', role: 'USER', token: 'fake-token' },
    },
};
let store;

// Helper function to render components with necessary providers
const renderWithProviders = (component, { route = '/', initialState: customInitialState = initialState } = {}) => {
    window.history.pushState({}, 'Test page', route);
    store = mockStore(customInitialState);
    return render(
        <Provider store={store}>
            <BrowserRouter>
                <Routes>
                    <Route path="/dashboard/*" element={component} />
                    <Route path="/advice" element={<div>Advice Page Content</div>} />
                    <Route path="/log" element={<DailyLogInputPage />} />
                    <Route path="/report" element={<div>Progress Report Page Content</div>} />
                    <Route path="/plans" element={<NutritionPlanPage />} />
                    <Route path="/plans/new" element={<div>Create New Plan Page Content</div>} /> {/* Thêm route này */}
                    <Route path="/profile" element={<div>Profile Management Page Content</div>} />
                    <Route path="/community" element={<div>Community Feed Page Content</div>} />
                    <Route path="/admin/users" element={<div>Admin Users Management Page Content</div>} />
                    {/* Add other routes that DashboardPage links to */}
                </Routes>
            </BrowserRouter>
        </Provider>
    );
};

describe('Main Business Flow Integration Tests', () => {
    beforeEach(() => {
        // Reset mocks and store before each test
        authService.performLogout.mockClear();
        logService.createDailyLog.mockClear();
        logService.getDailyLogs.mockClear();
        planService.getAllPlans.mockClear();
        planService.createMealPlan.mockClear();
        aiService.analyzeFoodImage.mockClear();
        store = mockStore(initialState);
    });

    // --- DashboardPage Tests ---
    describe('DashboardPage', () => {
        test('renders DashboardPage with user info and navigation links for USER role', async () => {
            renderWithProviders(<DashboardPage />, { route: '/dashboard' });

            // Use waitFor to handle any async rendering issues
            await waitFor(() => {
                expect(screen.getByText(/Chào mừng, testuser!/i)).toBeInTheDocument();
            });

            // Fix 1: Use a function to find text split across elements
            expect(screen.getByText((content, element) => element.textContent.trim().startsWith('Role:') && element.textContent.includes('USER'))).toBeInTheDocument();

            expect(screen.getByRole('link', { name: /Dashboard/i })).toBeInTheDocument();
            expect(screen.getByRole('link', { name: /Tư vấn Dinh dưỡng/i })).toBeInTheDocument();
            expect(screen.getByRole('link', { name: /Theo dõi Nhật ký/i })).toBeInTheDocument();
            expect(screen.getByRole('link', { name: /Xem báo cáo Nhật ký/i })).toBeInTheDocument();
            expect(screen.getByRole('link', { name: /Lập Kế hoạch/i })).toBeInTheDocument();
            expect(screen.getByRole('link', { name: /Hồ sơ/i })).toBeInTheDocument();
            expect(screen.getByRole('link', { name: /Cộng đồng/i })).toBeInTheDocument();
            expect(screen.queryByRole('link', { name: /Quản lý Người dùng/i })).not.toBeInTheDocument();
        }, 10000); // Increase timeout to 10 seconds

        test('does not render Admin link for USER role', () => {
            renderWithProviders(<DashboardPage />, { route: '/dashboard' });
            expect(screen.queryByRole('link', { name: /Quản lý Người dùng/i })).not.toBeInTheDocument();
        });

        test('renders Admin link for ADMIN role and navigates to admin page', async () => {
            const adminInitialState = {
                auth: {
                    user: { id: 'admin123', username: 'adminuser', role: 'ADMIN', token: 'fake-admin-token' },
                },
            };
            renderWithProviders(<DashboardPage />, { route: '/dashboard', initialState: adminInitialState });

            // Check if the admin link is present
            const adminLink = screen.getByRole('link', { name: /Quản lý Người dùng/i });
            expect(adminLink).toBeInTheDocument();

            // Check navigation
            fireEvent.click(adminLink);
            await waitFor(() => {
                expect(screen.getByText('Admin Users Management Page Content')).toBeInTheDocument();
            });
        });

        test('calls performLogout when logout button is clicked', () => {
            renderWithProviders(<DashboardPage />, { route: '/dashboard' });

            fireEvent.click(screen.getByRole('button', { name: /Đăng Xuất/i }));

            expect(authService.performLogout).toHaveBeenCalledTimes(1);
        });

        test('navigates to "Lập Kế hoạch" page when its link is clicked', async () => {
            render(
                <Provider store={store}>
                    <BrowserRouter>
                        <Routes>
                            <Route path="/dashboard/*" element={<DashboardPage />} />
                            {/* Fix 2: Render NutritionPlanPage directly for the /plans route */}
                            <Route path="/plans" element={<NutritionPlanPage />} />
                        </Routes>
                    </BrowserRouter>
                </Provider>,
                { route: '/dashboard' }
            );

            fireEvent.click(screen.getByRole('link', { name: /Lập Kế hoạch/i }));
            await waitFor(() => {
                // Fix 2: Assert on a text that is actually present in NutritionPlanPage
                expect(screen.getByText('Quản lý Kế hoạch Dinh dưỡng')).toBeInTheDocument();
            });
        });
    });

    // --- DailyLogInputPage Tests ---
    describe('DailyLogInputPage', () => {
        test('renders DailyLogInputPage and allows adding a food log', async () => {
            renderWithProviders(<DailyLogInputPage />, { route: '/log' });

            await waitFor(() => {
                expect(screen.getByText(/Ghi nhật ký Bữa ăn Hàng ngày/i)).toBeInTheDocument();
            });

            // Simulate user input
            fireEvent.change(screen.getByLabelText(/Tên Món ăn\/Thực phẩm/i), { target: { value: 'Bún chả' } });
            fireEvent.change(screen.getByLabelText(/Calo \(kcal\)/i), { target: { value: '500' } });
            fireEvent.change(screen.getByLabelText(/Protein \(g\)/i), { target: { value: '30' } });
            fireEvent.change(screen.getByLabelText(/Carb \(g\)/i), { target: { value: '50' } });
            fireEvent.change(screen.getByLabelText(/Chất béo \(g\)/i), { target: { value: '20' } });
            fireEvent.change(screen.getByLabelText(/Loại Bữa ăn/i), { target: { value: 'LUNCH' } });

            fireEvent.click(screen.getByRole('button', { name: /Lưu Mục nhật ký/i }));

            await waitFor(() => {
                expect(logService.createDailyLog).toHaveBeenCalledTimes(1);
                expect(logService.createDailyLog).toHaveBeenCalledWith(expect.objectContaining({
                    foodName: 'Bún chả',
                    calories: 500,
                    protein: 30,
                    carb: 50,
                    fat: 20,
                    mealType: 'LUNCH',
                    userId: 'user123',
                }));
            });
        });

        test('handles manual submission failure', async () => {
            logService.createDailyLog.mockRejectedValueOnce(new Error('API Error'));
            renderWithProviders(<DailyLogInputPage />, { route: '/log' });

            fireEvent.change(screen.getByLabelText(/Tên Món ăn\/Thực phẩm/i), { target: { value: 'Bún chả' } });
            fireEvent.click(screen.getByRole('button', { name: /Lưu Mục nhật ký/i }));

            await waitFor(() => {
                expect(screen.getByText('Lỗi khi ghi nhật ký. Vui lòng kiểm tra dữ liệu.')).toBeInTheDocument();
            });
        });

        test('handles image upload for food recognition', async () => {
            renderWithProviders(<DailyLogInputPage />, { route: '/log' });

            await waitFor(() => {
                expect(screen.getByText(/Ghi nhật ký Bữa ăn Hàng ngày/i)).toBeInTheDocument();
            });

            const file = new File(['(⌐□_□)'], 'food.png', { type: 'image/png' });
            const input = screen.getByLabelText(/Tải lên Hình ảnh Bữa ăn/i);

            fireEvent.change(input, { target: { files: [file] } });

            fireEvent.click(screen.getByRole('button', { name: /Phân tích & Điền Form/i }));

            await waitFor(() => {
                expect(aiService.analyzeFoodImage).toHaveBeenCalledTimes(1);
                expect(aiService.analyzeFoodImage).toHaveBeenCalledWith(file, 'user123');
                expect(screen.getByDisplayValue('mocked food')).toBeInTheDocument();
                expect(screen.getByDisplayValue('100')).toBeInTheDocument();
            });
        });

        test('handles image analysis failure', async () => {
            aiService.analyzeFoodImage.mockRejectedValueOnce(new Error('AI Error'));
            renderWithProviders(<DailyLogInputPage />, { route: '/log' });

            const file = new File(['(⌐□_□)'], 'food.png', { type: 'image/png' });
            const input = screen.getByLabelText(/Tải lên Hình ảnh Bữa ăn/i);
            fireEvent.change(input, { target: { files: [file] } });

            fireEvent.click(screen.getByRole('button', { name: /Phân tích & Điền Form/i }));

            await waitFor(() => {
                expect(screen.getByText('Phân tích hình ảnh thất bại. Vui lòng nhập bằng tay.')).toBeInTheDocument();
            });
        });

        test('allows clearing the selected image', async () => {
            renderWithProviders(<DailyLogInputPage />, { route: '/log' });

            const file = new File(['(⌐□_□)'], 'food.png', { type: 'image/png' });
            const input = screen.getByLabelText(/Tải lên Hình ảnh Bữa ăn/i);

            fireEvent.change(input, { target: { files: [file] } });

            // Check that the file name is displayed
            await waitFor(() => {
                expect(screen.getByText(/food.png đã sẵn sàng để phân tích./i)).toBeInTheDocument();
            });

            // Click the 'X' icon to clear the file
            fireEvent.click(screen.getByTestId('x-icon')); 

            await waitFor(() => {
                expect(screen.queryByText(/food.png đã sẵn sàng để phân tích./i)).not.toBeInTheDocument();
            });
        });

        test('disables submit button when food name is empty', () => {
            renderWithProviders(<DailyLogInputPage />, { route: '/log' });
            const submitButton = screen.getByRole('button', { name: /Lưu Mục nhật ký/i });
            expect(submitButton).toBeDisabled();
        });
    });

    // --- NutritionPlanPage Tests ---
    describe('NutritionPlanPage', () => {
        test('shows loading state initially', () => {
            planService.getAllPlans.mockReturnValueOnce(new Promise(() => {})); // Never resolves
            renderWithProviders(<NutritionPlanPage />, { route: '/plans' });
            expect(screen.getByText('Đang tải kế hoạch...')).toBeInTheDocument();
        });

        test('shows error message on fetch failure', async () => {
            // Mock lỗi Axios với cấu trúc phù hợp
            planService.getAllPlans.mockRejectedValueOnce({
                response: {
                    data: { message: 'API Error' },
                    status: 500
                },
                message: 'Request failed with status code 500'
            });
            renderWithProviders(<NutritionPlanPage />, { route: '/plans' });
            await waitFor(() => {
                expect(screen.getByText('Không thể tải danh sách kế hoạch. Vui lòng thử lại.')).toBeInTheDocument();
            });
        });

        test('renders NutritionPlanPage and displays meal plans with different goals', async () => {
            planService.getAllPlans.mockResolvedValueOnce([
                { id: '1', name: 'Kế hoạch giảm cân', goal: 'WEIGHT_LOSS', durationDays: 30, isActive: true },
                { id: '2', name: 'Kế hoạch tăng cơ', goal: 'MUSCLE_GAIN', durationDays: 60, isActive: false },
                { id: '3', name: 'Kế hoạch duy trì', goal: 'MAINTENANCE', durationDays: 90, isActive: false },
            ]);

            renderWithProviders(<NutritionPlanPage />, { route: '/plans' });

            await waitFor(() => {
                expect(screen.getByText('Kế hoạch giảm cân')).toBeInTheDocument();
                expect(screen.getByText(/Mục tiêu: Giảm Cân/i)).toBeInTheDocument();

                expect(screen.getByText('Kế hoạch tăng cơ')).toBeInTheDocument();
                expect(screen.getByText(/Mục tiêu: Tăng Cơ/i)).toBeInTheDocument();

                expect(screen.getByText('Kế hoạch duy trì')).toBeInTheDocument();
                expect(screen.getByText(/Mục tiêu: Duy Trì Sức Khỏe/i)).toBeInTheDocument();
            });
        });

        test('allows creation of a new meal plan', async () => {
            planService.getAllPlans.mockResolvedValueOnce([]);

            renderWithProviders(<NutritionPlanPage />, { route: '/plans' });

            await waitFor(() => {
                expect(screen.getByText(/Quản lý Kế hoạch Dinh dưỡng/i)).toBeInTheDocument();
            });

            const createPlanButton = screen.getByRole('link', { name: /Tạo Kế hoạch Mới/i });
            expect(createPlanButton).toBeInTheDocument();
            fireEvent.click(createPlanButton);

            await waitFor(() => {
                expect(screen.getByText('Create New Plan Page Content')).toBeInTheDocument(); // Kiểm tra nội dung của route mới
            });
        });
    });
});
