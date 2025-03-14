import axios from 'axios';
import { getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};

export const getOrCreateAddress = async (addressRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/address/create`, addressRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const setAddressToUser = async (userId, addressRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/address/setToUser/` + userId, addressRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};