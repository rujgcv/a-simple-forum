import axios from 'axios';
import { getItemWithExpire } from '@/utils/localStorageUtil';

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.timeout = 5000;

axios.interceptors.request.use(config => {
    const token = getItemWithExpire('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

export default axios;