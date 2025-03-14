import axios from 'axios';
import { getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};


export const getCartById = async (id) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/cart/getById/` + id, {
            ...config,
        });
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getCartByUserId = async (userId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/cart/getByUserId/` + userId, {
            ...config,
        });
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getCartItemsByCartId = async (cartId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/cartItem/getByCartId/` + cartId, {
            ...config,
        });
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const addProductToCart = async shoppingCartItemRequest => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/cartItem/add`, shoppingCartItemRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const removeProductFromCart = async shoppingCartItemRequest => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/cartItem/remove`, shoppingCartItemRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};
