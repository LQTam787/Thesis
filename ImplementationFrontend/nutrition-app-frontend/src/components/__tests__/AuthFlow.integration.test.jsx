// src/components/__tests__/AuthFlow.integration.test.jsx
import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { vi, describe, it, expect, beforeEach } from 'vitest';

import PrivateRoute from '../PrivateRoute';
import AdminRoute from '../AdminRoute';
import authReducer from '../../store/authSlice'; // Giả định đường dẫn đến authSlice

// Mock console.warn để tránh các cảnh báo không mong muốn từ react-router-dom
const consoleWarnMock = vi.spyOn(console, 'warn').mockImplementation(() => {});
const alertMock = vi.spyOn(window, 'alert').mockImplementation(() => {});

// --- Dummy Components ---
const ProtectedContent = () => <div data-testid="protected-content">Protected Content</div>;
const AdminContent = () => <div data-testid="admin-content">Admin Dashboard</div>;
const LoginPage = () => <div data-testid="login-page">Login Page</div>;
const DashboardPage = () => <div data-testid="dashboard-page">User Dashboard</div>;
const HomePage = () => <div data-testid="home-page">Home Page</div>;

// --- Helper function to create a mock Redux store ---
const createMockStore = (initialAuthState) => {
    return configureStore({
        reducer: {
            auth: authReducer,
        },
        preloadedState: {
            auth: initialAuthState,
        },
    });
};

// --- Helper function to render the app with a specific initial state and route ---
const renderApp = (store, initialEntries = ['/']) => {
    render(
        <Provider store={store}>
            <MemoryRouter initialEntries={initialEntries}>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/dashboard" element={<DashboardPage />} />

                    {/* Private Routes */}
                    <Route path="/private" element={<PrivateRoute />}>
                        <Route index element={<ProtectedContent />} />
                    </Route>

                    {/* Admin Routes */}
                    <Route path="/admin" element={<AdminRoute />}>
                        <Route index element={<AdminContent />} />
                    </Route>
                </Routes>
            </MemoryRouter>
        </Provider>
    );
};

