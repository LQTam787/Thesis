import React, { useEffect, useState } from 'react';
import { User } from '../types/User';
import { userService } from '../services/userService';

const UserManagementPage: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await userService.getAllUsers();
        setUsers(data);
      } catch (err) {
        setError('Không thể tải dữ liệu người dùng.');
      } finally {
        setLoading(false);
      }
    };
    fetchUsers();
  }, []);

  if (loading) {
    return <div className="container mx-auto p-4">Đang tải...</div>;
  }

  if (error) {
    return <div className="container mx-auto p-4 text-red-500">Lỗi: {error}</div>;
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6 text-center">Quản lý người dùng</h1>
      <div className="bg-white shadow-md rounded-lg p-6">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-semibold">Danh sách người dùng</h2>
          <button className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors duration-300">
            Thêm người dùng mới
          </button>
        </div>
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">ID</th>
              <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Tên thật</th>
              <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Email</th>
              <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Tên tài khoản</th>
              <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="hover:bg-gray-50">
                <td className="py-3 px-4 border-b border-gray-200 text-sm">{user.id}</td>
                <td className="py-3 px-4 border-b border-gray-200 text-sm">{user.tenThat}</td>
                <td className="py-3 px-4 border-b border-gray-200 text-sm">{user.email}</td>
                <td className="py-3 px-4 border-b border-gray-200 text-sm">{user.tenTaiKhoan}</td>
                <td className="py-3 px-4 border-b border-gray-200 text-sm">
                  <button className="text-blue-600 hover:text-blue-800 mr-3">Sửa</button>
                  <button className="text-red-600 hover:text-red-800">Xóa</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default UserManagementPage;
