import React, { useEffect, useState, useRef } from 'react';
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import { io } from 'socket.io-client';
import useAuth from '../../hooks/useAuth';
import NewConversation from './NewConversation';
import MessageDetail from './MessageDetail';
import { fetchConversationsByUserId } from '../../api/apiConfig';
import { BsChevronDown, BsPencilSquare } from "react-icons/bs";
import { Avatar } from "@mui/material";
import "./ChatPage.css";
import TimeAgo from "javascript-time-ago";
import en from 'javascript-time-ago/locale/en';

const ChatPage = ({ setSwitchAccount }) => {
    TimeAgo.setDefaultLocale(en.locale);
    TimeAgo.addLocale(en);
    const timeAgo = new TimeAgo('en-US');

    const { user } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    // const socketRef = useRef(null);

    const [sessions, setSessions] = useState([]);
    // const [selectedSessionDetail, setSelectedSessionDetail] = useState({}); // (Có thể bỏ nếu không dùng)

    const getDate = (updateTime) => {
        if (!updateTime) return '';
        try {
            const spr = timeAgo.format(new Date(updateTime)).split(' ');
            return spr[0] + (spr[1] ? spr[1][0] : '');
        } catch (e) { return ''; }
    };

    // 1. KẾT NỐI SOCKET
    // useEffect(() => {
    //     if (!socketRef.current) {
    //         console.log("Initializing socket connection...");

    //         const token = localStorage.getItem('token');
    //         console.log("token  ", token)
    //         socketRef.current = io("http://localhost:8099", {
    //                 query: {    
    //                     token: token
    //                 }
    //             });
    //         socketRef.current.on("connect", () => {
    //             console.log("Socket connected");
    //         });

    //         socketRef.current.on("disconnect", () => {
    //             console.log("Socket disconnected");
    //         });

    //         // Khi backend gửi "msg-receive" -> reload sidebar
    //         socketRef.current.on("msg-receive", () => {
    //             fetchSessions();
    //         });
    //     }

    //     return () => {
    //         if (socketRef.current) {
    //             socketRef.current.disconnect();
    //             socketRef.current = null;
    //         }
    //     };
    // }, []);

    // 2. LẤY DANH SÁCH CHAT
    const fetchSessions = async () => {
        if (user?.id) {
            try {
                const res = await fetchConversationsByUserId(user.id);
                // Dữ liệu trả về là mảng các object như bạn mô tả
                setSessions(res.result || []);
            } catch (error) {
                console.error("Lỗi tải list chat:", error);
            }
        }
    };

    useEffect(() => { fetchSessions(); }, [user]);

    // 3. SOCKET UPDATE
    // useEffect(() => {
    //     if (socketRef.current) {
    //         socketRef.current.on("msg-receive", () => fetchSessions());
    //     }
    // }, []);

    return (
        <div style={{ height: "90vh", display: "flex", flexDirection: "column", position: "relative" }}>
            <div className="messagePage_container">
                <div className="messagePage_main">
                    <div className="messagePage_main2">

                        {/* --- SIDEBAR --- */}
                        <div className="messagePage_left">
                            <div className="messagePage_left_up">
                                <div style={{ marginLeft: "30%" }}><b>{user?.userName}</b></div>&nbsp;
                                <div className="messagePage_change" onClick={() => setSwitchAccount && setSwitchAccount(true)}>
                                    <BsChevronDown />
                                </div>
                                <div className="messagePage_new_msg" onClick={() => navigate('/direct/inbox')}>
                                    <BsPencilSquare size={24} />
                                </div>
                            </div>

                            <div className="messagePage_left_down">
                                <div className="chat_session_container">
                                    {sessions.map((session, index) => {
                                        // --- LOGIC TÌM NGƯỜI CHAT CÙNG (QUAN TRỌNG) ---
                                        // Dựa vào cấu trúc: participants = [{userId, username, avatar}, ...]
                                        const partner = session.participants.find(p => p.userId !== user.id) || session.participants[0];

                                        const isSelected = location.pathname.includes(`/direct/t/${session.id}`);

                                        return (
                                            <div
                                                key={session.id || index}
                                                className="chat_session_containerContent"
                                                style={{ backgroundColor: isSelected ? 'rgb(239,239,239)' : 'unset' }}
                                                onClick={() => navigate(`/direct/t/${session.id}`)}
                                            >
                                                {/* Hiển thị Avatar */}
                                                <Avatar sx={{ width: "50px", height: "50px" }} src={partner?.avatar} />&nbsp;&nbsp;

                                                <div style={{ display: "flex", flexDirection: "column", justifyContent: "center" }}>
                                                    {/* Hiển thị Tên (Dùng field 'username' thay vì 'userName') */}
                                                    <div style={{ marginTop: "0.3rem" }}>
                                                        {partner?.name || "Người dùng"}
                                                    </div>

                                                    {session.messageDigestion && (
                                                        <div className="messagePage_summary">
                                                            <div style={{ width: "180px", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap", color: "gray" }}>
                                                                {session.messageDigestion}
                                                            </div>
                                                            <span style={{ fontSize: '12px', color: 'gray' }}>
                                                                &nbsp;· {getDate(session.updateTime)}
                                                            </span>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            </div>
                        </div>

                        {/* --- ROUTING --- */}
                        <Routes>
                            <Route path="inbox" element={<NewConversation existingSessions={sessions} />} />
                            <Route path="t/:sessionId" element={<MessageDetail  />} />
                        </Routes>

                    </div>
                </div>
            </div>
        </div>
    );
};

export default ChatPage;