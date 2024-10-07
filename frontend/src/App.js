import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';
import Sidebar from './Sidebar';

function App() {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [notification, setNotification] = useState('');

    useEffect(() => {
        fetchMessages();
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
                await axios.post('http://localhost:8080/api/messages', { content: newMessage });
                setNewMessage('');
                fetchMessages();
                setNotification('메시지가 전송되었습니다!');
                setTimeout(() => setNotification(''), 3000);
            } catch (error) {
                console.error('메시지 전송에 실패했습니다:', error);
            }
        }
    };

    return (
        <div className="app">
            <Sidebar /> 
            <div className="main-content">
                <h1 className="app-title">채팅</h1>
                {notification && <div className="notification">{notification}</div>}
                <ul className="message-list">
                    {messages.map((msg, index) => (
                        <li key={index} className="message-item">
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
                </div>
            </div>
        </div>
    );
}

export default App;
