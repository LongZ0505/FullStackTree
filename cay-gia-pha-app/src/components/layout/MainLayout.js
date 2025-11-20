import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import './MainLayout.css'; // Tạo file CSS này

const MainLayout = () => {
  const { logout } = useAuth();

  return (
    <div className="main-layout">
      <nav className="navbar">
        <div className="navbar-links">
          {/* Dựa trên thanh điều hướng ở source 15, 16, 17 */}
          <NavLink to="/" end>Cây Gia Phả</NavLink>
          <NavLink to="/members">Thành viên</NavLink>
          <NavLink to="/messages">Nhắn tin</NavLink>
          <NavLink to="/notifications">Thông báo</NavLink>
          <NavLink to="/posts">Bài viết</NavLink>
        </div>
        <div className="navbar-user">
          {/* TODO: Thêm tên người dùng */}
          <button onClick={logout} className="btn-logout">Đăng xuất</button>
        </div>
      </nav>
      <main className="content">
        <Outlet /> {/* Đây là nơi các trang con (Page) sẽ được hiển thị */}
      </main>
    </div>
  );
};

export default MainLayout;