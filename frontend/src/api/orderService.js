import axios  from 'axios';
import { getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};

export const createShopOrder = async (shopOrderRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/shopOrder/create`, shopOrderRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const createOrderItem = async (shopOrderItemRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/shopOrderItem/create`, shopOrderItemRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getShippingMethods = async () => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shippingMethod/all`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getPendingShopOrderByUserId = async (userId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrder/pending/` + userId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getShopOrderItemsByShopOrderId = async (shopOrderId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrderItem/getByShopOrderId/` + shopOrderId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const payShopOrder = async (paymentRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/shopOrder/pay`, paymentRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const hasUserPendingOrder = async (userId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrder/hasPendingShopOrder/` + userId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getShopOrdersByUserId = async (userId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrder/getShopOrdersByUserId/` + userId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getByOrderId = async (orderId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrder/getByShopOrderId/` + orderId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getPageOfOrders = async (pageNumber, ordersPerPage) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrder/pagination?pageNumber=${pageNumber}&pageSize?=${ordersPerPage}`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const updateOrderStatus = async (shopOrderRequest) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/shopOrder/updateShopOrderStatus`, shopOrderRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
        return null;
    }
};

export const deleteById = async (orderId) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/shopOrder/deleteById/` + orderId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getShopOrderStatuses = async () => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/shopOrderStatus/getAllStatus`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};