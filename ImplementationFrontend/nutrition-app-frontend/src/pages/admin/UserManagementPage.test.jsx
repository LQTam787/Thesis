// src/pages/admin/UserManagementPage.test.jsx
import React from 'react';
import { render, screen, waitFor, fireEvent, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import UserManagementPage from './UserManagementPage';
import adminService from '../../services/adminService'; // Import để mock

// Mock adminService
vi.mock('../../services/adminService', () => ({
    default: {
        getAllUsers: vi.fn(),
        toggleUserLockStatus: vi.fn(),
    },
}));

const mockUsers = [
    { id: 'u1', username: 'admin_test', fullName: 'Quản Trị Viên', email: 'admin@app.com', roles: ['ADMIN'], isLocked: false, createdAt: '2025-01-01T00:00:00Z' },
    { id: 'u2', username: 'user_active', fullName: 'Nguyễn Văn A', email: 'a@user.com', roles: ['USER'], isLocked: false, createdAt: '2025-03-15T10:00:00Z' },
    { id: 'u3', username: 'user_locked', fullName: 'Trần Thị B', email: 'b@user.com', roles: ['USER'], isLocked: true, createdAt: '2025-04-20T12:30:000Z' },
    { id: 'u4', username: 'another_user', fullName: 'Phạm Thị C', email: 'c@user.com', roles: ['USER'], isLocked: false, createdAt: '2025-05-01T00:00:00Z' },
];

describe('UserManagementPage', () => {
    beforeEach(() => {
        // Reset mocks trước mỗi test
        adminService.getAllUsers.mockReset();
        adminService.toggleUserLockStatus.mockReset();
        // Mock window.confirm
        vi.spyOn(window, 'confirm').mockReturnValue(true);
        // Mock window.alert
        vi.spyOn(window, 'alert').mockImplementation(() => {});
    });

    afterEach(() => {
        vi.restoreAllMocks();
    });

    // Test Case 1: Hiển thị trạng thái tải và danh sách người dùng thành công
    test('should display loading state and then users list on successful fetch', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);

        render(<UserManagementPage />);

        expect(screen.getByText('Đang tải danh sách người dùng...')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.queryByText('Đang tải danh sách người dùng...')).not.toBeInTheDocument();
            expect(screen.getByText('Quản lý Người dùng (Admin)')).toBeInTheDocument();
            expect(screen.getByText('admin_test')).toBeInTheDocument();
            expect(screen.getByText('user_active')).toBeInTheDocument();
            expect(screen.getByText('user_locked')).toBeInTheDocument();
        });
    });

    // Test Case 2: Hiển thị thông báo lỗi và dữ liệu giả định khi fetch thất bại
    test('should display error message and fallback data on failed fetch', async () => {
        adminService.getAllUsers.mockRejectedValue(new Error('Network error'));

        render(<UserManagementPage />);

        await waitFor(() => {
            expect(screen.getByText('Lỗi: Bạn không có quyền truy cập trang Quản trị viên này.')).toBeInTheDocument();
            // Kiểm tra dữ liệu giả định được hiển thị
            expect(screen.getByText('admin_test')).toBeInTheDocument();
            expect(screen.getByText('user_active')).toBeInTheDocument();
            expect(screen.getByText('user_locked')).toBeInTheDocument();
        });
    });

    // Test Case 3: Chức năng tìm kiếm theo username
    test('should filter users by username', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => expect(screen.getByText('admin_test')).toBeInTheDocument());

        const searchInput = screen.getByPlaceholderText('Tìm kiếm theo Username, Tên hoặc Email...');
        await userEvent.type(searchInput, 'admin');

        expect(screen.getByText('admin_test')).toBeInTheDocument();
        expect(screen.queryByText('user_active')).not.toBeInTheDocument();
        expect(screen.queryByText('user_locked')).not.toBeInTheDocument();

        await userEvent.clear(searchInput);
        await userEvent.type(searchInput, 'user_active');
        expect(screen.queryByText('admin_test')).not.toBeInTheDocument();
        expect(screen.getByText('user_active')).toBeInTheDocument();
        expect(screen.queryByText('user_locked')).not.toBeInTheDocument();
    });

    // Test Case 4: Chức năng tìm kiếm theo fullName (không phân biệt chữ hoa chữ thường)
    test('should filter users by full name (case-insensitive)', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => expect(screen.getByText('Quản Trị Viên')).toBeInTheDocument());

        const searchInput = screen.getByPlaceholderText('Tìm kiếm theo Username, Tên hoặc Email...');
        await userEvent.type(searchInput, 'nguyễn văn a'); // lowercase search

        expect(screen.queryByText('Quản Trị Viên')).not.toBeInTheDocument();
        expect(screen.getByText('Nguyễn Văn A')).toBeInTheDocument();
        expect(screen.queryByText('Trần Thị B')).not.toBeInTheDocument();
    });

    // Test Case 5: Chức năng tìm kiếm theo email
    test('should filter users by email', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => expect(screen.getByText('admin_test')).toBeInTheDocument());

        const searchInput = screen.getByPlaceholderText('Tìm kiếm theo Username, Tên hoặc Email...');
        await userEvent.type(searchInput, 'b@user.com');

        expect(screen.queryByText('admin_test')).not.toBeInTheDocument();
        expect(screen.queryByText('user_active')).not.toBeInTheDocument();
        expect(screen.getByText('user_locked')).toBeInTheDocument();
    });

    // Test Case 6: Khóa tài khoản thành công từ bảng
    test('should lock a user successfully from the table', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        adminService.toggleUserLockStatus.mockResolvedValue({ ...mockUsers[1], isLocked: true }); // user_active becomes locked

        render(<UserManagementPage />);
        await waitFor(() => expect(screen.getByText('user_active')).toBeInTheDocument());

        // Tìm nút "Khóa" cho user_active
        const userActiveRow = screen.getByText('user_active').closest('tr');
        const lockButton = userActiveRow.querySelector('button:nth-child(2)'); // Nút thứ 2 là "Khóa" hoặc "Mở Khóa"

        await userEvent.click(lockButton);

        expect(window.confirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn KHÓA tài khoản user_active không?');
        expect(adminService.toggleUserLockStatus).toHaveBeenCalledWith('u2', true);

        await waitFor(() => {
            expect(userActiveRow).toHaveClass('bg-red-50'); // Dòng đổi màu
            expect(userActiveRow).toHaveTextContent('Đã Khóa'); // Trạng thái hiển thị "Đã Khóa"
            expect(userActiveRow).toHaveTextContent('Mở Khóa'); // Nút đổi thành "Mở Khóa"
        });
    });

    // Test Case 7: Mở khóa tài khoản thành công từ bảng
    test('should unlock a user successfully from the table', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        adminService.toggleUserLockStatus.mockResolvedValue({ ...mockUsers[2], isLocked: false }); // user_locked becomes unlocked

        render(<UserManagementPage />);
        await waitFor(() => expect(screen.getByText('user_locked')).toBeInTheDocument());

        // Tìm nút "Mở Khóa" cho user_locked
        const userLockedRow = screen.getByText('user_locked').closest('tr');
        const unlockButton = userLockedRow.querySelector('button:nth-child(2)'); // Nút thứ 2 là "Khóa" hoặc "Mở Khóa"

        await userEvent.click(unlockButton);

        expect(window.confirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn MỞ KHÓA tài khoản user_locked không?');
        expect(adminService.toggleUserLockStatus).toHaveBeenCalledWith('u3', false);

        await waitFor(() => {
            expect(userLockedRow).not.toHaveClass('bg-red-50'); // Dòng không còn màu đỏ
            expect(userLockedRow).toHaveTextContent('Hoạt động'); // Trạng thái hiển thị "Hoạt động"
            expect(userLockedRow).toHaveTextContent('Khóa'); // Nút đổi thành "Khóa"
        });
    });

    // Test Case 8: Hủy bỏ hành động khóa/mở khóa
    test('should not toggle lock status if confirmation is cancelled', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        window.confirm.mockReturnValue(false); // Hủy xác nhận

        render(<UserManagementPage />);
        await waitFor(() => expect(screen.getByText('user_active')).toBeInTheDocument());

        const userActiveRow = screen.getByText('user_active').closest('tr');
        const lockButton = userActiveRow.querySelector('button:nth-child(2)');

        await userEvent.click(lockButton);

        expect(window.confirm).toHaveBeenCalled();
        expect(adminService.toggleUserLockStatus).not.toHaveBeenCalled(); // API không được gọi

        // Đảm bảo trạng thái UI không thay đổi
        expect(userActiveRow).toHaveTextContent('Hoạt động');
    });

    // Test Case 9: Xử lý lỗi khi khóa/mở khóa tài khoản
    test('should show an alert if toggle lock status fails', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        adminService.toggleUserLockStatus.mockRejectedValue(new Error('API error'));

        render(<UserManagementPage />);
        await waitFor(() => expect(screen.getByText('user_active')).toBeInTheDocument());

        const userActiveRow = screen.getByText('user_active').closest('tr');
        const lockButton = userActiveRow.querySelector('button:nth-child(2)');

        await userEvent.click(lockButton);

        expect(window.confirm).toHaveBeenCalled();
        expect(adminService.toggleUserLockStatus).toHaveBeenCalledWith('u2', true);

        await waitFor(() => {
            expect(window.alert).toHaveBeenCalledWith('Lỗi khi thay đổi trạng thái khóa: API error');
        });
        // Đảm bảo trạng thái UI không thay đổi nếu có lỗi
        expect(userActiveRow).toHaveTextContent('Hoạt động');
    });

    // Test Case 10: Mở modal chi tiết người dùng
    test('should open user detail modal when "Xem" button is clicked', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => expect(screen.getByText('admin_test')).toBeInTheDocument());

        const userAdminRow = screen.getByText('admin_test').closest('tr');
        const viewButton = userAdminRow.querySelector('button:first-child'); // Nút "Xem" là nút đầu tiên

        await userEvent.click(viewButton);

        const modal = screen.getByRole('dialog'); // Tìm modal theo role
        expect(modal).toBeInTheDocument();

        // Sử dụng within để giới hạn phạm vi tìm kiếm trong modal
        expect(within(modal).getByText('Chi tiết Người dùng')).toBeInTheDocument();
        expect(within(modal).getByText((content, node) => node.textContent === 'ID: u1')).toBeInTheDocument();
        expect(within(modal).getByText((content, node) => node.textContent === 'Tên tài khoản: admin_test')).toBeInTheDocument();
        expect(within(modal).getByText((content, node) => node.textContent === 'Email: admin@app.com')).toBeInTheDocument();
        expect(within(modal).getByText((content, node) => node.textContent === 'Tên đầy đủ: Quản Trị Viên')).toBeInTheDocument();
        expect(within(modal).getByText((content, node) => node.textContent === 'Vai trò: ADMIN')).toBeInTheDocument();
        expect(within(modal).getByText('Hoạt động')).toBeInTheDocument();
    });

    // Test Case 11: Đóng modal chi tiết người dùng
    test('should close user detail modal when close button is clicked', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => expect(screen.getByText('admin_test')).toBeInTheDocument());

        const userAdminRow = screen.getByText('admin_test').closest('tr');
        const viewButton = userAdminRow.querySelector('button:first-child');

        await userEvent.click(viewButton);

        expect(screen.getByRole('dialog')).toBeInTheDocument();

        const closeButton = screen.getByLabelText('Close'); // Tìm bằng aria-label
        await userEvent.click(closeButton);

        expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });

    // Test Case 12: Khóa tài khoản từ bên trong modal
    test('should lock a user from within the modal', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        adminService.toggleUserLockStatus.mockResolvedValue({ ...mockUsers[1], isLocked: true }); // user_active becomes locked

        render(<UserManagementPage />);
        await waitFor(() => expect(screen.getByText('user_active')).toBeInTheDocument());

        const userActiveRow = screen.getByText('user_active').closest('tr');
        const viewButton = userActiveRow.querySelector('button:first-child');
        await userEvent.click(viewButton);

        const modal = screen.getByRole('dialog');
        expect(within(modal).getByText((content, node) => node.textContent === 'Tên tài khoản: user_active')).toBeInTheDocument();

        const lockButtonInModal = within(modal).getByRole('button', { name: /Khóa Tài khoản/i });
        await userEvent.click(lockButtonInModal);

        expect(window.confirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn KHÓA tài khoản user_active không?');
        expect(adminService.toggleUserLockStatus).toHaveBeenCalledWith('u2', true);

        await waitFor(() => {
            expect(screen.queryByRole('dialog')).not.toBeInTheDocument(); // Modal đóng
            expect(userActiveRow).toHaveClass('bg-red-50');
            expect(userActiveRow).toHaveTextContent('Đã Khóa');
        });
    });

    // Test Case 13: Mở khóa tài khoản từ bên trong modal
    test('should unlock a user from within the modal', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        adminService.toggleUserLockStatus.mockResolvedValue({ ...mockUsers[2], isLocked: false }); // user_locked becomes unlocked

        render(<UserManagementPage />);
        await waitFor(() => expect(screen.getByText('user_locked')).toBeInTheDocument());

        const userLockedRow = screen.getByText('user_locked').closest('tr');
        const viewButton = userLockedRow.querySelector('button:first-child');
        await userEvent.click(viewButton);

        const modal = screen.getByRole('dialog');
        expect(within(modal).getByText((content, node) => node.textContent === 'Tên tài khoản: user_locked')).toBeInTheDocument();

        const unlockButtonInModal = within(modal).getByRole('button', { name: /Mở Khóa Tài khoản/i });
        await userEvent.click(unlockButtonInModal);

        expect(window.confirm).toHaveBeenCalledWith('Bạn có chắc chắn muốn MỞ KHÓA tài khoản user_locked không?');
        expect(adminService.toggleUserLockStatus).toHaveBeenCalledWith('u3', false);

        await waitFor(() => {
            expect(screen.queryByRole('dialog')).not.toBeInTheDocument(); // Modal đóng
            expect(userLockedRow).not.toHaveClass('bg-red-50');
            expect(userLockedRow).toHaveTextContent('Hoạt động');
        });
    });

    // Test Case 14: Kiểm tra hiển thị vai trò ADMIN/USER
    test('should display correct roles for users', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => {
            const adminUserRow = screen.getByText('admin_test').closest('tr');
            expect(adminUserRow).toHaveTextContent('ADMIN');

            const regularUserRow = screen.getByText('user_active').closest('tr');
            expect(regularUserRow).toHaveTextContent('USER');
        });
    });

    // Test Case 15: Kiểm tra hiển thị trạng thái khóa/hoạt động
    test('should display correct lock status for users', async () => {
        adminService.getAllUsers.mockResolvedValue(mockUsers);
        render(<UserManagementPage />);

        await waitFor(() => {
            const activeUserRow = screen.getByText('user_active').closest('tr');
            expect(activeUserRow).toHaveTextContent('Hoạt động');

            const lockedUserRow = screen.getByText('user_locked').closest('tr');
            expect(lockedUserRow).toHaveTextContent('Đã Khóa');
            expect(lockedUserRow).toHaveClass('bg-red-50');
        });
    });
});
