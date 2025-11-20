import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

// Import Layouts
import MainLayout from '../components/layout/MainLayout'; 

// Import Pages
import LoginPage from '../pages/auth/LoginPage';
import RegisterPage from '../pages/auth/RegisterPage';
import ForgotPasswordPage from '../pages/auth/ForgotPasswordPage';
import GenealogyPage from '../pages/home/GenealogyPage';
import MemberListPage from '../pages/members/MemberListPage';
import PostListPage from '../pages/posts/PostListPage';
import ChatPage from '../pages/messages/ChatPage';
import NotificationPage from '../pages/notifications/NotificationPage';

// --- SỬA LẠI COMPONENT NÀY ---
const PrivateRoute = ({ children }) => {
  // 1. Lấy thêm biến loading từ useAuth
  const { user, loading } = useAuth();

  // 2. QUAN TRỌNG: Nếu đang tải (đang check localStorage), hiện màn hình chờ
  // Nếu không có đoạn này, F5 sẽ bị đá về Login ngay lập tức
  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        Loading...
      </div>
    );
  }

  // 3. Check xong rồi mới quyết định: Có user -> cho vào, Không -> về login
  return user ? children : <Navigate to="/login" />;
};

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Các trang không cần layout (Public) */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />

        {/* Các trang cần đăng nhập (Protected) */}
        <Route 
          element={
            <PrivateRoute>
              <MainLayout />
            </PrivateRoute>
          }
        >
          {/* Lưu ý: MainLayout của bạn PHẢI có <Outlet /> bên trong để render các trang con này */}
          <Route path="/" element={<GenealogyPage />} />
          <Route path="/members" element={<MemberListPage />} />
          <Route path="/posts" element={<PostListPage />} />
          <Route path="/messages" element={<ChatPage />} />
          <Route path="/direct/*" element={<ChatPage />} />
          <Route path="/notifications" element={<NotificationPage />} />
        </Route>
        
        {/* Trang mặc định */}
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRouter;