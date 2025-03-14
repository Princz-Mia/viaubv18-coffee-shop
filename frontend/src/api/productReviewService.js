import axios from 'axios';
import { getAuthToken } from '../api/axiosConfiguration'
import { toast } from 'react-toastify';

const baseURL = 'http://localhost:8080';
const config = {
    headers: { Authorization: `Bearer ${getAuthToken()}` }
};

export const getReviewsByProductId = async (productId) => {
    try {
        const response = await axios.get(`${baseURL}/api/v1/productReviews?productId=${productId}`);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};

export const addProductReview = async (userId, productId, review) => {
    try {
        const response = await axios.post(`${baseURL}/api/v1/productReviews/add`, {
            userId: userId,
            productId: productId,
            ratingValue: review.ratingValue,
            comment: review.comment,
        }, config);
        return response;
    } catch (error) {
        toast.error(error.response.data.message)
    }
};