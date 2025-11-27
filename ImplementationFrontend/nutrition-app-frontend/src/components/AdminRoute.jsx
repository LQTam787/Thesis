// src/components/AdminRoute.jsx
import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate, Outlet, useLocation } from 'react-router-dom';

const AdminRoute = () => {
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    // Tách việc truy cập và xác thực mảng vai trò.
    const rawRoles = useSelector((state) => state.auth.user?.roles);

    // SỬA LỖI: Đảm bảo userRoles LUÔN là một mảng, ngay cả khi rawRoles là null, undefined, hoặc một giá trị không phải mảng.
    const userRoles = Array.isArray(rawRoles) ? rawRoles : [];

    const location = useLocation();

    // Bây giờ, userRoles chắc chắn là một mảng, không còn lỗi TypeError: userRoles.includes is not a function
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