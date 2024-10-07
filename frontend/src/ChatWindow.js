import React from 'react';
import './ChatWindow.css';

function ChatWindow({ messages, newMessage, setNewMessage, sendMessage }) {
    return (
        <div className="chat-window">
            <h1>메시지 목록</h1>
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
    );
}

export default ChatWindow;
