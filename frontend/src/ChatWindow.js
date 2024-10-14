import React from 'react';

function ChatWindow({ messages, newMessage, setNewMessage, sendMessage }) {
    return (
        <div className="chat-window">
            <div className="messages">
                {messages.map((message, index) => (
                    <div
                        key={index}
                        className={`message ${message.senderType === 'self' ? 'self-message' : 'other-message'}`}
                    >
                        <span className="sender">{message.sender}:</span>
                        <span className="content">{message.content}</span>
                    </div>
                ))}
            </div>
            <div className="input-area">
                <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Type a message..."
                />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
}

export default ChatWindow;
