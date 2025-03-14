import React, { useEffect, useState } from 'react'
import { toast } from 'react-toastify';

import { useAuth } from '../../hooks/useAuth';
import { useCart } from '../../hooks/useCart';

import classes from './paymentPage.module.css';
import Title from '../../components/Title/Title';
import OrderItemsList from '../../components/OrderItemsList/OrderItemsList';
import PaypalButtons from '../../components/PaypalButtons/PaypalButtons';

import * as orderService from '../../api/orderService';

export function PaymentPage() {
    const { user } = useAuth();
    const { cart } = useCart();

    const [shopOrder, setShopOrder] = useState();
    const [shopOrderItems, setShopOrderItems] = useState();

    useEffect(() => {
        const fetchShopOrder = async () => {
            try {
                const responseShopOrder = await orderService.getPendingShopOrderByUserId(user.id);
                setShopOrder(responseShopOrder.data);
            } catch (error) {
                toast.error(error.response.data.message);
                return;
            }
        };
    
        fetchShopOrder();
    }, []);

    useEffect(() => {
      if (shopOrder) {
          const fetchShopOrderItems = async () => {
              try {
                  const responseShopOrderItems = await orderService.getShopOrderItemsByShopOrderId(shopOrder.id);
                  setShopOrderItems(responseShopOrderItems.data);
              } catch (error) {
                  toast.error(error.response.data.message);
                  return;
              }
          };

          fetchShopOrderItems();
      }
    }, [shopOrder]);

    if (!shopOrder || !shopOrderItems) return;

    return (
        <div className={classes.container}>
            <div className={classes.content}>
                <Title title="Order Form" fontSize="1.6rem" />
                <div className={classes.summary}>
                    <div>
                        <h3>Customer Name:</h3>
                        <span>{`${shopOrder.firstName} ${shopOrder.lastName}`}</span>
                    </div>
                    <div>
                        <h3>Customer Contacts:</h3>
                        <span>+{shopOrder.phoneNumber}<br />{user.email}</span>
                    </div>
                    <div>
                    <h3>Shipping Address:</h3>
                        <span>&nbsp;{`${shopOrder.shippingAddress.country.name}, ${shopOrder.shippingAddress.region}, ${shopOrder.shippingAddress.postalCode}, ${shopOrder.shippingAddress.city}`}
                        <br />
                        &nbsp;{`${shopOrder.shippingAddress.addressLine1}, ${shopOrder.shippingAddress.addressLine2 ? shopOrder.shippingAddress.addressLine2 + ',' : ''} ${shopOrder.shippingAddress.streetNumber}, ${shopOrder.shippingAddress.unitNumber ? shopOrder.shippingAddress.unitNumber : ''}`}</span>
                    </div>
                    <div className={classes.order}>
                        <OrderItemsList order={shopOrderItems} totalPrice={shopOrder.orderTotal} />
                    </div>
                    <div className={classes.buttons_container}>
                        <div className={classes.buttons}>
                            <PaypalButtons order={shopOrder} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

