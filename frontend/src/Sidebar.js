import React from 'react';
import './Sidebar.css';

function Sidebar() {
    return (
        <div className="sidebar">
            <div className="profile-section">
                <h2>프로필</h2>
                <p>사용자 이름: 사용자1</p>
                <p>상태: 온라인</p>
                <button className="settings-button">설정</button>
            </div>
            <div className="server-list">
                <h2>학과</h2>
                <ul>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
        </div>
    );
}

export default Sidebar;
