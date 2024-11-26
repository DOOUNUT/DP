import React, { useState } from 'react';
import './Sidebar.css';
import Login from './Login'; // 로그인 컴포넌트를 불러옵니다.

function Sidebar() {
    const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리
    const [username, setUsername] = useState('');

    const handleLoginSuccess = (username) => {
        setIsLoggedIn(true);
        setUsername(username);
    };

    return (
        <div className="sidebar">
            <div className="profile-section">
                <h2>프로필</h2>
                {isLoggedIn ? (
                    <>
                        <p>사용자 이름: {username}</p>
                        <p>상태: 온라인</p>
                        <button className="settings-button">설정</button>
                    </>
                ) : (
                    <Login onLoginSuccess={handleLoginSuccess} />
                )}
            </div>
            <div className="server-list">
                <h2>학과</h2>
                <ul>
                    <li>학과 1</li>
                    <li>학과 2</li>
                    <li>학과 3</li>
                </ul>
            </div>
        </div>
    );
}

export default Sidebar;
