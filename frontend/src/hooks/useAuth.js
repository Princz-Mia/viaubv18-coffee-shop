import { useState, createContext, useContext } from 'react';
import { toast } from 'react-toastify';

import { getAuthToken, setAuthToken } from '../api/axiosConfiguration'

import * as userService from '../api/userService';
import * as orderService from '../api/orderService';
import * as addressService from '../api/addressService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(userService.getUser());

    const login = async (loginRequest) => {
        try {
            const response = await userService.login(loginRequest);

            if (response) {
                // Ohh boy... big stinky hack coming up!
                window.location.reload();

                setUser(response.data);
                setAuthToken(response.data.token);
                //toast.success('Login Successful');
            }
        } catch (error) {
            setAuthToken(null);
            toast.error(error.message);
        }
    };

    const register = async (registerRequest) => {
        try {
            const response = await userService.register(registerRequest);
            if (response)
                toast.success(response.data.message);
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    const verify = async (key) => {
        try {
            const response = await userService.verify(key);
            if (response) {
                toast.success(response.data.message);
                return response;
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    const logout = async () => {
        userService.logout();
        setUser(null);
        setAuthToken(null);
        toast.success('Logout Successful');
    };

    const changeNames = async (firstName, lastName) => {
        try {
            const updatedUser = await userService.changeNames(user.id, firstName, lastName);
            if (updatedUser) {
                setUser(updatedUser.data);
                toast.success('Name change was successful');
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    const changeEmail = async (email) => {
        try {
            const updatedUser = await userService.changeEmail(user.id, email);
            if (updatedUser) {
                setUser(updatedUser.data);
                toast.success('Email address update was successful');
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    const changePhoneNumber = async (phoneNumber) => {
        try {
            const updatedUser = await userService.changePhoneNumber(user.id, phoneNumber);
            if (updatedUser) {
                setUser(updatedUser.data);
                toast.success('Phone number update was successful');
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };
    
    const changePassword = async (passwordChangeRequest) => {
        try {
            const updatedUser = await userService.changePassword(user.id, passwordChangeRequest)
            if (updatedUser) {
                await logout();
                toast.success('Password changed successfully');
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    const setAddressToUser = async (userId, addressRequest) => {
        try {
            const responseAddress = await addressService.setAddressToUser(userId, addressRequest);
            if (responseAddress) {
                user.address = responseAddress.data;
                localStorage.setItem('user', JSON.stringify(user));
                toast.success('Address changed successfully')
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    const fetchPendingOrdersOfUser = async () => {
        try {
            const response = await orderService.hasUserPendingOrder(user.id);
            if (response)
                return response;
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };

    return (
        <AuthContext.Provider value={{ user: user, login, register, verify, logout, fetchPendingOrdersOfUser, changeNames, changeEmail, changePhoneNumber, changePassword, setAddressToUser }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);