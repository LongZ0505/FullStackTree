import React, { useEffect, useState, useRef, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import { Avatar } from "@mui/material";
import { BsCameraVideo, BsInfoCircle, BsTelephone } from "react-icons/bs";
import { FaRegSmile } from "react-icons/fa";
import Picker from "emoji-picker-react";
import moment from 'moment';

// Import API
import { getMessages, sendMessage, getConversationsByConversationId } from "../../api/apiConfig";
import "./MessageDetail.css";
import { io, Socket } from 'socket.io-client';

const MessageDetail = ({ socket }) => {
    const { sessionId } = useParams(); // Lấy ID hội thoại từ URL
    const { user: userInfo } = useAuth();
    const socketRef = useRef(null);
    const [messages, setMessages] = useState([]);
    const [currentChat, setCurrentChat] = useState(null); // Thông tin partner
    const [newMessage, setNewMessage] = useState("");
    const [showEmoji, setShowEmoji] = useState(false);

    const scrollRef = useRef();

    // --- 1. LOAD DỮ LIỆU CHAT ---
    useEffect(() => {
        const fetchChatData = async () => {
            try {
                // A. Lấy thông tin hội thoại (để biết partner là ai)
                const sessionRes = await getConversationsByConversationId(sessionId);
                const sessionData = sessionRes.result;
                console.log("okokokoko", sessionData)
                // Tìm người đối thoại
                const partner = sessionData.participants.find(p => p.userId !== userInfo.id) || sessionData.participants[0];
                console.log("partner", partner)
                setCurrentChat({ ...sessionData, partner });

                // B. Lấy tin nhắn cũ
                const msgRes = await getMessages(sessionId);
                console.log("chatsss: ", msgRes)
                setMessages(msgRes.result || []);
            } catch (error) {
                console.error("Lỗi tải chat:", error);
            }
        };

        if (sessionId && userInfo) {
            fetchChatData();
        }
    }, [sessionId, userInfo]);

    // --- 2. SOCKET LISTEN ---
    // useEffect(() => {
    //     if (!socket.current) return;

    //     const handleIncoming = (data) => {

    //         console.log("SOCKET MESSAGE:", data);

    //         // Chỉ push tin nhắn nếu đúng hội thoại
    //         if (data.conversationId === sessionId) {
    //             setMessages(prev => [
    //                 ...prev,
    //                 {
    //                     chatId: data.conversationId || Date.now(),
    //                     userId: data.userId,
    //                     message: data.message,
    //                     chatTimestamp: data.chatTimestamp
    //                 }
    //             ]);
    //         }
    //     };

    //     // Backend gửi dạng JSON string => parse ra
    //     socket.current.on("message", (raw) => {
    //         try {
    //             console.log("raw,",raw)
    //             const parsed = JSON.parse(raw);
    //             console.log("received Message: ",JSON.parse(raw))
    //             handleIncoming(parsed);
    //         } catch (e) {
    //             console.error("Lỗi parse message:", e);
    //         }
    //     });

    //     return () => {
    //         socket.current.off("message");
    //     };
    // }, [socket, sessionId]);
    useEffect(() => {
        if (!userInfo) return;

        const token = localStorage.getItem("token");
        socketRef.current = io("http://localhost:8099", {
            query: { token },
        });

        socketRef.current.on("connect", () => console.log("Socket connected"));
        socketRef.current.on("disconnect", () => console.log("Socket disconnected"));

        socketRef.current.on("message", (raw) => {
            try {
                const data = typeof raw === "string" ? JSON.parse(raw) : raw;
                if (data.conversationId === sessionId) {
                    setMessages(prev => [...prev, data]);
                }
            } catch (err) {
                console.error("Lỗi parse message:", err);
            }
        });

        return () => {
            socketRef.current.disconnect();
            socketRef.current = null;
        };
    }, [sessionId, userInfo]);

    const handleIncomingMessage = useCallback((message) => {
        setMessages((prev) => [...prev, {
            chatId: Date.now(),
            userId: userInfo.userId,
            message: message,
            chatTimestamp: new Date().toISOString()
        }]);
        console.log(messages);
    }, []);
    // --- 3. SCROLL TO BOTTOM ---
    useEffect(() => {
        scrollRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    // --- 4. GỬI TIN NHẮN ---
    const handleSendMessage = async () => {
        if (!newMessage.trim()) return;
        const msgText = newMessage;

        // // A. Gửi qua Socket
        // socket.current.emit("send-msg", {
        //     to: currentChat?.partner?.userId,
        //     from: userInfo.userId,
        //     conversationId: sessionId,
        //     msg: msgText,
        // });

        // B. Lưu DB
        try {
            const formData = {
                conversationId: sessionId,
                senderId: userInfo.id,
                message: msgText,
                chatTimestamp: new Date().toISOString()
            };
            console.log("data:", formData)
            await sendMessage(formData);

            // C. Update UI
            // setMessages((prev) => [...prev, {
            //     chatId: Date.now(),
            //     userId: userInfo.userId,
            //     message: msgText,
            //     chatTimestamp: new Date().toISOString()
            // }]);

            setNewMessage("");
            setShowEmoji(false);
        } catch (error) {
            console.error("Gửi lỗi:", error);
        }
    };

    return (
        <div className="messagePage_right">
            {/* HEADER */}
            <div className="messagePage_right_up">
                <div className="messagePage_right_up_left">
                    <Avatar sx={{ width: "32px", height: "32px" }} src={currentChat?.partner?.avatar} />&nbsp;&nbsp;
                    <div>
                        <div style={{ fontSize: "1.1rem" }}><b>{currentChat?.partner?.name}</b></div>
                    </div>
                </div>
                <div className="messagePage_right_up_right">
                    {/* <BsTelephone className="icon-header" />
                    <BsCameraVideo className="icon-header" />
                    <BsInfoCircle className="icon-header" /> */}
                </div>
            </div>

            {/* BODY */}
            <div className="messagePage_right_down">
                <div className="messagePage_right_down_chat">
                    {messages.map((chat, index) => {
                        console.log(chat.userId, "---", userInfo.id)
                        const isMine = chat.userId === userInfo.id;
                        return (
                            <div ref={scrollRef} className="messagePage_right_down_chatDetail" key={index}>
                                <div className="messagePage_time">
                                    {moment(new Date(chat.chatTimestamp)).format("h:mm a")}
                                </div>
                                {isMine ? (
                                    <div className="messagePage_self">
                                        <div className="box_self">{chat.message}</div>
                                    </div>
                                ) : (
                                    <div className="messagePage_counterpart">
                                        <Avatar sx={{ width: "25px", height: "25px" }} src={currentChat?.partner?.avatar} />
                                        <div className="box_other">{chat.message}</div>
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>

                {/* FOOTER INPUT */}
                <div className="messagePage_right_down_text">
                    <div className="messagePage_right_down_border">
                        <div className="postCard_comment">
                            {showEmoji && (
                                <div className="emoji-picker-container" onClick={e => e.stopPropagation()}>
                                    <Picker
                                        height={350}
                                        width="100%"
                                        onEmojiClick={(emojiData) => {
                                            // Trong bản mới, tham số đầu tiên chính là data
                                            setNewMessage(prev => prev + emojiData.emoji);
                                        }}
                                    />
                                </div>
                            )}
                            <div className="postCard_commentEmoji">
                                <FaRegSmile className="icon-smile" onClick={(e) => {
                                    e.stopPropagation(); setShowEmoji(!showEmoji);
                                }} />
                                <input type="text"
                                    value={newMessage}
                                    placeholder="Nhắn tin..."
                                    className="postCard_commentInput"
                                    onChange={e => setNewMessage(e.target.value)}
                                    onKeyDown={e => e.key === "Enter" && handleSendMessage()}
                                />
                            </div>
                            <button
                                className={newMessage.trim() ? "btn-send active" : "btn-send"}
                                onClick={handleSendMessage}
                            >
                                Gửi
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MessageDetail;