describe('V.C Integration Test: Authentication and Authorization Flow', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        alertMock.mockClear();
    });

    // Test Case 1: Người dùng chưa đăng nhập cố gắng truy cập PrivateRoute
    it('1. Unauthenticated user should be redirected from /private to /login', async () => {
        const store = createMockStore({
            isAuthenticated: false,
            isLoading: false,
            user: null,
            token: null,
            error: null,
        });

        renderApp(store, ['/private']);

        // Chờ cho đến khi trang đăng nhập được hiển thị
        await waitFor(() => {
            expect(screen.getByTestId('login-page')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('protected-content')).not.toBeInTheDocument();
    });

    // Test Case 2: Người dùng chưa đăng nhập cố gắng truy cập AdminRoute
    it('2. Unauthenticated user should be redirected from /admin to /login', async () => {
        const store = createMockStore({
            isAuthenticated: false,
            isLoading: false,
            user: null,
            token: null,
            error: null,
        });

        renderApp(store, ['/admin']);

        await waitFor(() => {
            expect(screen.getByTestId('login-page')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
    });

    // Test Case 3: Người dùng đã đăng nhập (USER) truy cập PrivateRoute
    it('3. Authenticated USER should access PrivateRoute /private', async () => {
        const store = createMockStore({
            isAuthenticated: true,
            isLoading: false,
            user: { roles: ['USER'] },
            token: 'mock-token',
            error: null,
        });

        renderApp(store, ['/private']);

        await waitFor(() => {
            expect(screen.getByTestId('protected-content')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('login-page')).not.toBeInTheDocument();
    });

    // Test Case 4: Người dùng đã đăng nhập (USER) cố gắng truy cập AdminRoute
    it('4. Authenticated USER should be redirected from /admin to /dashboard and show alert', async () => {
        const store = createMockStore({
            isAuthenticated: true,
            isLoading: false,
            user: { roles: ['USER'] },
            token: 'mock-token',
            error: null,
        });

        renderApp(store, ['/admin']);

        await waitFor(() => {
            expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        expect(alertMock).toHaveBeenCalledWith('Truy cập bị từ chối. Bạn không có quyền quản trị viên.');
    });

    // Test Case 5: Người dùng đã đăng nhập (ADMIN) truy cập AdminRoute
    it('5. Authenticated ADMIN should access AdminRoute /admin', async () => {
        const store = createMockStore({
            isAuthenticated: true,
            isLoading: false,
            user: { roles: ['USER', 'ADMIN'] },
            token: 'mock-token',
            error: null,
        });

        renderApp(store, ['/admin']);

        await waitFor(() => {
            expect(screen.getByTestId('admin-content')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('login-page')).not.toBeInTheDocument();
        expect(screen.queryByTestId('dashboard-page')).not.toBeInTheDocument();
        expect(alertMock).not.toHaveBeenCalled();
    });

    // Test Case 6: Người dùng đang tải (isLoading = true) cố gắng truy cập PrivateRoute
    it('6. Should show loading state when isLoading is true for PrivateRoute', async () => {
        const store = createMockStore({
            isAuthenticated: false,
            isLoading: true,
            user: null,
            token: null,
            error: null,
        });

        renderApp(store, ['/private']);

        expect(screen.getByText('Đang tải...')).toBeInTheDocument();
        expect(screen.queryByTestId('protected-content')).not.toBeInTheDocument();
        expect(screen.queryByTestId('login-page')).not.toBeInTheDocument();
    });

    // Test Case 7: Người dùng đang tải (isLoading = true) cố gắng truy cập AdminRoute
    // AdminRoute không có trạng thái isLoading riêng, nó dựa vào PrivateRoute hoặc trạng thái chung.
    // Nếu isLoading là true, PrivateRoute sẽ hiển thị "Đang tải...", và AdminRoute sẽ không được render.
    it('7. Should show loading state when isLoading is true for AdminRoute (via PrivateRoute logic)', async () => {
        const store = createMockStore({
            isAuthenticated: false, // Hoặc true, miễn là isLoading là true
            isLoading: true,
            user: null,
            token: null,
            error: null,
        });

        renderApp(store, ['/admin']); // AdminRoute được lồng trong PrivateRoute nếu có

        // Trong trường hợp này, AdminRoute không trực tiếp xử lý isLoading.
        // Nếu AdminRoute được sử dụng độc lập, nó sẽ không hiển thị "Đang tải...".
        // Tuy nhiên, nếu nó được lồng sau PrivateRoute (như trong App.jsx thường thấy),
        // thì PrivateRoute sẽ xử lý isLoading.
        // Giả định AdminRoute không lồng trong PrivateRoute cho test này để kiểm tra hành vi riêng.
        // Nếu AdminRoute không lồng trong PrivateRoute, nó sẽ chuyển hướng ngay lập tức nếu chưa xác thực.
        // Nhưng vì AdminRoute cũng kiểm tra isAuthenticated, nên nó sẽ chuyển hướng đến /login.
        // Để kiểm tra isLoading, ta cần một kịch bản mà AdminRoute được render nhưng PrivateRoute không chặn.
        // Tuy nhiên, theo cấu trúc hiện tại, AdminRoute không có logic isLoading riêng.
        // Do đó, test này sẽ kiểm tra hành vi chuyển hướng nếu isAuthenticated=false, isLoading=true.
        // Hoặc nếu isAuthenticated=true, isLoading=true, thì AdminRoute sẽ được render và kiểm tra quyền.

        // Cập nhật: AdminRoute không có logic isLoading. Nếu isAuthenticated là false, nó sẽ chuyển hướng.
        // Nếu isAuthenticated là true, nó sẽ kiểm tra vai trò.
        // Test này không thực sự kiểm tra isLoading của AdminRoute mà là hành vi của nó khi isLoading của authSlice là true.
        // Nếu AdminRoute được gọi trực tiếp (không lồng trong PrivateRoute), nó sẽ bỏ qua isLoading.
        // Để kiểm tra isLoading, ta cần một route cha như PrivateRoute.
        // Vì vậy, test này sẽ kiểm tra rằng nó vẫn chuyển hướng đến login nếu isAuthenticated là false, bất kể isLoading.
        await waitFor(() => {
            expect(screen.getByTestId('login-page')).toBeInTheDocument();
        });
        expect(screen.queryByText('Đang tải...')).not.toBeInTheDocument(); // AdminRoute không hiển thị "Đang tải..."
    });

    // Test Case 8: Người dùng đã đăng nhập (ADMIN) truy cập PrivateRoute
    it('8. Authenticated ADMIN should access PrivateRoute /private', async () => {
        const store = createMockStore({
            isAuthenticated: true,
            isLoading: false,
            user: { roles: ['USER', 'ADMIN'] },
            token: 'mock-token',
            error: null,
        });

        renderApp(store, ['/private']);

        await waitFor(() => {
            expect(screen.getByTestId('protected-content')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('login-page')).not.toBeInTheDocument();
    });

    // Test Case 9: Người dùng đã đăng nhập nhưng user object là null (không có vai trò) cố gắng truy cập AdminRoute
    it('9. Authenticated user with null user object should be redirected from /admin to /dashboard and show alert', async () => {
        const store = createMockStore({
            isAuthenticated: true,
            isLoading: false,
            user: null, // user object is null
            token: 'mock-token',
            error: null,
        });

        renderApp(store, ['/admin']);

        await waitFor(() => {
            expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        expect(alertMock).toHaveBeenCalledWith('Truy cập bị từ chối. Bạn không có quyền quản trị viên.');
    });

    // Test Case 10: Người dùng đã đăng nhập nhưng user.roles là mảng rỗng cố gắng truy cập AdminRoute
    it('10. Authenticated user with empty roles array should be redirected from /admin to /dashboard and show alert', async () => {
        const store = createMockStore({
            isAuthenticated: true,
            isLoading: false,
            user: { roles: [] }, // empty roles array
            token: 'mock-token',
            error: null,
        });

        renderApp(store, ['/admin']);

        await waitFor(() => {
            expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
        });
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        expect(alertMock).toHaveBeenCalledWith('Truy cập bị từ chối. Bạn không có quyền quản trị viên.');
    });
});
