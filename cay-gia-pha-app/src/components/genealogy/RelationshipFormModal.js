import React, { useState, useEffect } from 'react';
import './MemberFormModal.css'; // Dùng chung CSS

// Lấy từ CSDL Neo4j (nhãn hiển thị)
const RELATIONSHIP_TYPES = [
  'vợ-chồng', // SOUSE
  'cha-con',  // FATHER_SON
  'mẹ-con',   // MOTHER_SON
  'anh-em',   // BROTHER_SISTER
];

const RelationshipFormModal = ({ isOpen, onClose, onSubmit, nodes }) => {
  const [source, setSource] = useState('');
  const [target, setTarget] = useState('');
  const [type, setType] = useState(RELATIONSHIP_TYPES[0]);

  useEffect(() => {
    // Reset form khi đóng
    if (!isOpen) {
      setSource('');
      setTarget('');
      setType(RELATIONSHIP_TYPES[0]);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!source || !target || !type) {
      alert('Vui lòng chọn đầy đủ thông tin.');
      return;
    }
    if (source === target) {
      alert('Không thể tạo quan hệ với chính mình.');
      return;
    }
    onSubmit(source, target, type);
  };

  // Lọc danh sách, không cho chọn node nguồn làm node đích
  const targetNodes = nodes.filter(n => n.id !== source);
  const sourceNodes = nodes.filter(n => n.id !== target);

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="modal-close" onClick={onClose}>&times;</button>
        <h3>Tạo mối quan hệ mới</h3>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Từ thành viên:</label>
            <select value={source} onChange={(e) => setSource(e.target.value)} required>
              <option value="" disabled>-- Chọn thành viên 1 --</option>
              {sourceNodes.map(node => (
                // Dùng data.name và data.sex từ node
                <option key={node.id} value={node.id}>
                  {node.data.name} ({node.data.dob})
                </option>
              ))}
            </select>
          </div>
          
          <div className="form-group">
            <label>Loại quan hệ:</label>
            <select value={type} onChange={(e) => setType(e.target.value)} required>
              {RELATIONSHIP_TYPES.map(t => (
                <option key={t} value={t}>{t}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Đến thành viên:</label>
            <select value={target} onChange={(e) => setTarget(e.target.value)} required>
              <option value="" disabled>-- Chọn thành viên 2 --</option>
              {targetNodes.map(node => (
                // Dùng data.name và data.sex từ node
                <option key={node.id} value={node.id}>
                  {node.data.name} ({node.data.dob})
                </option>
              ))}
            </select>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-submit">Tạo quan hệ</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RelationshipFormModal;