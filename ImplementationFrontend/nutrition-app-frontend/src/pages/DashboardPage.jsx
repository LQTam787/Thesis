// src/pages/DashboardPage.jsx
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import authService from '../services/authService';

function DashboardPage() {
    // Lấy thông tin người dùng từ Redux Store
    const user = useSelector((state) => state.auth.user);

    // Hàm Đăng xuất
    const handleLogout = () => {
        authService.performLogout();
    };

    return (
        <div className="min-h-screen bg-gray-50 p-8">
            <header className="flex justify-between items-center mb-10">
                <h1 className="text-4xl font-extrabold text-green-700">
                    Chào mừng, {user ? user.username : 'Người dùng'}!
                </h1>
                <button
                    onClick={handleLogout}
                    className="bg-red-500 hover:bg-red-600 text-white font-semibold py-2 px-4 rounded-lg shadow-md transition duration-300"
                >
                    Đăng Xuất
                </button>
            </header>

            <section className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">
                <h2 className="text-2xl font-semibold text-gray-800 mb-4">Tổng quan Hệ thống</h2>
                <p className="text-gray-600">
                    Đây là trang Dashboard, nơi bạn có thể truy cập các chức năng chính: Tư vấn Dinh dưỡng, Theo dõi Nhật ký, và Lập Kế hoạch.
                </p>
                {/* Các liên kết/thẻ chức năng sẽ được thêm vào trong các module sau */}
                <div className="mt-4 p-3 bg-green-50 rounded-lg border border-green-200 text-sm text-green-800">
                    **Trạng thái:** Đã đăng nhập thành công. Role: **{user ? user.role || 'USER' : 'Không rõ'}**
                </div>
            </section>
        </div>
    );
}

export default DashboardPage;