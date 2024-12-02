import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';

function Login({ onLoginSuccess }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                username,
                password,
            });

            // 서버 응답 처리
            if (response.data.message === '로그인 성공') {
                setMessage('로그인 성공');
                onLoginSuccess(response.data.username); // 성공적으로 로그인한 경우 부모 컴포넌트에 사용자 이름 전달
            } else {
                setMessage('로그인 실패: 사용자 정보 오류');
            }
        } catch (error) {
            setMessage('로그인 실패: 서버 오류');
        }
    };

    return (
        <div className="login-container">
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
