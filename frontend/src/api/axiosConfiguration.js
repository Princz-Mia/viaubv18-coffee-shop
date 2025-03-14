import axios from 'axios';

export const getAuthToken = () => {
    return localStorage.getItem("auth_token");
};

export const setAuthToken = (token) => {
    token ? localStorage.setItem("auth_token", token) : localStorage.setItem("auth_token", null);
};

export const config = {
    headers: { Authorization: `Bearer ${localStorage.getItem("auth_token")}` }
};

axios.defaults.headers.post['Content-Type'] = 'application/json';