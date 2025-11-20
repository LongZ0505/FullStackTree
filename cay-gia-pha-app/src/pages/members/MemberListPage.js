import React, { useState, useEffect } from 'react';
// import { getMembersList } from '../../api/genealogyApi'; // Tạm thời tắt API
import './MemberListPage.css';
import {  getNodeList } from '../../api/apiConfig';
// --- THÊM DỮ LIỆU DEMO ---



const MemberListPage = () => {
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchMembers = async () => {
      try {
        setLoading(true);
        const data = await getNodeList(); // 1. Tạm thời tắt API thật
        
        // 2. Giả lập API trả về sau 1 giây và dùng data demo
        setTimeout(() => {
          setMembers(data.result);
          setLoading(false);
        }, 1000); // 1000ms = 1 giây

      } catch (err) {
        setError('Không thể tải danh sách thành viên.');
        setLoading(false); // Đảm bảo tắt loading kể cả khi lỗi
      }
     
    };
    fetchMembers();
  }, []);

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="member-list-container">
      <h2>Danh sách thành viên</h2>
      <table className="member-table">
        <thead>
          <tr>
            <th>THẾ HỆ</th>
            <th>HỌ TÊN</th>
            <th>GIỚI TÍNH</th>
            <th>NGÀY SINH</th>
            <th>NGÀY MẤT</th>
          </tr>
        </thead>
        <tbody>
          {members.map((member) => (
            <tr key={member.id}>
              <td>{member.generation}</td>
              <td>{member.name}</td>
              <td>{member.sex ? 'Nam' : 'Nữ'}</td> 
              <td>{member.dob}</td>
              <td>{member.dod || 'N/A'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MemberListPage;