// src/pages/__tests__/ProfileManagementPage.test.jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach, afterEach, beforeAll, afterAll } from 'vitest';
import { Provider } from 'react-redux'; // FIX: Changed from '@reduxjs/toolkit'
import { configureStore } from '@reduxjs/toolkit';
import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import ProfileManagementPage from '../ProfileManagementPage';
import { INITIAL_PROFILE } from '../ProfileManagementPage'; // ADDED: Import INITIAL_PROFILE

// --- 1. MOCK DATA & SETUP ---

// Dữ liệu mẫu trả về từ API
const mockProfileData = {
    user: { fullName: 'API User', email: 'api@example.com', username: 'api_user' },
    characteristics: {
        height: 170,
        weight: 65,
        activityLevel: 'MODERATE',
        allergies: 'None',
        underlyingDisease: 'None'
    },
    goals: {
        goalType: 'MAINTAIN',
        durationDays: 30,
        targetDailyCalories: 2000
    },
};

// Dữ liệu User trong Redux (giả lập đã đăng nhập)
const mockReduxUser = {
    fullName: 'Redux User',
    email: 'redux@example.com',
    username: 'redux_user'
};

// Mock Server (MSW)
const handlers = [
    // GET Profile
    http.get('*/profile', () => {
        return HttpResponse.json(mockProfileData);
    }),

    // UPDATE Characteristics
    http.put('*/characteristics', async ({ request }) => { // Giả định endpoint dựa trên tên service
        const body = await request.json();
        // Giả lập logic server: trả về đúng data đã gửi lên
        return HttpResponse.json(body);
    }),

    // UPDATE Goals
    http.put('*/goals', async ({ request }) => {
        const body = await request.json();
        return HttpResponse.json(body);
    })
];

const server = setupServer(...handlers);

// Quản lý vòng đời Server MSW
beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

// Mock Redux Store
const createTestStore = (preloadedState = {}) => {
    return configureStore({
        reducer: {
            auth: (state = { user: null }, action) => state, // Simple reducer
        },
        preloadedState,
    });
};

// Helper function để render component với Redux Provider
const renderWithProviders = (
    ui,
    {
        preloadedState = { auth: { user: mockReduxUser } },
        store = createTestStore(preloadedState),
        ...renderOptions
    } = {}
) => {
    function Wrapper({ children }) {
        return <Provider store={store}>{children}</Provider>;
    }
    return { store, ...render(ui, { wrapper: Wrapper, ...renderOptions }) };
};

// Mock profileService để tránh lỗi nếu nó không sử dụng axios instance mặc định hoặc path khác
// Tuy nhiên, với Integration test tốt nhất là mock network (MSW).
// Ở đây ta giả định profileService gọi đúng các endpoint mà MSW bắt được.
// Nếu service file chưa tồn tại hoặc đường dẫn khác, bạn cần điều chỉnh handlers ở trên.

// --- 2. TEST CASES ---

