import React, { createContext, useContext, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './useAuth';

import * as productService from '../api/productService';
import * as cartService from '../api/cartService';

const CartContext = createContext(null);

export default function CartProvider( { children } ) {
    const navigate = useNavigate();
    const { user } = useAuth();
    const [shoppingCart, setShoppingCart] = useState(null);
    const [shoppingCartItems, setShoppingCartItems] = useState([]);
    const [totalPrice, setTotalPrice] = useState();
    const [totalCount, setTotalCount] = useState();
    const [isChanged, setIsChanged] = useState(false);

    useEffect(() => {
        if (user) {
            const fetchCart = async () => {
                try {
                    const response = await cartService.getCartByUserId(user.id);
                    setShoppingCart(response.data);
                } catch (error) {
                    toast.error("Cannot load your cart at the moment. Please refresh the page and try again later.");
                    navigate("/");
                }
            };
            fetchCart();
        }
    }, [user]);

    useEffect(() => {
        if (shoppingCart) {
            const fetchCartItems = async () => {
                try {
                    const response = await cartService.getCartItemsByCartId(shoppingCart.id)
                    setShoppingCartItems(response.data);
                } catch (error) {
                    toast.error("Cannot load the products in your cart at the moment. Please refresh the page and try again later.");
                    navigate("/");
                }    
            }
            fetchCartItems();
            setIsChanged(false);
        }
    }, [shoppingCart, isChanged]);

    useEffect(() => {
        try {
            const totalPrice = sum(shoppingCartItems.map(item => item.qty * item.product.price));
            const totalCount = sum(shoppingCartItems.map(item => item.qty));
            setTotalPrice(totalPrice);
            setTotalCount(totalCount);
        } catch (error) {
            toast.error(error.message);
        }
    }, [shoppingCartItems, isChanged]);

    const sum = items => {
        return items.reduce((prevValue, curValue) => prevValue + curValue, 0);
    };

    const removeFromCart = async productId => {
        const shoppingCartItemRequest = {
            cartId: shoppingCart.id,
            productId: productId,
            qty: 0
        };
        
        try {
            await cartService.removeProductFromCart(shoppingCartItemRequest);
    
            const filteredShoppingCartItems = shoppingCartItems.filter(item => item.product.id !== productId);
            setShoppingCartItems(filteredShoppingCartItems);

        } catch (error) {
            throw error;
        }
    };

    const changeQuantity = async (selectedShoppingCartItem, newQuantity) => {
        const shoppingCartItemRequest = {
            cartId: shoppingCart.id,
            productId: selectedShoppingCartItem.product.id,
            qty: newQuantity
        };
    
        try {
            await cartService.addProductToCart(shoppingCartItemRequest);
    
            const changedShoppingCartItem = {...selectedShoppingCartItem, qty: newQuantity };

            const updatedShoppingCartItems = shoppingCartItems.map(item => (item.product.id === selectedShoppingCartItem.product.id ? changedShoppingCartItem : item));
            setShoppingCartItems(updatedShoppingCartItems);

        } catch (error) {
            toast.error("Failed to update quantity. Please try again later.");
        }
    };

    const addToCart = async product => {
        try {
            const shoppingCartItem = shoppingCartItems.find(item => item.product.id === product.id);
    
            if (shoppingCartItem) {
                const shoppingCartItemRequest = {
                    cartId: shoppingCart.id,
                    productId: product.id,
                    qty: shoppingCartItem.qty + 1
                };
                cartService.addProductToCart(shoppingCartItemRequest);
                changeQuantity(shoppingCartItem, shoppingCartItem.qty + 1);
            } else {
                const shoppingCartItemRequest = {
                    cartId: shoppingCart.id,
                    productId: product.id,
                    qty: 1
                };
                cartService.addProductToCart(shoppingCartItemRequest);
                setIsChanged(true);
            }
        } catch (error) {
            toast.error(error.message);
        }
    };

    const clearCart = () => {
        try {
            shoppingCartItems.map(item => {
                removeFromCart(item.product.id);
            });
            setIsChanged(true);
        } catch (error) {
            toast.error(error.message);
        }
    };

    return (
        <CartContext.Provider value={{ cart: {products: shoppingCartItems, totalPrice, totalCount }, removeFromCart, changeQuantity, addToCart, clearCart }}>
            {children}
        </CartContext.Provider>
    );
}

export const useCart = () => useContext(CartContext);