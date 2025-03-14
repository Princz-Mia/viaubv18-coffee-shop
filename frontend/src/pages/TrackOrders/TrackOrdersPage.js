import React, { useEffect, useReducer, useState } from 'react'
import classes from './trackOrdersPage.module.css';
import NotFound from '../../components/NotFound/NotFound';
import * as orderService from '../../api/orderService';
import CollapsibleTable from '../../components/CollapsibleTable/CollapsibleTable';
import { useAuth } from '../../hooks/useAuth';
import { toast } from 'react-toastify';

const initialState = { items: [] };

const reducer = (state, action) => {
    switch (action.type) {
        case 'ITEMS_LOADED':
            return { ...state, items: [...state.items, ...action.payload] }; // Régi és új elemek együtt hozzáadása
        default:
             return state;
    }
};


export function TrackOrdersPage() {
    const { user } = useAuth();

    const [state, dispatch] = useReducer(reducer, initialState);
    const { items } = state;

    const [shopOrders, setShopOrders] = useState();

    useEffect(() => {
        const fetchShopOrders = async () => {
            try {
                const response = await orderService.getShopOrdersByUserId(user.id);
                setShopOrders(response.data);
            } catch(error) {
                toast.error(error.response.data.message)
            }
        };

        fetchShopOrders();
    }, []);

    useEffect(() => {
        const fetchShopOrderItems = async () => {
            if (shopOrders) {
                try {
                    let newItems = [];
                    for (const shopOrder of shopOrders) {
                        const response = await orderService.getShopOrderItemsByShopOrderId(shopOrder.id);
                        newItems = [...newItems, ...response.data]; // Új elemek hozzáadása
                    }
                    dispatch({ type: 'ITEMS_LOADED', payload: newItems }); // Új elemeket átadni a reducer-nek
                } catch (error) {
                    toast.error(error.response.data.message);
                }
            }
        };
        fetchShopOrderItems();
    }, [shopOrders]);

    if (!shopOrders || shopOrders.length === 0)
        return <NotFound message="Seems like you haven't ordered yet" linkText="Go To Product Page" />

  return (
    shopOrders && <div className={classes.container}>
        <div className={classes.content}>
            <h1>Your Orders:</h1>
            <CollapsibleTable shopOrders={shopOrders} shopOrderItems={items} />
        </div>
    </div>
  );
}
