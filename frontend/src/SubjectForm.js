import React, { useState } from 'react';
import { addSubject } from '../services/ApiService'; // 과목 추가 서비스

function SubjectForm() {
    const [subjectName, setSubjectName] = useState('');
    const [professor, setProfessor] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        const subjectData = { subjectName, professor };
        
        addSubject(subjectData)
            .then(() => alert('과목이 추가되었습니다.'))
            .catch((error) => alert('과목 추가 실패'));
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={subjectName}
                onChange={(e) => setSubjectName(e.target.value)}
                placeholder="과목 이름"
            />
            <input
                type="text"
                value={professor}
                onChange={(e) => setProfessor(e.target.value)}
                placeholder="교수님 이름"
            />
            <button type="submit">과목 추가</button>
        </form>
    );
}

export default SubjectForm;
