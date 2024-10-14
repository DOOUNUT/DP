// Chat.js
import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import './ChatWindow.css';

function Chat() {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [notification, setNotification] = useState('');
    const [currentUser, setCurrentUser] = useState('myProfile'); // 현재 사용자의 프로필 식별자
    const [isSharing, setIsSharing] = useState(false);
    const localVideoRef = useRef(null);
    const remoteVideoRef = useRef(null);
    const peerConnectionRef = useRef(null);

    useEffect(() => {
        fetchMessages();
        initWebRTC();
    }, []);

    const fetchMessages = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/messages');
            setMessages(response.data);
        } catch (error) {
            console.error('메시지를 가져오는 데 실패했습니다:', error);
        }
    };

    const sendMessage = async () => {
        if (newMessage) {
            try {
                await axios.post('http://localhost:8080/api/messages', { content: newMessage, sender: currentUser });
                setNewMessage('');
                fetchMessages();
                setNotification('메시지가 전송되었습니다!');
                setTimeout(() => setNotification(''), 3000);
            } catch (error) {
                console.error('메시지 전송에 실패했습니다:', error);
            }
        }
    };

    const startScreenShare = async () => {
        try {
            const stream = await navigator.mediaDevices.getDisplayMedia({ video: true });
            localVideoRef.current.srcObject = stream;

            if (peerConnectionRef.current) {
                stream.getTracks().forEach(track => {
                    peerConnectionRef.current.addTrack(track, stream);
                });
            }

            setIsSharing(true);
        } catch (error) {
            console.error('화면 공유에 실패했습니다:', error);
        }
    };

    const initWebRTC = () => {
        // WebRTC 초기화 코드 (ICE 후보 및 시그널링 등)
        peerConnectionRef.current = new RTCPeerConnection();

        // Remote Video Stream
        peerConnectionRef.current.ontrack = (event) => {
            remoteVideoRef.current.srcObject = event.streams[0];
        };
    };

    const handleVideoClick = () => {
        if (localVideoRef.current) {
            if (localVideoRef.current.requestFullscreen) {
                localVideoRef.current.requestFullscreen();
            } else if (localVideoRef.current.webkitRequestFullscreen) { // Safari
                localVideoRef.current.webkitRequestFullscreen();
            } else if (localVideoRef.current.msRequestFullscreen) { // IE/Edge
                localVideoRef.current.msRequestFullscreen();
            }
        }
    };

    return (
        <div className="app">
            <div className="main-content">
                <h1 className="app-title">채팅</h1>
                {notification && <div className="notification">{notification}</div>}
                <ul className="message-list">
                    {messages.map((msg, index) => (
                        <li
                            key={index}
                            className={`message-item ${msg.sender === currentUser ? 'my-message' : 'other-message'}`}
                        >
                            <span className="message-sender">{msg.sender === currentUser ? '나' : '상대방'}:</span> 
                            {msg.content}
                        </li>
                    ))}
                </ul>
                <div className="input-area">
                    <input
                        type="text"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="메시지를 입력하세요"
                        className="message-input"
                    />
                    <button onClick={sendMessage} className="send-button">전송</button>
                    <button onClick={startScreenShare} className="share-button" disabled={isSharing}>
                        {isSharing ? '화면 공유 중...' : '화면 공유 시작'}
                    </button>
                </div>
                <div className="video-container">
                    <video
                        ref={localVideoRef}
                        autoPlay
                        playsInline
                        style={{ width: '60%', height: 'auto', marginRight: '5%' }}
                        onClick={handleVideoClick} // 클릭 시 전체 화면으로 전환
                    />
                    <video ref={remoteVideoRef} autoPlay playsInline style={{ width: '60%', height: 'auto' }} />
                </div>
            </div>
        </div>
    );
}

export default Chat;
