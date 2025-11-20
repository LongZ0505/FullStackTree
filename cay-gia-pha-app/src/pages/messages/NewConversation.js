import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '../../hooks/useAuth'; 
import { Avatar } from "@mui/material";
import { FaEdit, FaTimes } from "react-icons/fa";
// Import API
import { queryUser, createConversation } from "../../api/apiConfig"; 
import "./NewConversation.css";

const NewConversation = ({ existingSessions = [] }) => {
    const [isSearching, setIsSearching] = useState(false);
    const [keyword, setKeyword] = useState("");
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    
    const navigate = useNavigate();
    const { user: userInfo } = useAuth(); // Lấy thông tin mình

    // --- 1. TÌM KIẾM USER (Debounce) ---
    useEffect(() => {
        const timer = setTimeout(async () => {
            if (keyword.trim().length > 0) {
                setLoading(true);
                try {
                    const res = await queryUser(keyword);
                    // Lọc bỏ bản thân mình khỏi kết quả
                    if (userInfo) {
                        // Lưu ý: Kiểm tra API trả về userName hay username để sửa ở đây
                        const filteredUsers = res.result.filter(u => u.name !== userInfo.name);
                        setUsers(filteredUsers);
                    }
                } catch (error) {
                    console.error("Lỗi tìm kiếm:", error);
                } finally {
                    setLoading(false);
                }
            } else {
                setUsers([]);
            }
        }, 500);

        return () => clearTimeout(timer);
    }, [keyword, userInfo]);

    // --- 2. XỬ LÝ KHI CHỌN USER ---
    const handleSelectUser = async (selectedUser) => {
        // A. Kiểm tra xem đã có chat với người này chưa?
        const existingSession = existingSessions.find(session => {
            const partner = session.participants.find(p => p.userId !== userInfo?.userId);
            // So sánh userId (lưu ý API trả về userId hay id)
            return partner && partner.userId === selectedUser.userId;
        });

        if (existingSession) {
            // B. Có rồi -> Mở chat cũ
            navigate(`/direct/t/${existingSession.id}`);
        } else {
            // C. Chưa có -> Gọi API tạo mới
            try {
                console.log(selectedUser)
                const formData = {
                    participantIds: [userInfo?.id, selectedUser.userId],
                    createdDate: new Date().toISOString(),
                };
                const res = await createConversation(formData); 
                console.log(res)
                if (res && res.result) {
                    console.log("good")
                    // API trả về ID conversation mới
                    navigate(`/direct/t/${res.result}`);
                }
            } catch (error) {
                console.error("Lỗi tạo cuộc hội thoại:", error);
                alert("Không thể tạo cuộc trò chuyện lúc này.");
            }
        }
    };

    // --- GIAO DIỆN 1: NÚT BẤM ---
    if (!isSearching) {
        return (
            <div className="new-convo-wrapper center">
                <div className="icon-circle">
                    <FaEdit size={40} />
                </div>
                <h2>Tin nhắn của bạn</h2>
                <p>Gửi ảnh và tin nhắn riêng tư cho bạn bè và người thân.</p>
                <button className="btn-primary" onClick={() => setIsSearching(true)}>
                    Gửi tin nhắn
                </button>
            </div>
        );
    }

    // --- GIAO DIỆN 2: TÌM KIẾM ---
    return (
        <div className="new-convo-wrapper search-mode">
            <div className="search-header">
                <span>Đến: </span>
                <input 
                    autoFocus
                    type="text" 
                    placeholder="Tìm kiếm..." 
                    value={keyword}
                    onChange={e => setKeyword(e.target.value)}
                />
                <div className="close-btn" onClick={() => setIsSearching(false)}>
                    <FaTimes />
                </div>
            </div>
            
            <div className="search-results">
                {loading && <div className="info-text">Đang tìm...</div>}
                
                {!loading && keyword && users.length === 0 && (
                    <div className="info-text">Không tìm thấy tài khoản nào.</div>
                )}
                
                {users.map((user, index) => (
                    <div key={index} className="user-row" onClick={() => handleSelectUser(user)}>
                        <Avatar src={user.avatar} sx={{width: 44, height: 44}}/>
                        <div className="user-info">
                            <div className="u-name">{user.name}</div> 
                            {/* API trả về username hay userName? Hãy check kỹ */}
                            <div className="u-sub">{user.fullName || "Thành viên"}</div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default NewConversation;