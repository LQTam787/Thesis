import React from 'react';
import { Link } from 'react-router-dom';

const AdminDashboardPage: React.FC = () => {
  return (
    <div className="container mx-auto p-4">
      <header className="bg-green-600 text-white p-4 rounded-t-lg text-center mb-4">
        <h1 className="text-3xl font-bold">Bảng điều khiển quản trị viên</h1>
      </header>
      <nav className="flex justify-center bg-gray-800 p-2 rounded-md mb-4">
        <Link to="/admin/dashboard" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Trang chủ</Link>
        <Link to="/admin/users" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Quản lý người dùng</Link>
        <Link to="/admin/shared-content" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Quản lý nội dung chia sẻ</Link>
        <Link to="#" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Cài đặt</Link>
        <Link to="/login" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Đăng xuất</Link>
      </nav>
      <main>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mt-4">
          <div className="bg-green-50 border border-green-200 rounded-lg p-4 text-center shadow-sm hover:shadow-md transition-shadow duration-300">
            <h2 className="text-xl font-semibold text-green-800 mb-2">Quản lý người dùng</h2>
            <p className="text-gray-700 mb-4">Xem, thêm, sửa, xóa thông tin người dùng.</p>
            <Link to="/admin/users" className="inline-block px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors duration-300">Đi tới</Link>
          </div>
          <div className="bg-green-50 border border-green-200 rounded-lg p-4 text-center shadow-sm hover:shadow-md transition-shadow duration-300">
            <h2 className="text-xl font-semibold text-green-800 mb-2">Quản lý nội dung chia sẻ</h2>
            <p className="text-gray-700 mb-4">Duyệt, chỉnh sửa, xóa các bài viết được chia sẻ.</p>
            <Link to="/admin/shared-content" className="inline-block px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors duration-300">Đi tới</Link>
          </div>
          <div className="bg-green-50 border border-green-200 rounded-lg p-4 text-center shadow-sm hover:shadow-md transition-shadow duration-300">
            <h2 className="text-xl font-semibold text-green-800 mb-2">Thống kê hệ thống</h2>
            <p className="text-gray-700 mb-4">Xem các báo cáo và thống kê tổng quan.</p>
            <Link to="#" className="inline-block px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors duration-300">Xem báo cáo</Link>
          </div>
          <div className="bg-green-50 border border-green-200 rounded-lg p-4 text-center shadow-sm hover:shadow-md transition-shadow duration-300">
            <h2 className="text-xl font-semibold text-green-800 mb-2">Cài đặt hệ thống</h2>
            <p className="text-gray-700 mb-4">Cấu hình các thiết lập của ứng dụng.</p>
            <Link to="#" className="inline-block px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors duration-300">Cài đặt</Link>
          </div>
        </div>
      </main>
      <footer className="text-center mt-8 p-4 text-gray-600 border-t border-gray-200">
        <p>&copy; 2025 Hệ thống Quản lý Dinh dưỡng. Tất cả quyền được bảo lưu.</p>
      </footer>
    </div>
  );
};

export default AdminDashboardPage;
