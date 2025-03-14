import React from 'react';
import { Link } from 'react-router-dom';
import Price from '../Price/Price';
import classes from './orderItemsList.module.css';

export default function OrderItemsList({ order, totalPrice, shippingMethod }) {


return (
    <table className={classes.table}>
        <tbody>
            <tr>
                <td colSpan="5">
                    <h3>Order Items:</h3>
                </td>
            </tr>
            {order && (
                order.products ? (
                    order.products.map(item => (
                        <tr key={item.product.id}>
                            <td>
                                <Link to={`/product/${item.product.name}`}>
                                    <img src={item.product.productImage ? `${item.product.productImage}` : "/no_image_placeholder.svg"} alt={item.product.name}/>
                                </Link>
                            </td> 
                            <td>{item.product.name}</td>
                            <td>
                                <Price price={item.product.price} />
                            </td>
                            <td>{item.qty}</td>
                            <td>
                                <Price price={item.qty * item.product.price} />
                            </td>
                        </tr>
                    ))
                ) : (
                    order.map(item => (
                        <tr key={item.product.id}>
                            <td>
                                <Link to={`/product/${item.product.name}`}>
                                    <img src={item.product.productImage ? `${item.product.productImage}` : "/no_image_placeholder.svg"} alt={item.product.name}/>
                                </Link>
                            </td> 
                            <td>{item.product.name}</td>
                            <td>
                                <Price price={item.product.price} />
                            </td>
                            <td>{item.qty}</td>
                            <td>
                                <Price price={item.qty * item.product.price} />
                            </td>
                        </tr>
                    ))
                )
            )}

            <tr>
                <td colSpan="3"></td>
                <td>
                    <strong>Total:</strong>
                </td>
                <td>
                    <Price price={totalPrice} />
                </td>
            </tr>
        </tbody>
    </table>
  )
}
