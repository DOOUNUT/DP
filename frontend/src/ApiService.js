import axios from 'axios';

const API_URL = 'http://localhost:8080/api/subjects';

export const addSubject = (subjectData) => {
    return axios.post(API_URL, subjectData);
};

export const getSubjects = () => {
    return axios.get(API_URL);
};
