import axios from 'axios';
import { setAuthToken, getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};

export const getUser = () => localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null;

export const getUserById = async (userId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/user/` + userId, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
    }
};

export const login = async (loginRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/user/login`,  
        { 
            email: loginRequest.email,
            password: loginRequest.password
        });
        localStorage.setItem('user', JSON.stringify(response.data));
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};

export const register = async (registerRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/user/register`, 
        { 
            firstName: registerRequest.firstName, 
            lastName: registerRequest.lastName, 
            email: registerRequest.email, 
            password: registerRequest.password 
        });
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};

export const verify = async (key) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/user/verify/account?key=` + key);
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};

export const requestPasswordReset = async (email) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/user/requestPasswordReset?email=` + email);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
        return null;
    }
};

export const resetPassword = async (passwordResetRequest, key) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/user/resetPassword?key=` + key, 
        { 
            newPassword: passwordResetRequest.newPassword, 
            confirmNewPassword: passwordResetRequest.confirmNewPassword, 
        });
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
        return null;
    }
};

export const logout = () => {
    localStorage.removeItem('user');
};

export const changeNames = async (userId, firstName, lastName) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/user/change/name`, { userId, firstName, lastName }, config);
        localStorage.setItem('user', JSON.stringify(response.data));
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};

export const changeEmail = async (userId, email) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/user/change/email`, { userId, email }, config);
        localStorage.setItem('user', JSON.stringify(response.data));
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};

export const changePhoneNumber = async (userId, phoneNumber) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/user/change/phoneNumber`, { userId, phoneNumber }, config);
        localStorage.setItem('user', JSON.stringify(response.data));
        return response;
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};

export const changePassword = async (userId, passwordChangeRequest) => {
    try {
        await axios.post(`${baseURL}/api/v1/user/change/password/` + userId, passwordChangeRequest, config);
    } catch (error) {
        toast.error(error.response.data.message);
        return null;
    }
};