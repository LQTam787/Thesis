
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../services/authService';

const LoginPage: React.FC = () => {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [message, setMessage] = useState<string>('');
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      await login(username, password);
      setMessage('Đăng nhập thành công!');
      navigate('/'); // Chuyển hướng về trang chủ tạm thời
    } catch (error: any) {
      setMessage(error.toString());
    }
  };

  return (
    <div className="login-container">
      <h2>Đăng nhập</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Tên tài khoản:</label>
          <input
            type="text"
            id="username"
            name="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Mật khẩu:</label>
          <input
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn-login">Đăng nhập</button>
      </form>
      {message && <p id="message" style={{ color: message.includes('thành công') ? 'green' : 'red' }}>{message}</p>}
      <p className="register-link">Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link></p>
    </div>
  );
};

export default LoginPage;
