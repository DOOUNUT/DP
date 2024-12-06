import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Login.css';

// Axios 인스턴스 설정
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
});

function Login({ onLoginSuccess }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    // 컴포넌트가 처음 로드될 때 로컬스토리지에서 토큰을 가져와서 Axios 헤더에 설정
    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            axiosInstance.defaults.headers['Authorization'] = `Bearer ${token}`;
        }
    }, []);

    // 로그인 요청 처리
    const handleLogin = async () => {
        if (!username || !password) {
            setMessage('아이디와 비밀번호를 입력해주세요.');
            return;
        }

        setLoading(true);

        try {
            const response = await axiosInstance.post('/auth/login', {
                username,
                password,
            });

            setLoading(false);

            if (response.data.message === '로그인 성공') {
                setMessage('로그인 성공');
                const { accessToken, refreshToken, username } = response.data;

                // 토큰을 로컬 스토리지에 저장
                localStorage.setItem('jwtToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);

                // axios 헤더에 Authorization 토큰 설정
                axiosInstance.defaults.headers['Authorization'] = `Bearer ${accessToken}`;

                // 로그인 성공 후 부모 컴포넌트에 사용자 정보 전달
                onLoginSuccess({ username, accessToken, refreshToken });
            } else {
                setMessage('로그인 실패: 사용자 정보 오류');
            }
        } catch (error) {
            setLoading(false);
            if (error.response && error.response.status === 401) {
                setMessage('로그인 실패: 인증 오류');
            } else {
                setMessage('로그인 실패: 서버 오류');
            }
        }
    };

    // 로그아웃 처리
    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('refreshToken');
        axiosInstance.defaults.headers['Authorization'] = '';
        setUsername('');
        setPassword('');
        setMessage('로그아웃되었습니다.');
        onLoginSuccess(null); // 부모 컴포넌트에 로그인 상태 초기화 전달
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
            <button onClick={handleLogin} disabled={loading}>
                {loading ? '로그인 중...' : '로그인'}
            </button>
            <button onClick={handleLogout} style={{ marginLeft: '10px' }}>
                로그아웃
            </button>
            <p>{message}</p>
        </div>
    );
}

export default Login;
