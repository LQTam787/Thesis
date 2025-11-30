// src/pages/admin/UserManagementPage.jsx
import React, { useState, useEffect } from 'react';
import adminService from '../../services/adminService';
import { Users, Lock, Unlock, UserCheck, UserX, Search, X } from 'lucide-react';

function UserManagementPage() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);

    // --- 1. Tải danh sách người dùng ---
    const fetchUsers = async () => {
        try {
            setLoading(true);
            const data = await adminService.getAllUsers();
            setUsers(data);
            setError(null);
        } catch (err) {
            // Giả định lỗi 403/401 nếu không phải Admin
            setError('Lỗi: Bạn không có quyền truy cập trang Quản trị viên này.');
            // Dữ liệu giả định nếu lỗi (chỉ để demo UI)
            setUsers([
                { id: 'u1', username: 'admin_test', fullName: 'Quản Trị Viên', email: 'admin@app.com', roles: ['ADMIN'], isLocked: false, createdAt: '2025-01-01T00:00:00Z' },
                { id: 'u2', username: 'user_active', fullName: 'Nguyễn Văn A', email: 'a@user.com', roles: ['USER'], isLocked: false, createdAt: '2025-03-15T10:00:00Z' },
                { id: 'u3', username: 'user_locked', fullName: 'Trần Thị B', email: 'b@user.com', roles: ['USER'], isLocked: true, createdAt: '2025-04-20T12:30:00Z' },
            ]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // --- 2. Xử lý khóa/mở khóa tài khoản ---
    const handleToggleLock = async (user) => {
        const newLockStatus = !user.isLocked;
        if (!window.confirm(`Bạn có chắc chắn muốn ${newLockStatus ? 'KHÓA' : 'MỞ KHÓA'} tài khoản ${user.username} không?`)) {
            return;
        }

        try {
            // Gọi API để cập nhật trạng thái
            const updatedUser = await adminService.toggleUserLockStatus(user.id, newLockStatus);

            // Cập nhật State cục bộ
            setUsers(prevUsers =>
                prevUsers.map(u => u.id === user.id ? { ...u, isLocked: updatedUser.isLocked } : u)
            );
        } catch (err) {
            alert(`Lỗi khi thay đổi trạng thái khóa: ${err.message}`);
        }
    };

    // --- 3. Lọc người dùng ---
    const filteredUsers = users.filter(user =>
        user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // --- 4. Modal Chi tiết Người dùng ---
    const UserDetailModal = ({ user, onClose }) => (
        <div
            className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4"
            role="dialog"
            aria-modal="true"
            aria-labelledby="userDetailModalTitle"
        >
            <div className="bg-white p-6 rounded-xl w-full max-w-md shadow-2xl">
                <div className="flex justify-between items-center border-b pb-3 mb-4">
                    <h3 id="userDetailModalTitle" className="text-2xl font-bold text-green-700">Chi tiết Người dùng</h3>
                    <button onClick={onClose} aria-label="Close" className="text-gray-400 hover:text-gray-600"><X className="w-6 h-6" /></button>
                </div>

                <p className="mb-2"><strong>ID:</strong><span> {user.id}</span></p>
                <p className="mb-2"><strong>Tên tài khoản:</strong><span> {user.username}</span></p>
                <p className="mb-2"><strong>Email:</strong><span> {user.email}</span></p>
                <p className="mb-2"><strong>Tên đầy đủ:</strong><span> {user.fullName}</span></p>
                <p className="mb-2"><strong>Vai trò:</strong><span> {user.roles.join(', ')}</span></p>
                <p className="mb-4"><strong>Trạng thái:</strong>
                    <span className={`font-semibold ml-2 ${user.isLocked ? 'text-red-600' : 'text-green-600'}`}>
            {user.isLocked ? 'Đã Khóa' : 'Hoạt động'}
          </span>
                </p>

                <button
                    onClick={() => { handleToggleLock(user); onClose(); }}
                    className={`w-full py-2 rounded-lg text-white font-semibold transition ${user.isLocked ? 'bg-green-500 hover:bg-green-600' : 'bg-red-500 hover:bg-red-600'}`}
                >
                    {user.isLocked ? <><Unlock className="w-5 h-5 inline mr-2" /> Mở Khóa Tài khoản</> : <><Lock className="w-5 h-5 inline mr-2" /> Khóa Tài khoản</>}
                </button>
            </div>
        </div>
    );


    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-8 flex items-center">
                <Users className="w-7 h-7 mr-3 text-green-600" />
                Quản lý Người dùng (Admin)
            </h1>

            {error && <div className="p-4 mb-6 text-red-700 bg-red-100 border border-red-200 rounded-lg">{error}</div>}

            <div className="bg-white p-6 rounded-xl shadow-lg border border-gray-200">

                {/* Thanh tìm kiếm */}
                <div className="mb-6 flex items-center border border-gray-300 rounded-lg overflow-hidden">
                    <Search className="w-5 h-5 ml-3 text-gray-400" />
                    <input
                        type="text"
                        placeholder="Tìm kiếm theo Username, Tên hoặc Email..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full p-3 focus:ring-green-500 focus:border-green-500 border-none"
                    />
                </div>

                {loading ? (
                    <div className="text-center p-8">Đang tải danh sách người dùng...</div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tên đầy đủ</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Vai trò</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Trạng thái</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Hành động</th>
                            </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                            {filteredUsers.map((user) => (
                                <tr key={user.id} className={user.isLocked ? 'bg-red-50' : ''}>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{user.username}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{user.fullName}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                                        {user.roles.map(role => (
                                            <span key={role} className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${role === 'ADMIN' ? 'bg-blue-100 text-blue-800' : 'bg-green-100 text-green-800'}`}>
                          {role}
                        </span>
                                        ))}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                                        {user.isLocked ? (
                                            <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                          <Lock className="w-3 h-3 mr-1" /> Đã Khóa
                        </span>
                                        ) : (
                                            <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                          <UserCheck className="w-3 h-3 mr-1" /> Hoạt động
                        </span>
                                        )}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button
                                            onClick={() => { setSelectedUser(user); setIsModalOpen(true); }}
                                            className="text-indigo-600 hover:text-indigo-900"
                                        >
                                            Xem
                                        </button>
                                        <button
                                            onClick={() => handleToggleLock(user)}
                                            className={`font-semibold ${user.isLocked ? 'text-green-500 hover:text-green-700' : 'text-red-500 hover:text-red-700'}`}
                                        >
                                            {user.isLocked ? 'Mở Khóa' : 'Khóa'}
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* Hiển thị Modal nếu đang mở */}
                {isModalOpen && selectedUser && (
                    <UserDetailModal user={selectedUser} onClose={() => setIsModalOpen(false)} />
                )}

            </div>
        </div>
    );
}

export default UserManagementPage;