// src/components/PrivateRoute.jsx
import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';

function PrivateRoute() {
    // Lấy trạng thái xác thực từ Redux Store
    const { isAuthenticated, isLoading } = useSelector((state) => state.auth);

    // Hiển thị một màn hình tải trong khi kiểm tra trạng thái (ví dụ: kiểm tra token trong Local Storage)
    if (isLoading) {
        return <div className="text-center p-8">Đang tải...</div>;
    }

    // Nếu đã đăng nhập, cho phép truy cập Component con (Outlet)
    return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
}

export default PrivateRoute;