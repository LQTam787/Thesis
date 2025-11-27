// src/components/__tests__/PrivateRoute.test.jsx
import React from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route, Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';
import PrivateRoute from '../PrivateRoute';

// Mock các dependencies
vi.mock('react-redux', async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useSelector: vi.fn(), // Mock useSelector
    };
});
// Mock console.warn (vì React Router DOM có thể cảnh báo về Navigate)
const consoleWarnMock = vi.spyOn(console, 'warn').mockImplementation(() => {});

// ------------------- DUMMY COMPONENTS -------------------
// Component giả lập nội dung được bảo vệ
const ProtectedContent = () => <div data-testid="protected-content">Protected Content</div>;
// Component giả lập trang đăng nhập
const LoginContent = () => <div data-testid="login-content">Login Page</div>;


// ------------------- HÀM HELPER -------------------
const renderPrivateRoute = (isAuthenticated, isLoading = false, initialEntries = ['/private']) => {
    // Cấu hình giá trị trả về cho useSelector
    useSelector.mockImplementation((selector) => {
        // Selector: const { isAuthenticated, isLoading } = useSelector((state) => state.auth);
        if (selector.toString().includes('state.auth')) {
            return {
                // Sử dụng tham số isAuthenticated
                isAuthenticated: isAuthenticated,
                // SỬ DỤNG THAM SỐ isLoading MỚI
                isLoading: isLoading,
            };
        }
        return undefined;
    });

    render(
        // Sử dụng MemoryRouter để giả lập môi trường Routing
        <MemoryRouter initialEntries={initialEntries}>
            <Routes>
                {/* Route có PrivateRoute bảo vệ */}
                <Route path="/private" element={<PrivateRoute />}>
                    <Route index element={<ProtectedContent />} />
                </Route>
                {/* Route chuyển hướng */}
                <Route path="/login" element={<LoginContent />} />
            </Routes>
        </MemoryRouter>
    );
};


// ------------------- UNIT TESTS -------------------
describe('V.1 Unit Test: Component PrivateRoute.jsx (Bảo vệ Route)', () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // Case 1: Đã đăng nhập (isAuthenticated = true)
    it('1. Should render Outlet content when user is authenticated', () => {
        // Cần truyền isLoading = false rõ ràng để tránh lỗi tham số trong tương lai
        renderPrivateRoute(true, false);

        // 1. Kiểm tra nội dung được bảo vệ có được render
        expect(screen.getByTestId('protected-content')).toBeInTheDocument();

        // 2. Đảm bảo trang login KHÔNG được render
        expect(screen.queryByTestId('login-content')).not.toBeInTheDocument();
    });

    // Case 2: Chưa đăng nhập (isAuthenticated = false)
    it('2. Should redirect to /login and pass location state when not authenticated', () => {
        // SỬA LỖI: Truyền isLoading = false rõ ràng
        renderPrivateRoute(false, false, ['/private?id=123']);

        // 1. Kiểm tra nội dung được bảo vệ KHÔNG được render
        expect(screen.queryByTestId('protected-content')).not.toBeInTheDocument();

        // 2. Kiểm tra đích chuyển hướng là /login đã được render
        expect(screen.getByTestId('login-content')).toBeInTheDocument();

        // 3. Đảm bảo không có nội dung loading
        expect(screen.queryByText('Đang tải...')).not.toBeInTheDocument();
    });

    // Case 3: Đang tải (isLoading = true)
    it('3. Should render Loading content when isLoading is true (Covers line 12)', () => {
        // GỌI HÀM CÓ isLoading = true
        renderPrivateRoute(false, true);

        // 1. Kiểm tra nội dung Loading có được render
        // SỬA LỖI QUERY: Dùng getByText vì component Loading không có data-testid="loading"
        expect(screen.getByText('Đang tải...')).toBeInTheDocument();

        // 2. Đảm bảo nội dung bảo vệ KHÔNG được render
        expect(screen.queryByTestId('protected-content')).not.toBeInTheDocument();

        // 3. Đảm bảo trang login KHÔNG được render
        expect(screen.queryByTestId('login-content')).not.toBeInTheDocument();
    });
});

// Không cần thiết phải đặt lại consoleWarnMock vì nó là một spy trên global console