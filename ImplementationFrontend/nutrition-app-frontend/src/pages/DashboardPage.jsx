import React from 'react';
import { useSelector } from 'react-redux';

function DashboardPage() {
    const user = useSelector((state) => state.auth.user);

    return (
        <>
            <header className="flex justify-between items-center mb-10 bg-white p-6 rounded-xl shadow-md">
                <h1 className="text-4xl font-extrabold text-green-700">
                    Chào mừng, {user ? user.username : 'Người dùng'}!
                </h1>
                <div className="text-lg text-gray-700">
                    <span className="font-semibold">Role:</span> {user ? user.role || 'USER' : 'Không rõ'}
                </div>
            </header>

            <section className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">
                <h2 className="text-2xl font-semibold text-gray-800 mb-4">Tổng quan Hệ thống</h2>
                <p className="text-gray-600 mb-4">
                    Đây là trang Dashboard, nơi bạn có thể truy cập các chức năng chính của hệ thống thông qua sidebar bên trái.
                </p>
                <div className="mt-4 p-3 bg-green-50 rounded-lg border border-green-200 text-sm text-green-800">
                    Trạng thái: Đã đăng nhập thành công.
                </div>
            </section>
        </>
    );
}

export default DashboardPage;