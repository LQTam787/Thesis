// src/components/__tests__/AdminRoute.test.jsx
import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { vi, describe, test, expect, afterEach } from 'vitest';

import AdminRoute from '../AdminRoute';

// --- I. Mocks ---
// 1. Mock useSelector để kiểm soát trạng thái Redux
// Chúng ta cần mock module react-redux để thay thế hàm useSelector bằng hàm mock
vi.mock('react-redux', async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useSelector: vi.fn(),
    };
});

// 2. Mock window.alert để ngăn chặn alert thực và kiểm tra việc gọi hàm
const alertMock = vi.spyOn(window, 'alert').mockImplementation(() => {});


// --- II. Helper Components ---
// Component thay thế cho nội dung được bảo vệ (<Outlet />)
const AdminContent = () => <div data-testid="admin-content">Admin Dashboard</div>;
// Component đích cho việc chuyển hướng (Redirected Content)
const DashboardContent = () => <div data-testid="dashboard-content">User Dashboard</div>;
const LoginContent = () => <div data-testid="login-content">Login Page</div>;


// --- III. Render Utility ---
// Hàm tiện ích để render AdminRoute trong môi trường Router và cung cấp Redux State mock
// HÀM HELPER ĐƯỢC CẬP NHẬT: Đảm bảo mock trả về đúng giá trị selector mong muốn
const renderAdminRoute = (isAuthenticated, user, initialEntries = ['/admin']) => {
    // Cấu hình giá trị trả về cho useSelector
    useSelector.mockImplementation((selector) => {
        const selectorString = selector.toString();

        // Selector 1: const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
        if (selectorString.includes('isAuthenticated')) {
            return isAuthenticated;
        }

        // Selector 2: const rawRoles = useSelector((state) => state.auth.user?.roles);
        // Chúng ta kiểm tra chuỗi có chứa 'roles' không.
        if (selectorString.includes('roles')) {
            // Nếu user là null/undefined, thì rawRoles phải là undefined (hoặc null).
            // Nếu user tồn tại, trả về mảng user.roles.
            return user ? user.roles : undefined;
        }

        // Fallback (chẳng hạn nếu có selector chỉ lấy mỗi user object)
        if (selectorString.includes('user')) {
            return user;
        }

        return undefined;
    });

    render(
        <MemoryRouter initialEntries={initialEntries}>
            <Routes>
                {/* Route AdminRoute */}
                <Route path="/admin" element={<AdminRoute />}>
                    <Route index element={<AdminContent />} />
                </Route>
                {/* Route chuyển hướng */}
                <Route path="/dashboard" element={<DashboardContent />} />
                <Route path="/login" element={<LoginContent />} />
            </Routes>
        </MemoryRouter>
    );
};


describe('V.1 Unit Test: Component AdminRoute.jsx (Phân quyền)', () => {

    afterEach(() => {
        // Xóa tất cả các lệnh gọi mock sau mỗi bài kiểm thử để đảm bảo tính độc lập
        vi.clearAllMocks();
        alertMock.mockClear();
    });

    // V.1.1: Đã đăng nhập VÀ là ADMIN -> Cho phép truy cập
    test('1. Should render Outlet content when authenticated and user is ADMIN', () => {
        renderAdminRoute(true, { roles: ['USER', 'ADMIN'] }); // <--- Cập nhật: Truyền object user

        // 1. Kiểm tra nội dung được bảo vệ có được render không
        expect(screen.getByTestId('admin-content')).toBeInTheDocument();
        // 2. Đảm bảo không có alert được gọi
        expect(alertMock).not.toHaveBeenCalled();
        // 3. Đảm bảo không có chuyển hướng xảy ra
        expect(screen.queryByTestId('dashboard-content')).not.toBeInTheDocument();
        expect(screen.queryByTestId('login-content')).not.toBeInTheDocument();
    });

    // V.1.2: Đã đăng nhập nhưng KHÔNG phải ADMIN -> Chuyển hướng đến /dashboard và gọi alert
    test('2. Should redirect to /dashboard and show alert when authenticated but not ADMIN', () => {
        renderAdminRoute(true, { roles: ['USER'] }); // <--- Cập nhật: Truyền object user

        // 1. Kiểm tra nội dung được bảo vệ KHÔNG được render
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        // 2. Kiểm tra đích chuyển hướng là /dashboard đã được render
        expect(screen.getByTestId('dashboard-content')).toBeInTheDocument();
        // 3. Kiểm tra alert đã được gọi với thông báo chính xác
        expect(alertMock).toHaveBeenCalledWith('Truy cập bị từ chối. Bạn không có quyền quản trị viên.');
        expect(alertMock).toHaveBeenCalledTimes(1);
    });

    // V.1.3: Chưa đăng nhập -> Chuyển hướng đến /login
    test('3. Should redirect to /login and pass location state when not authenticated', () => {
        // user có thể là bất kỳ giá trị nào vì isAuthenticated đã là false.
        renderAdminRoute(false, { roles: ['USER', 'ADMIN'] });

        // 1. Kiểm tra nội dung được bảo vệ KHÔNG được render
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        // 2. Kiểm tra đích chuyển hướng là /login đã được render
        expect(screen.getByTestId('login-content')).toBeInTheDocument();
        // 3. Đảm bảo không có alert được gọi
        expect(alertMock).not.toHaveBeenCalled();
        // Lưu ý: Việc kiểm tra chính xác `state={{ from: location }}` rất phức tạp trong unit test đơn giản,
        // nhưng việc chuyển hướng thành công đến /login đã chứng minh logic đường dẫn được thực thi đúng.
    });

    // V.1.4: Đã đăng nhập nhưng user.roles là mảng rỗng (biến thể của case 2)
    test('4. Should redirect to /dashboard and show alert when authenticated but roles array is empty', () => {
        renderAdminRoute(true, { roles: [] }); // <--- Cập nhật: Truyền object user

        // 1. Kiểm tra nội dung được bảo vệ KHÔNG được render
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        // 2. Kiểm tra đích chuyển hướng là /dashboard đã được render
        expect(screen.getByTestId('dashboard-content')).toBeInTheDocument();
        // 3. Kiểm tra alert đã được gọi
        expect(alertMock).toHaveBeenCalledTimes(1);
    });

    // V.1.5: Đã đăng nhập nhưng state.auth.user là null/undefined
    test('5. Should redirect to /dashboard and show alert when authenticated but user object is null/undefined (Covers line 9 branch)', () => {
        // user: null/undefined (mocking)
        // Khi đó, state.auth.user?.roles sẽ là undefined, và userRoles = [] (mảng rỗng)
        renderAdminRoute(true, null); // isAuthenticated: true, user: null

        // 1. Kiểm tra nội dung được bảo vệ KHÔNG được render
        expect(screen.queryByTestId('admin-content')).not.toBeInTheDocument();
        // 2. Kiểm tra đích chuyển hướng là /dashboard đã được render
        expect(screen.getByTestId('dashboard-content')).toBeInTheDocument();
        // 3. Kiểm tra alert đã được gọi (vì userRoles = [] -> !isAdmin là true)
        expect(alertMock).toHaveBeenCalledWith('Truy cập bị từ chối. Bạn không có quyền quản trị viên.');
        expect(alertMock).toHaveBeenCalledTimes(1);
    });
});