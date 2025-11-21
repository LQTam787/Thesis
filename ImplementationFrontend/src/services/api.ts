import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // Thay đổi thành URL backend của bạn
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
