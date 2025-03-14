import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { PayPalButtons, PayPalScriptProvider, usePayPalScriptReducer } from '@paypal/react-paypal-js';
import { toast } from 'react-toastify';

import { useCart } from '../../hooks/useCart';

import * as orderService from '../../api/orderService';

export default function PaypalButtons({ order }) {
    return (
        <PayPalScriptProvider
        options={
          {
            clientId:'AYtOiz820AMXi85yxs_q8PDVK85p4MejyNzXj1jVe2EX9FEeloaMIXrSqDBuz-wOl-Yk_fOLrzmW14Qh'
          }
        }
        >
            <Buttons order={order} />
        </PayPalScriptProvider>
    );
}

function Buttons({ order }) {
    const { clearCart } = useCart();
    const navigate = useNavigate();
    const [{ isPending }] = usePayPalScriptReducer();
    //const {showLoading, hideLoading} = useLoading();

    useEffect(() => {
        //isPending ? showLoading() : hideLoading();
    });

    const createPayPalOrder = (data, actions) => {
        const totalPriceFormatted = order.orderTotal.toFixed(2);
        return actions.order.create({
            purchase_units: [
                {
                    amount: 
                        {
                            currency_code: 'USD',
                            value: totalPriceFormatted,
                        },
                },
            ],
        });
    };

    const onApprove = (data, actions) => {
        try {
            const payment = actions.order.capture();
            const paymentRequest = {
                shopOrderId: order.id,
                paymentId: payment.id,
            };
            const payedOrder = orderService.payShopOrder(paymentRequest);
            clearCart();
            toast.success('Payment Saved Successfully', 'Success');
            navigate('/track/orders');
        } catch (error) {
            toast.error('Payment Save Failed', 'Error');
        }
    };

    const onError = (error) => {
        toast.error(`Payment Failed`, 'Error');
    };

    return (
        <PayPalButtons
            createOrder={createPayPalOrder}
            onApprove={onApprove}
            onError={onError}
        />
    );
}