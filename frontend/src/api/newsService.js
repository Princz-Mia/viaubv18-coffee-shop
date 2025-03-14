import axios from 'axios';
import { getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};

export const getAllNews = async () => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/news`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const getPageOfNews = async (pageNumber, productsPerPage) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/news/pagination?pageNumber=${pageNumber}&pageSize?=${productsPerPage}`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
}

export const getBySearchTerm = async (searchTerm, pageNumber, productsPerPage) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/news/search/${searchTerm}?pageNumber=${pageNumber}&pageSize=${productsPerPage}`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const createNews = async newsRequest => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/news/create`, newsRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const updateNews = async newsRequest => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/news/update`, newsRequest, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const deleteById = async newsId => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/news/deleteById/` + newsId);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

