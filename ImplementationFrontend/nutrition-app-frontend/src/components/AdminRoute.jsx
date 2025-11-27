// src/components/AdminRoute.jsx
import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate, Outlet, useLocation } from 'react-router-dom';

const AdminRoute = () => {
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    // Giả định vai trò được lưu trong state.auth.user.roles là một mảng string (ví dụ: ['USER', 'ADMIN'])
    const userRoles = useSelector((state) => state.auth.user?.roles || []);
    const location = useLocation();

    const isAdmin = userRoles.includes('ADMIN');

    if (!isAuthenticated) {
        // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    if (!isAdmin) {
        // Nếu đã đăng nhập nhưng không phải ADMIN, chuyển hướng đến trang dashboard hoặc trang lỗi 403
        // Cảnh báo người dùng về lỗi phân quyền
        alert('Truy cập bị từ chối. Bạn không có quyền quản trị viên.');
        return <Navigate to="/dashboard" replace />;
    }

    // Nếu đã đăng nhập và là ADMIN, cho phép truy cập
    return <Outlet />;
};

export default AdminRoute;