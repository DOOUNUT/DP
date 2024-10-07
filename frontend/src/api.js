// frontend/src/services/api.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/messages';

export const sendMessage = (message) => {
  return axios.post(API_URL, { text: message });
};
