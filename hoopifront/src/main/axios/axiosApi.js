import axios from 'axios';

const api = axios.create({
    baseURL: 'https://hoopi.co.kr/api/'
});

export default api;
