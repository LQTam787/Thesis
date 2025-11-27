// src/pages/RegisterPage.jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../services/authService';

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState(''); // Thêm trường email
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(false);
        setLoading(true);

        try {
            const userData = { username, password, email };
            await authService.register(userData);

            setSuccess(true);
            // Chuyển hướng đến trang Đăng nhập sau 2 giây
            setTimeout(() => {
                navigate('/login');
            }, 2000);

        } catch (err) {
            // Giả định lỗi từ Backend có thể là response.data.message
            const errorMessage = err.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.';
            setError(errorMessage);
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-xl shadow-lg">
                <h2 className="text-3xl font-bold text-center text-green-700">Đăng Ký</h2>
                <form onSubmit={handleRegister} className="space-y-4">

                    {/* Thông báo lỗi/thành công */}
                    {error && (
                        <div className="p-3 text-sm text-red-700 bg-red-100 rounded-lg">{error}</div>
                    )}
                    {success && (
                        <div className="p-3 text-sm text-green-700 bg-green-100 rounded-lg">
                            Đăng ký thành công! Đang chuyển hướng đến trang Đăng nhập...
                        </div>
                    )}

                    {/* Trường Tên người dùng */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Tên người dùng</label>
                        <input
                            type="text"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        />
                    </div>

                    {/* Trường Email */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email</label>
                        <input
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        />
                    </div>

                    {/* Trường Mật khẩu */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Mật khẩu</label>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        />
                    </div>

                    {/* Nút Đăng ký */}
                    <button
                        type="submit"
                        disabled={loading || success}
                        className={`w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white ${
                            (loading || success) ? 'bg-green-400 cursor-not-allowed' : 'bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500'
                        }`}
                    >
                        {loading ? 'Đang xử lý...' : 'Đăng Ký'}
                    </button>
                </form>

                {/* Liên kết Đăng nhập */}
                <p className="text-sm text-center text-gray-600 mt-4">
                    Đã có tài khoản?{' '}
                    <Link to="/login" className="font-medium text-green-600 hover:text-green-500">
                        Đăng nhập
                    </Link>
                </p>
            </div>
        </div>
    );
}

export default RegisterPage;