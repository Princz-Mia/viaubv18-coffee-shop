import axios from 'axios';
import { getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};

export const getProductsPage = async (pageNumber, productsPerPage) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/products?pageNumber=${pageNumber}&pageSize?=${productsPerPage}`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
}

export const getBySearchTerm = async (searchTerm, pageNumber, productsPerPage) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/products/search/${searchTerm}?pageNumber=${pageNumber}&pageSize=${productsPerPage}`);
        return response;
    } catch (error) {
        throw error;
    }
};

export const getProductById = async productId => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/products/getById/` + productId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getProductByName = async productName => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/products/getByName/` + productName);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const deleteProductById = async productId => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/products/deleteById/` + productId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getAllPorductCategory = async () => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/product/category/getAllCategory`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const createNewProduct = async productRequest => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/products/createNewProduct`, productRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const updateProduct = async productRequest => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/products/updateProduct`, productRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const reStockProductById = async (productId) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/products/reStockProductById/` + productId, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};