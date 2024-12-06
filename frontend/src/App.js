import React, { useState, useEffect } from 'react';
import Login from './Login';  // Login 컴포넌트 import
import Sidebar from './Sidebar';  // Sidebar 컴포넌트 import
import axios from 'axios';  // axios import
import './App.css';

// Axios 인스턴스 설정
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
});

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [username, setUsername] = useState('');
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [loading, setLoading] = useState(false); // 로딩 상태 추가

    // 페이지 로딩 시 JWT 토큰이 있으면 자동 로그인
    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            axiosInstance.defaults.headers['Authorization'] = `Bearer ${token}`;
            axiosInstance.get('/auth/validate') // 토큰 유효성 검증 API 호출
                .then(() => {
                    setIsLoggedIn(true);
                    setUsername(localStorage.getItem('username') || '');
                    fetchCourses(); // 로그인 시 과목 목록 불러오기
                })
                .catch((error) => {
                    console.error("토큰 검증 실패:", error);
                    handleLogout(); // 토큰 유효성 실패 시 로그아웃 처리
                });
        }
    }, []);

    // 로그인 성공 시 호출
    const handleLoginSuccess = (userInfo) => {
        setIsLoggedIn(true);
        setUsername(userInfo.username);
        localStorage.setItem('username', userInfo.username);
        localStorage.setItem('jwtToken', userInfo.accessToken); // 로그인 성공 시 JWT 토큰 저장
        fetchCourses(); // 로그인 후 과목 목록 불러오기
    };

    // 로그아웃 처리
    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('username');
        axiosInstance.defaults.headers['Authorization'] = '';
        setIsLoggedIn(false);
        setUsername('');
        setCourses([]);
        setSelectedCourse(null);
    };

    // 과목 목록 가져오기
    const fetchCourses = async () => {
        setLoading(true); // 로딩 시작
        try {
            const response = await axiosInstance.get('/subjects'); // '/subjects'로 수정
            setCourses(response.data);
        } catch (error) {
            console.error("과목 목록 가져오기 오류:", error);
        } finally {
            setLoading(false); // 로딩 끝
        }
    };

    // 과목 클릭 시 처리
    const handleCourseClick = (courseId) => {
        if (selectedCourse !== courseId) {
            setSelectedCourse(courseId);
            // 게시판 내용에 대한 API 요청도 추가할 수 있습니다.
        }
    };

    return (
        <div className="app">
            <Sidebar isLoggedIn={isLoggedIn} username={username} onLogout={handleLogout} />
            <div className="main-content">
                {!isLoggedIn ? (
                    <div className="login-container">
                        <h2>로그인</h2>
                        <Login onLoginSuccess={handleLoginSuccess} />
                    </div>
                ) : (
                    <div className="course-list">
                        <h2>수강 중인 과목</h2>
                        {loading ? (
                            <p>로딩 중...</p>
                        ) : (
                            courses.length > 0 ? (
                                <ul>
                                    {courses.map((course) => (
                                        <li key={course.id} onClick={() => handleCourseClick(course.id)}>
                                            {course.subjectName} {/* course.name 대신 subjectName */}
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p>수강 중인 과목이 없습니다.</p>
                            )
                        )}
                    </div>
                )}

                {selectedCourse && (
                    <div className="course-board">
                        <h3>게시판 - 과목 ID: {selectedCourse}</h3>
                        {/* 해당 과목의 게시판 내용을 추가할 수 있습니다 */}
                    </div>
                )}
            </div>
        </div>
    );
}

export default App;
