import React, { useState, useEffect } from 'react';
import './MemberFormModal.css';

// Dữ liệu ban đầu rỗng cho form
const EMPTY_FORM = {
  name: '',
  sex: 'true',
  dob: '',
  dod: ''
};

const MemberFormModal = ({ isOpen, onClose, onSubmit, onDelete, initialData }) => {
  const [formData, setFormData] = useState(EMPTY_FORM);
  
  // Khi `initialData` thay đổi (khi click Sửa) hoặc khi mở modal (cho form Thêm)
  useEffect(() => {
    if (isOpen) {
      setFormData(initialData || EMPTY_FORM);
    } else {
      setFormData(EMPTY_FORM);
    }
  }, [initialData, isOpen]);

  if (!isOpen) {
    return null;
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
   const dataToSubmit = { ...formData };
    dataToSubmit.sex = dataToSubmit.sex === 'true';
    
    onSubmit(dataToSubmit);
  };

  const handleDelete = () => {
    // Chỉ gọi onDelete nếu đây là form Sửa (có initialData.id)
    if (initialData?.id && window.confirm('Bạn có chắc muốn xóa thành viên này?')) {
      onDelete(initialData.id);
    }
  };

  const isEditMode = !!initialData?.id;
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="modal-close" onClick={onClose}>&times;</button>
        <h3>{isEditMode ? 'Sửa thông tin thành viên' : 'Thêm thành viên mới'}</h3>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Họ và tên:</label>
            <input type="text" name="name" value={formData.name} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label>Giới tính:</label>
            <select name="sex" value={formData.sex} onChange={handleChange}>
              <option value="true">Nam</option>
              <option value="false">Nữ</option>
            </select>
          </div>
          <div className="form-group">
            <label>Ngày sinh:</label>
            <input type="date" name="dob" value={formData.dob} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Ngày mất:</label>
            <input type="date" name="dod" value={formData.dod || ""}  onChange={handleChange} />
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-submit">
              {isEditMode ? 'Cập nhật' : 'Thêm mới'}
            </button>
            {isEditMode && (
              <button type="button" className="btn btn-delete" onClick={handleDelete}>
                Xóa
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
};

export default MemberFormModal;