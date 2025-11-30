// src/pages/LoginPage.jsx
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../services/authService';
import { setCredentials } from '../store/authSlice';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            // Gọi hàm login từ Service
            const user = await authService.login(username, password);

            // Chuyển hướng đến trang Dashboard sau khi đăng nhập thành công
            navigate('/dashboard');

        } catch (err) {
            // Xử lý lỗi hiển thị trên UI
            setError('Đăng nhập thất bại. Vui lòng kiểm tra tên người dùng và mật khẩu.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-xl shadow-lg">
                <h2 className="text-3xl font-bold text-center text-green-700">Đăng Nhập</h2>
                <form onSubmit={handleLogin} className="space-y-6">

                    {/* Thông báo lỗi */}
                    {error && (
                        <div className="p-3 text-sm text-red-700 bg-red-100 rounded-lg">
                            {error}
                        </div>
                    )}

                    {/* Trường Tên người dùng */}
                    <div>
                        <label htmlFor="username" className="block text-sm font-medium text-gray-700"> {/* Added htmlFor */}
                            Tên người dùng
                        </label>
                        <input
                            type="text"
                            id="username" // Added id
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        />
                    </div>

                    {/* Trường Mật khẩu */}
                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700"> {/* Added htmlFor */}
                            Mật khẩu
                        </label>
                        <input
                            type="password"
                            id="password" // Added id
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                        />
                    </div>

                    {/* Nút Đăng nhập */}
                    <button
                        type="submit"
                        disabled={loading}
                        className={`w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white ${
                            loading ? 'bg-green-400 cursor-not-allowed' : 'bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500'
                        }`}
                    >
                        {loading ? 'Đang đăng nhập...' : 'Đăng Nhập'}
                    </button>
                </form>

                {/* Liên kết Đăng ký */}
                <p className="text-sm text-center text-gray-600">
                    Chưa có tài khoản?{' '}
                    <Link to="/register" className="font-medium text-green-600 hover:text-green-500">
                        Đăng ký ngay
                    </Link>
                </p>
            </div>
        </div>
    );
}

export default LoginPage;