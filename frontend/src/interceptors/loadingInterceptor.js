import axios from "axios";

export const setLoadingInterceptor = ({ showLoading, hideLoading }) => {
    axios.interceptors.request.use(
        (req) => {
            if (!req.headers['disableLoading']) {
                showLoading();
            }
            return req;
        },
        (error) => {
            hideLoading();
            return Promise.reject(error);
        }
    );

    axios.interceptors.response.use(
        (res) => {
            hideLoading();
            return res;
        },
        (error) => {
            hideLoading();
            return Promise.reject(error);
        }
    );
};

export default setLoadingInterceptor;
