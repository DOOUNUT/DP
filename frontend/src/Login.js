import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';

function Login({ onLoginSuccess }) { // 부모 컴포넌트에서 콜백을 받습니다.
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = async () => {
        try {
            // POST 방식으로 로그인 데이터 보내기
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                username,
                password,
            });

            // 서버 응답에 따른 처리
            if (response.data.success) {
                setMessage('로그인 성공');
                onLoginSuccess(username); // 로그인 성공 시 부모에게 알립니다.
            } else {
                setMessage('로그인 실패: 사용자 정보 오류');
            }
        } catch (error) {
            setMessage('로그인 실패: 서버 오류');
        }
    };

    return (
        <div className="login-container">
            <h2>로그인</h2>
            <input
                type="text"
                placeholder="아이디"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="비밀번호"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>로그인</button>
            <p>{message}</p>
        </div>
    );
}

export default Login;