describe('ProfileManagementPage Integration Tests', () => {

    it('hiển thị trạng thái loading ban đầu', () => {
        renderWithProviders(<ProfileManagementPage />);
        expect(screen.getByText(/Đang tải hồ sơ.../i)).toBeInTheDocument();
    });

    it('hiển thị dữ liệu profile sau khi tải thành công (Ưu tiên User từ Redux)', async () => {
        renderWithProviders(<ProfileManagementPage />);

        // Chờ loading biến mất và nội dung xuất hiện
        await waitFor(() => {
            expect(screen.queryByText(/Đang tải hồ sơ.../i)).not.toBeInTheDocument();
        });

        // Kiểm tra Header
        expect(screen.getByText('Quản lý Hồ sơ Cá nhân')).toBeInTheDocument();

        // Kiểm tra thông tin User (Phải lấy từ Redux mockReduxUser chứ không phải API)
        expect(screen.getByText(mockReduxUser.fullName)).toBeInTheDocument();
        expect(screen.getByText(mockReduxUser.email)).toBeInTheDocument();

        // Kiểm tra Form Characteristics (Lấy từ API)
        expect(screen.getByDisplayValue('170')).toBeInTheDocument(); // Chiều cao
        expect(screen.getByDisplayValue('65')).toBeInTheDocument();  // Cân nặng

        // Kiểm tra Form Goals (Lấy từ API)
        expect(screen.getByDisplayValue('2000')).toBeInTheDocument(); // Calo
    });

    it('hiển thị thông báo lỗi khi API getProfile thất bại', async () => {
        // Override handler để trả về lỗi 500
        server.use(
            http.get('*/profile', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderWithProviders(<ProfileManagementPage />);

        await waitFor(() => {
            expect(screen.getByText(/Không thể tải hồ sơ/i)).toBeInTheDocument();
        });

        // Dù lỗi API, vẫn hiển thị UI với dữ liệu Redux hoặc mặc định
        expect(screen.getByText(mockReduxUser.fullName)).toBeInTheDocument();
    });

    it('cập nhật Đặc điểm Cá nhân thành công', async () => {
        renderWithProviders(<ProfileManagementPage />);

        // Chờ data load xong
        await waitFor(() => expect(screen.getByDisplayValue('65')).toBeInTheDocument());

        // Thay đổi cân nặng
        const weightInput = screen.getByLabelText(/Cân nặng/i);
        fireEvent.change(weightInput, { target: { value: '70' } });
        expect(weightInput.value).toBe('70'); // Kiểm tra local state update

        // Thay đổi dị ứng
        const allergyInput = screen.getByLabelText(/Dị ứng/i);
        fireEvent.change(allergyInput, { target: { value: 'Tôm' } });

        // Submit form
        const characteristicsForm = screen.getByRole('form', { name: /Đặc điểm Sinh học & Sức khỏe/i });
        fireEvent.submit(characteristicsForm);

        // Kiểm tra thông báo thành công
        await waitFor(() => {
            expect(screen.getByText(/Đặc điểm đã được cập nhật thành công/i)).toBeInTheDocument();
        });
    });

    it('hiển thị lỗi khi cập nhật Đặc điểm thất bại', async () => {
        // Override handler update trả về lỗi
        server.use(
            http.put('*/characteristics', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => expect(screen.getByDisplayValue('65')).toBeInTheDocument());

        // Submit form ngay
        const characteristicsForm = screen.getByRole('form', { name: /Đặc điểm Sinh học & Sức khỏe/i });
        fireEvent.submit(characteristicsForm);

        // Kiểm tra thông báo lỗi
        await waitFor(() => {
            expect(screen.getByText(/Lỗi cập nhật characteristics/i)).toBeInTheDocument();
        });
    });

    it('cập nhật Mục tiêu Dinh dưỡng thành công', async () => {
        renderWithProviders(<ProfileManagementPage />);

        // Chờ data load xong
        await waitFor(() => expect(screen.getByDisplayValue('2000')).toBeInTheDocument());

        // Thay đổi Calo mục tiêu
        const caloInput = screen.getByLabelText(/Calo Mục tiêu/i);
        fireEvent.change(caloInput, { target: { value: '2500' } });

        // Thay đổi Loại mục tiêu (Select box)
        const goalSelect = screen.getByRole('combobox', { name: /Mục tiêu/i }); // Tìm select theo label
        fireEvent.change(goalSelect, { target: { value: 'WEIGHT_LOSS' } });

        // Submit form
        const goalsForm = screen.getByRole('form', { name: /Mục tiêu Dinh dưỡng/i });
        fireEvent.submit(goalsForm);

        // Kiểm tra thông báo thành công
        await waitFor(() => {
            expect(screen.getByText(/Mục tiêu đã được cập nhật thành công/i)).toBeInTheDocument();
        });
    });

    it('hiển thị lỗi khi cập nhật Mục tiêu thất bại', async () => {
        server.use(
            http.put('*/goals', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => expect(screen.getByDisplayValue('2000')).toBeInTheDocument());

        const goalsForm = screen.getByRole('form', { name: /Mục tiêu Dinh dưỡng/i });
        fireEvent.submit(goalsForm);

        await waitFor(() => {
            expect(screen.getByText(/Lỗi cập nhật goals/i)).toBeInTheDocument();
        });
    });

    it('Fallback dữ liệu user khi Redux store không có user (trường hợp hiếm)', async () => {
        // Render với store rỗng (người dùng chưa đăng nhập hoặc mất session, nhưng trang vẫn load được nhờ data API)
        renderWithProviders(<ProfileManagementPage />, {
            preloadedState: { auth: { user: null } }
        });

        await waitFor(() => {
            // Lúc này sẽ hiển thị tên từ API mockProfileData
            expect(screen.getByText(mockProfileData.user.fullName)).toBeInTheDocument();
        });
    });

    it('hiển thị dữ liệu mặc định khi Redux user và API getProfile đều thất bại', async () => {
        // Override handler để trả về lỗi 500 cho getProfile
        server.use(
            http.get('*/profile', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        // Render với store rỗng (không có user Redux)
        renderWithProviders(<ProfileManagementPage />, {
            preloadedState: { auth: { user: null } }
        });

        await waitFor(() => {
            // Kiểm tra thông báo lỗi từ API
            expect(screen.getByText(/Không thể tải hồ sơ/i)).toBeInTheDocument();
            // Kiểm tra dữ liệu user hiển thị là từ INITIAL_PROFILE
            expect(screen.getByText(INITIAL_PROFILE.user.fullName)).toBeInTheDocument();
            expect(screen.getByText(INITIAL_PROFILE.user.email)).toBeInTheDocument();
            // Also check characteristics and goals fallback
            expect(screen.getByDisplayValue(INITIAL_PROFILE.characteristics.height.toString())).toBeInTheDocument();
            expect(screen.getByDisplayValue(INITIAL_PROFILE.goals.targetDailyCalories.toString())).toBeInTheDocument();
        });
    });
});