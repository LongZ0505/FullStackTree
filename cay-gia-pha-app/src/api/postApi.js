import apiClient from './apiConfig';

// Lấy tất cả bài viết (dựa trên source 17)
export const getPosts = async () => {
  try {
    const response = await apiClient.get('/posts');
    return response.data; // Mảng các bài viết
  } catch (error) {
    console.error("Lỗi tải bài viết:", error.response?.data);
    throw error;
  }
};

// Tạo bài viết mới
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
    const response = await apiClient.delete(`/posts/${postId}`);
    return response.data;
  } catch (error) {
    console.error("Lỗi xóa bài viết:", error.response?.data);
    throw error;
  }
};