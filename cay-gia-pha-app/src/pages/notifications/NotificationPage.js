import React, { useState, useEffect } from 'react';
// import { getNotifications } from '../../api/messageApi'; // Tạm thời tắt API
import './NotificationPage.css'; // Tạo file CSS này

// --- THÊM DỮ LIỆU DEMO ---
const DEMO_NOTIFICATIONS = [
  { 
    id: 'n1', 
    message: 'Nguyễn Văn Cường đã thêm một thành viên mới vào cây gia phả.', 
    createdAt: '2025-10-23T14:30:00Z' // Sử dụng định dạng ISO string
  },
  { 
    id: 'n2', 
    message: 'Trần Thị Bình đã đăng một bài viết mới: "Thông báo họp họ".', 
    createdAt: '2025-10-22T09:15:00Z' 
  },
  { 
    id: 'n3', 
    message: 'Bạn có tin nhắn mới từ Nguyễn Văn An.', 
    createdAt: '2025-10-22T08:05:00Z' 
  },
  { 
    id: 'n4', 
    message: 'Chào mừng bạn đến với hệ thống Cây Gia Phả!', 
    createdAt: '2025-10-21T12:00:00Z' 
  },
];
// -------------------------

const NotificationPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    
    // Tạm thời tắt API thật
    // getNotifications()
    //   .then(setNotifications)
    //   .catch(console.error)
    //   .finally(() => setLoading(false));

    // --- Giả lập API ---
    setTimeout(() => {
      setNotifications(DEMO_NOTIFICATIONS);
      setLoading(false);
    }, 500); // Giả lập 0.5 giây tải
    
  }, []);

  return (
    <div className="notification-container">
      <h2>🔔 Thông báo</h2>
      {loading ? (
        <p>Đang tải thông báo...</p>
      ) : (
        <ul className="notification-list">
          {/* --- THÊM LOGIC KIỂM TRA MẢNG RỖNG --- */}
          {notifications.length === 0 ? (
            <p>Bạn không có thông báo nào.</p>
          ) : (
            notifications.map(notif => (
              <li key={notif.id}>
                <p>{notif.message}</p>
                <span className="notif-date">
                  {new Date(notif.createdAt).toLocaleString('vi-VN')}
                </span>
              </li>
            ))
          )}
        </ul>
      )}
    </div>
  );
};

export default NotificationPage;