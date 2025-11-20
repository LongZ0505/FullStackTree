import apiClient from './apiConfig';

// === API cho trang Tin nhắn (ChatPage) ===

// Lấy danh sách các cuộc hội thoại (danh sách bên trái, source 13)
export const getConversations = async () => {
  try {
    const response = await apiClient.get('/conversations');
    return response.data; // Mảng các cuộc hội thoại
  } catch (error) {
    console.error("Lỗi tải hội thoại:", error.response?.data);
    throw error;
  }
};

// Lấy tin nhắn của một cuộc hội thoại cụ thể
export const getMessages = async (conversationId) => {
  try {
    const response = await apiClient.get(`/messages/${conversationId}`);
    return response.data; // Mảng tin nhắn
  } catch (error) {
    console.error("Lỗi tải tin nhắn:", error.response?.data);
    throw error;
  }
};

// Gửi tin nhắn (sẽ được gọi qua API hoặc WebSocket)
export const sendMessage = async (conversationId, messageContent) => {
  try {
    const response = await apiClient.post('/messages', {
      conversationId: conversationId,
      message: messageContent,
    });
    return response.data;
  } catch (error) {
    console.error("Lỗi gửi tin nhắn:", error.response?.data);
    throw error;
  }
};

// === API cho trang Thông báo (NotificationPage) ===

// Lấy danh sách thông báo (dựa trên source 16)
export const getNotifications = async () => {
  try {
    const response = await apiClient.get('/notifications');
    return response.data; // Mảng thông báo
  } catch (error) {
    console.error("Lỗi tải thông báo:", error.response?.data);
    throw error;
  }
};