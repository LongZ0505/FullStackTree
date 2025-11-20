import apiClient from './apiConfig';

// ... (Các hàm getGenealogyTree, getMembersList, addMember đã có) ...

// Thêm thành viên mới (dựa trên source 14)
export const addMember = async (memberData) => {
  // memberData: { name, dob, dod, sex }
  try {
    const response = await apiClient.post('/genealogy/member', memberData);
    return response.data; // Giả sử backend trả về node mới (ví dụ: { id: 'new_id', ...memberData })
  } catch (error) {
    console.error("Lỗi thêm thành viên:", error.response?.data);
    throw error;
  }
};
export const getMembersList = async (memberData) => {
  // memberData: { name, dob, dod, sex }
  try {
    const response = await apiClient.post('/genealogy/getMember', memberData);
    return response.data; // Giả sử backend trả về node mới (ví dụ: { id: 'new_id', ...memberData })
  } catch (error) {
    console.error("Lỗi thêm thành viên:", error.response?.data);
    throw error;
  }
};

// CẬP NHẬT: Sửa thông tin thành viên
export const updateMember = async (id, memberData) => {
  try {
    const response = await apiClient.put(`/genealogy/member/${id}`, memberData);
    return response.data; // Giả sử backend trả về node đã cập nhật
  } catch (error) {
    console.error("Lỗi cập nhật thành viên:", error.response?.data);
    throw error;
  }
};

// CẬP NHẬT: Xóa thành viên
export const deleteMember = async (id) => {
  try {
    await apiClient.delete(`/genealogy/member/${id}`);
    return { success: true }; // Trả về thành công
  } catch (error) {
    console.error("Lỗi xóa thành viên:", error.response?.data);
    throw error;
  }
};
/**
 * Thêm mối quan hệ
 * @param {string} sourceId - ID của node 1
 * @param {string} targetId - ID của node 2
 * @param {string} relationType - Loại quan hệ (ví dụ: 'SOUSE', 'FATHER_SON' từ CSDL)
 */
export const addRelationship = async (sourceId, targetId, relationType) => {
  try {
    const response = await apiClient.post('/genealogy/relationship', {
      fromNodeId: sourceId,
      toNodeId: targetId,
      type: relationType,
    });
    return response.data; // Giả sử backend trả về edge mới
  } catch (error) {
    console.error("Lỗi tạo quan hệ:", error.response?.data);
    throw error;
  }
};

/**
 * Xóa mối quan hệ
 * @param {string} edgeId - ID của cạnh (relationship)
 */
export const deleteRelationship = async (edgeId) => {
  try {
    await apiClient.delete(`/genealogy/relationship/${edgeId}`);
    return { success: true };
  } catch (error) {
    console.error("Lỗi xóa quan hệ:", error.response?.data);
    throw error;
  }
};