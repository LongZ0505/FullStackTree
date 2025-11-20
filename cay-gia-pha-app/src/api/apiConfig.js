import axios from 'axios';

const API_BASE_URL = 'http://localhost:8888/api/v1';
const token = localStorage.getItem('token');
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: token ? `Bearer ${token}` : "",
  },
});
//--------------------------Auth 
export const login = async (formData) => {
  try {
    const response = await apiClient.post('/identity/auth/login', 
      formData
    );localStorage.setItem("token",response.data.result.token);
      localStorage.setItem('user', response.data.result);
    return response.data;
  } catch (error) {
    console.error("Lỗi đăng nhập:", error.response?.data);
    throw error;
  }
};
export const register = async (formData) => {
  try {
    const response = await apiClient.post('/identity/users/register', formData);
    return response.data;
  } catch (error) {
    console.error("Lỗi đăng ký:", error.response?.data);
    throw error;
  }
};

export const forgotPassword = async (email) => {
  try {
    const response = await apiClient.post('/auth/forgot-password', { email });
    return response.data;
  } catch (error) {
    console.error("Lỗi quên mật khẩu:", error.response?.data);
    throw error;
  }
};
//--------------------------Member 
export const getMembersList = async () => {
  try {
    const response = await apiClient.get('/follow/userNode/allUserNode');
    return response.data; 
  } catch (error) {
    console.error("Lỗi thêm thành viên:", error.response?.data);
    throw error;
  }
};
export const addMember = async (memberData) => {
  // memberData: { name, dob, dod, sex }
  try {
    const response = await apiClient.post('/follow/userNode/createNode', memberData);
    return response.data; // Giả sử backend trả về node mới (ví dụ: { id: 'new_id', ...memberData })
  } catch (error) {
    console.error("Lỗi thêm thành viên:", error.response?.data);
    throw error;
  }
}
export const updateMember = async (id, memberData) => {
  try {
    const response = await apiClient.put(`/follow/userNode/userNode/${id}`, memberData);
    return response.data; // Giả sử backend trả về node đã cập nhật
  } catch (error) {
    console.error("Lỗi cập nhật thành viên:", error.response?.data);
    throw error;
  }
};
export const addRelationship = async (relationData) => {
  try {
    const response = await apiClient.post('/follow/userNode/relation', relationData);
    return response.data; // Giả sử backend trả về edge mới
  } catch (error) {
    console.error("Lỗi tạo quan hệ:", error.response?.data);
    throw error;
  }
};
export const deleteRelationship = async (relationId) => {
  try {
    await apiClient.delete(`/follow/userNode/ralations/${relationId}`);
    return { success: true };
  } catch (error) {
    console.error("Lỗi xóa quan hệ:", error.response?.data);
    throw error;
  }
};
export const deleteMember = async (id) => {
  try {
    await apiClient.delete(`/follow/userNode/userNode/${id}`);
    return { success: true }; // Trả về thành công
  } catch (error) {
    console.error("Lỗi xóa thành viên:", error.response?.data);
    throw error;
  }
};
export const queryUser = async (query) => {
  try {
    const response = await apiClient.get(`follow/userNode/query/${query}`);
    return response.data;
  } catch (error) {
    console.error("Lỗi Tìm kiếm :", error.response?.data);
    throw error;
  }
};
//------------------MemberList
export const getNodeList = async () => {
  try {
    const response = await apiClient.get('/follow/userNode/allNode');
    return response.data; 
  } catch (error) {
    console.error("Lỗi thêm thành viên:", error.response?.data);
    throw error;
  }
};


// -------------------------Conversation
export const fetchConversationsByUserId = async (userId) =>{
  try {
    const response = await apiClient.get(`/chat/conversations/userId/${userId}`);
    return response.data; 
  } catch (error) {
    console.error("Lỗi lấy đoạn chat:", error.response?.data);
    throw error;
  }
}
export const createConversation = async (formData) => {
  try {
    console.log(formData)
    const response = await apiClient.post('/chat/conversations/conversation',  formData);
    return response.data;
  } catch (error) {
    console.error("Lỗi quên tạo hội thoại:", error.response?.data);
    throw error;
  }
};
export const getMessages = async (conversationId) => {
  try {
    const response = await apiClient.get(`/chat/chats/allChats/${conversationId}`);
    return response.data;
  } catch (error) {
    console.error("Lỗi lấy tin nhắn chat:", error.response?.data);
    throw error;
  }
};
export const sendMessage = async (formData) => {
  try {
    const response = await apiClient.post('/chat/chats/message',formData);
    return response.data;
  } catch (error) {
    console.error("Lỗi gửi tin nhắn:", error.response?.data);
    throw error;
  }
};
export const getConversationsByConversationId = async (conversationId) => {
  try {
    const response = await apiClient.get(`/chat/conversations/conversationId/${conversationId}`);
    return response.data;
  } catch (error) {
    console.error("Lỗi lấy đoạn hội thoại:", error.response?.data);
    throw error;
  }
};
//-------------------------Post

// Lấy tất cả bài viết (dựa trên source 17)
export const getPosts = async () => {
  try {
    const response = await apiClient.get('/post/posts/allPosts');
    console.log(response)
    return response.data; // Mảng các bài viết  
  } catch (error) {
    console.error("Lỗi tải bài viết:", error.response?.data);
    throw error;
  }
};

//Tạo bài viết mới
export const createPost = async (postData) => {
  // postData: { title, content }
  try {
    const response = await apiClient.post('/post/posts/newPost', postData);
    return response.data;
  } catch (error) {
    console.error("Lỗi tạo bài viết:", error.response?.data);
    throw error;
  }
};

// Xóa bài viết
export const deletePost = async (postId) => {
  try {
    const response = await apiClient.delete(`/post/posts/postIdentifier/${postId}`);
    return response.data;
  } catch (error) {
    console.error("Lỗi xóa bài viết:", error.response?.data);
    throw error;
  }
};
export const updatePost = async (formData) => {
  try {
    const response = await apiClient.put(`/post/posts`,formData);
    return response.data;
  } catch (error) {
    console.error("Lỗi sửa bài viết:", error.response?.data);
    throw error;
  }
};
//------Notification
export const getNotifications = async () => {
  try {
    const response = await apiClient.get(`/notification/allNotifications`);
    return response.data;
  } catch (error) {
    console.error("Lỗi lấy thông báo:", error.response?.data);
    throw error;
  }
};
export default apiClient;