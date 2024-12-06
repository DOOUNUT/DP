import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Sidebar.css';

// axios 인스턴스 설정
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api/',
    headers: {
        'Content-Type': 'application/json',
    },
});

function Sidebar({ isLoggedIn, username, onLogout }) {
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [newCourse, setNewCourse] = useState('');

    // 페이지 로딩 시 JWT 토큰을 이용해 Authorization 헤더 설정
    useEffect(() => {
        if (isLoggedIn) {
            const token = localStorage.getItem('jwtToken');
            axiosInstance.defaults.headers['Authorization'] = `Bearer ${token}`;
            fetchCourses(); // 로그인 후 과목 목록 불러오기
        } else {
            axiosInstance.defaults.headers['Authorization'] = '';
        }
    }, [isLoggedIn]);

    // 과목 목록 가져오기
    const fetchCourses = () => {
        axiosInstance
            .get('subjects')
            .then((response) => setCourses(response.data))
            .catch((error) => console.error('Error fetching courses:', error));
    };

    // 과목 클릭 시 세부 정보 가져오기
    const handleCourseClick = (courseId) => {
        axiosInstance
            .get(`subjects/${courseId}`)
            .then((response) => setSelectedCourse(response.data))
            .catch((error) => console.error('Error fetching subject details:', error));
    };

    // 과목 추가
    const handleAddCourse = () => {
        if (newCourse.trim()) {
            const courseData = {
                subjectName: newCourse,  // 과목 이름
                professor: 'Test Professor',  // 예시로 'Test Professor'로 추가
                description: 'No description available',  // 예시로 설명 추가
                user_id: null,  // 예시로 NULL 처리
            };

            axiosInstance
                .post('subjects', courseData)
                .then((response) => {
                    setCourses((prevCourses) => [...prevCourses, response.data]); // 새 과목을 기존 과목 목록에 추가
                    setNewCourse('');
                })
                .catch((error) => console.error('Error adding new course:', error));
        }
    };

    // 로그아웃 처리
    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        axiosInstance.defaults.headers['Authorization'] = '';
        onLogout(); // 부모 컴포넌트에 로그아웃 알림
    };

    return (
        <div className="sidebar">
            <div className="profile-section">
                <h2>프로필</h2>
                {isLoggedIn ? (
                    <>
                        <p>학번: {username}</p>
                        <p>상태: 온라인</p>
                        <button className="settings-button">설정</button>
                        <button className="logout-button" onClick={handleLogout}>
                            로그아웃
                        </button>
                    </>
                ) : (
                    <p>로그인 후 이용 가능합니다.</p>
                )}
            </div>

            {isLoggedIn && (
                <div className="course-list">
                    <h2>수강 중인 과목</h2>
                    {courses && courses.length > 0 ? (
                        <ul>
                            {courses.map((course) => (
                                <li key={course.id} onClick={() => handleCourseClick(course.id)}>
                                    {course.subjectName} {/* 수정된 부분: subjectName 사용 */}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>수강 중인 과목이 없습니다.</p>
                    )}

                    <div className="add-course">
                        <input
                            type="text"
                            placeholder="새로운 과목 추가"
                            value={newCourse}
                            onChange={(e) => setNewCourse(e.target.value)}
                        />
                        <button onClick={handleAddCourse} className="add-course-button">
                            과목 추가
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Sidebar;
