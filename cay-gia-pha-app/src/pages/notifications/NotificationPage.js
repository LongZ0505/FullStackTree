import React, { useState, useEffect } from 'react';
// ⭐ Đảm bảo hàm này đã được import và định nghĩa đúng
 
import './NotificationPage.css'; 
import { getNotifications } from '../../api/apiConfig';

// --- Dữ liệu DEMO đã được loại bỏ ---

const NotificationPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchNotifications = async () => {
        setLoading(true);
        setError(null);

        try {
            // ⭐ 1. Gọi API
            const apiResponse = await getNotifications(); 

            // ⭐ 2. Lấy danh sách từ trường 'result' của ApiResponse
            const notificationList = apiResponse.result || [];
            
            // ⭐ 3. Ánh xạ (Map) dữ liệu: Đặt message = content và thêm id/createdAt nếu API có
            // Giả định: Mỗi NotificationResponse có ít nhất các trường id, content (message), createdAt
            const formattedNotifications = notificationList.map(item => ({
                id: item.id || item.someUniqueId, // Sử dụng ID duy nhất từ API
                message: item.content || 'Nội dung thông báo trống', // LẤY TRƯỜNG CONTENT
                createdAt: item.createAt || new Date().toISOString() // Sử dụng thời gian từ API
            }));

            setNotifications(formattedNotifications);
            
        } catch (err) {
            console.error("Lỗi khi tải thông báo:", err);
            setError("Không thể tải thông báo. Vui lòng kiểm tra kết nối API.");
            setNotifications([]);
        } finally {
            setLoading(false);
        }
    };
    
    fetchNotifications();
  }, []); 

  return (
    <div className="notification-container">
      <h2>🔔 Thông báo</h2>
      {loading ? (
        <p>Đang tải thông báo...</p>
      ) : error ? ( 
        <p className="error-message">❌ {error}</p>
      ) : (
        <ul className="notification-list">
          {notifications.length === 0 ? (
            <p>Bạn không có thông báo nào.</p>
          ) : (
            notifications.map(notif => (
              <li key={notif.id} className="notification-item">
                <p className="notif-message">
                    {/* Thêm icon dựa trên nội dung message (là content từ API) */}
                    {notif.message.includes('thành viên') ? '👨‍👩‍👧‍👦 ' : 
                     notif.message.includes('bài viết') ? '📝 ' : 
                     notif.message.includes('tin nhắn') ? '✉️ ' : 'ℹ️ '}
                    {notif.message}
                </p>
                <span className="notif-date">
                  {notif.createdAt}
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