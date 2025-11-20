import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { forgotPassword } from '../../api/authApi';
import './AuthPages.css';
import Lottie from 'lottie-react';
import dragonAnim from '../../assets/dragon.json';

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    try {
      await forgotPassword(email);
      setMessage('✅ Đã gửi email khôi phục mật khẩu. Vui lòng kiểm tra hộp thư của bạn.');
    } catch (err) {
      setError('❌ Email không tồn tại trong hệ thống.');
    }
  };

  return (
    <div className="auth-container">
      {/* 🐉 Hiệu ứng rồng nền */}
      <Lottie animationData={dragonAnim} loop={true} className="auth-dragon" />

      <form onSubmit={handleSubmit} className="auth-form">
        <h2>Khôi phục mật khẩu</h2>

        <div className="form-group">
          <label htmlFor="email">Nhập email đã đăng ký *</label>
          <input
            type="email"
            id="email"
            placeholder="you@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        {error && <p className="error-message">{error}</p>}
        {message && <p style={{ color: 'green', textAlign: 'center' }}>{message}</p>}

        <button type="submit" className="btn-submit">
          Gửi yêu cầu
        </button>

        <div className="auth-links">
          <Link to="/login">← Quay lại đăng nhập</Link>
        </div>
      </form>
    </div>
  );
};

export default ForgotPasswordPage;
