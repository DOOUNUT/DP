import React, { useState, useEffect } from 'react';
import Login from './Login';  // Login 컴포넌트 import
import Sidebar from './Sidebar'; // 사이드바 컴포넌트
import './App.css';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태
    const [username, setUsername] = useState(''); // 로그인한 사용자 이름
    const [courses, setCourses] = useState([]); // 수강중인 과목 목록
    const [selectedCourse, setSelectedCourse] = useState(null); // 선택된 과목

    // 로그인 성공 시 호출되는 함수
    const handleLoginSuccess = (username) => {
        setIsLoggedIn(true);
        setUsername(username);
        fetchCourses(username); // 로그인 후 과목 목록 가져오기
    };

    // 과목 목록을 DB에서 가져오는 함수
    const fetchCourses = async (username) => {
        try {
            const response = await fetch(`/api/courses/${username}`); // 서버에서 과목 목록 가져오기
            const data = await response.json();
            setCourses(data); // 가져온 과목 목록을 상태에 저장
        } catch (error) {
            console.error("Error fetching courses:", error);
        }
    };

    // 과목 클릭 시 해당 과목의 게시판 표시
    const handleCourseClick = (courseId) => {
        setSelectedCourse(courseId);
    };

    return (
        <div className="app">
            <Sidebar isLoggedIn={isLoggedIn} username={username} />
            <div className="main-content">
                {!isLoggedIn ? (
                    <div className="login-container">
                        <h2>로그인</h2>
                        <Login onLoginSuccess={handleLoginSuccess} /> {/* 로그인 컴포넌트 렌더링 */}
                    </div>
                ) : (
                    <div className="course-list">
                        <h2>수강중인 과목</h2>
                        <ul>
                            {courses.map((course) => (
                                <li key={course.id} onClick={() => handleCourseClick(course.id)}>
                                    {course.name}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {selectedCourse && (
                    <div className="course-board">
                        <h3>게시판 - 과목 ID: {selectedCourse}</h3>
                        {/* 여기에 해당 과목의 게시판 내용을 추가할 수 있습니다 */}
                    </div>
                )}
            </div>
        </div>
    );
}

export default App;
