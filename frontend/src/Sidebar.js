import React, { useState, useEffect } from 'react';
import './Sidebar.css';

function Sidebar({ isLoggedIn, username }) {
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [newCourse, setNewCourse] = useState('');

    // 컴포넌트가 처음 렌더링될 때 과목 목록을 API로부터 가져옴
    useEffect(() => {
        if (isLoggedIn) {
            fetch('http://localhost:8080/api/subjects') // 백엔드에서 과목 정보 가져오기
                .then((response) => response.json())
                .then((data) => setCourses(data))
                .catch((error) => console.error('Error fetching courses:', error));
        }
    }, [isLoggedIn]);

    // 과목 클릭 시 해당 과목의 게시판을 보여주는 기능
    const handleCourseClick = (courseId) => {
        fetch(`http://localhost:8080/api/subjects/${courseId}`) // 과목 ID로 게시판 정보 가져오기
            .then((response) => response.json())
            .then((data) => setSelectedCourse(data))
            .catch((error) => console.error('Error fetching subject details:', error));
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
                                    {course.name}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>수강 중인 과목이 없습니다.</p>
                    )}

                    {/* 과목 추가 */}
                    <div className="add-course">
                        <input
                            type="text"
                            placeholder="새로운 과목 추가"
                            value={newCourse}
                            onChange={(e) => setNewCourse(e.target.value)}
                        />
                        <button
                            onClick={() => {
                                if (newCourse.trim()) {
                                    setCourses([...courses, { name: newCourse }]);
                                    setNewCourse('');
                                }
                            }}
                            className="add-course-button"
                        >
                            과목 추가
                        </button>
                    </div>
                </div>
            )}

            {/* 선택된 과목에 대한 게시판 보기 */}
            {selectedCourse && (
                <div className="course-board">
                    <h2>{selectedCourse.name} 게시판</h2>
                    <p>{selectedCourse.description}</p>
                    {/* 게시판에 대한 다른 정보나 기능 추가 가능 */}
                </div>
            )}
        </div>
    );
}

export default Sidebar;
