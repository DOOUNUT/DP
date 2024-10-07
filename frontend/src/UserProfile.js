import React from 'react';
import './UserProfile.css';

function UserProfile() {
    return (
        <div className="user-profile">
            <h2>사용자 프로필</h2>
            <p>사용자 이름: 사용자1</p>
            <p>상태: 온라인</p>
            <button>설정</button>
        </div>
    );
}

export default UserProfile;
