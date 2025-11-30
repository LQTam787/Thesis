// src/pages/__tests__/ProfileManagementPage.test.jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi, describe, it, expect, beforeEach, afterEach, beforeAll, afterAll } from 'vitest';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import ProfileManagementPage from '../ProfileManagementPage';
import { INITIAL_PROFILE } from '../ProfileManagementPage';

// --- 1. MOCK DATA & SETUP ---

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

const mockReduxUser = {
    fullName: 'Redux User',
    email: 'redux@example.com',
    username: 'redux_user'
};

// Sửa đường dẫn API để khớp với log lỗi
const defaultHandlers = [
    http.get('*/api/profile', () => {
        return HttpResponse.json(mockProfileData);
    }),
    http.put('*/api/profile/characteristics', async ({ request }) => {
        const body = await request.json();
        return HttpResponse.json(body);
    }),
    http.put('*/api/profile/goals', async ({ request }) => {
        const body = await request.json();
        return HttpResponse.json(body);
    })
];

const server = setupServer(...defaultHandlers);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

const createTestStore = (preloadedState = {}) => {
    return configureStore({
        reducer: {
            auth: (state = { user: null }, action) => state,
        },
        preloadedState,
    });
};

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

// --- 2. TEST CASES ---

describe('ProfileManagementPage Integration Tests', () => {
    beforeEach(() => {
        // Đảm bảo các handler mặc định được áp dụng lại nếu một test case nào đó override chúng
        server.use(...defaultHandlers);
    });

    it('hiển thị trạng thái loading ban đầu', () => {
        renderWithProviders(<ProfileManagementPage />);
        expect(screen.getByText(/Đang tải hồ sơ.../i)).toBeInTheDocument();
    });

    it('hiển thị dữ liệu profile sau khi tải thành công (Ưu tiên User từ Redux)', async () => {
        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => {
            expect(screen.queryByText(/Đang tải hồ sơ.../i)).not.toBeInTheDocument();
        });
        expect(screen.getByText('Quản lý Hồ sơ Cá nhân')).toBeInTheDocument();
        expect(screen.getByText(mockReduxUser.fullName)).toBeInTheDocument();
        expect(screen.getByText(mockReduxUser.email)).toBeInTheDocument();
        expect(screen.getByDisplayValue('170')).toBeInTheDocument();
        expect(screen.getByDisplayValue('65')).toBeInTheDocument();
        expect(screen.getByDisplayValue('2000')).toBeInTheDocument();
    });

    it('hiển thị thông báo lỗi khi API getProfile thất bại', async () => {
        server.use(
            http.get('*/api/profile', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );
        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => {
            expect(screen.getByText(/Không thể tải hồ sơ/i)).toBeInTheDocument();
        });
        expect(screen.getByText(mockReduxUser.fullName)).toBeInTheDocument();
    });

    it('cập nhật Đặc điểm Cá nhân thành công', async () => {
        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => expect(screen.getByDisplayValue('65')).toBeInTheDocument());

        // Sửa lỗi `expected '6570' to be '70'` bằng cách dùng fireEvent.change
        const weightInput = screen.getByLabelText(/Cân nặng/i);
        fireEvent.change(weightInput, { target: { value: '70' } });
        expect(weightInput.value).toBe('70');

        const allergyInput = screen.getByLabelText(/Dị ứng/i);
        fireEvent.change(allergyInput, { target: { value: 'Tôm' } });

        const characteristicsForm = screen.getByRole('form', { name: /Đặc điểm Sinh học & Sức khỏe/i });
        fireEvent.submit(characteristicsForm);

        await waitFor(() => {
            expect(screen.getByText(/Đặc điểm đã được cập nhật thành công/i)).toBeInTheDocument();
        });
    });

    it('hiển thị lỗi khi cập nhật Đặc điểm thất bại', async () => {
        server.use(
            http.put('*/api/profile/characteristics', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );
        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => expect(screen.getByDisplayValue('65')).toBeInTheDocument());

        const characteristicsForm = screen.getByRole('form', { name: /Đặc điểm Sinh học & Sức khỏe/i });
        fireEvent.submit(characteristicsForm);

        await waitFor(() => {
            expect(screen.getByText(/Lỗi cập nhật characteristics/i)).toBeInTheDocument();
        });
    });

    it('cập nhật Mục tiêu Dinh dưỡng thành công', async () => {
        renderWithProviders(<ProfileManagementPage />);
        await waitFor(() => expect(screen.getByDisplayValue('2000')).toBeInTheDocument());

        const caloInput = screen.getByLabelText(/Calo Mục tiêu/i);
        fireEvent.change(caloInput, { target: { value: '2500' } });

        const goalSelect = screen.getByRole('combobox', { name: /Mục tiêu/i });
        fireEvent.change(goalSelect, { target: { value: 'WEIGHT_LOSS' } });

        const goalsForm = screen.getByRole('form', { name: /Mục tiêu Dinh dưỡng/i });
        fireEvent.submit(goalsForm);

        await waitFor(() => {
            expect(screen.getByText(/Mục tiêu đã được cập nhật thành công/i)).toBeInTheDocument();
        });
    });

    it('hiển thị lỗi khi cập nhật Mục tiêu thất bại', async () => {
        server.use(
            http.put('*/api/profile/goals', () => {
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
        renderWithProviders(<ProfileManagementPage />, {
            preloadedState: { auth: { user: null } }
        });
        await waitFor(() => {
            expect(screen.getByText(mockProfileData.user.fullName)).toBeInTheDocument();
        });
    });

    it('hiển thị dữ liệu mặc định khi Redux user và API getProfile đều thất bại', async () => {
        server.use(
            http.get('*/api/profile', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );
        renderWithProviders(<ProfileManagementPage />, {
            preloadedState: { auth: { user: null } }
        });
        await waitFor(() => {
            expect(screen.getByText(/Không thể tải hồ sơ/i)).toBeInTheDocument();
            expect(screen.getByText(INITIAL_PROFILE.user.fullName)).toBeInTheDocument();
            expect(screen.getByText(INITIAL_PROFILE.user.email)).toBeInTheDocument();
            expect(screen.getByDisplayValue(INITIAL_PROFILE.characteristics.height.toString())).toBeInTheDocument();
            expect(screen.getByDisplayValue(INITIAL_PROFILE.goals.targetDailyCalories.toString())).toBeInTheDocument();
        });
    });
});
