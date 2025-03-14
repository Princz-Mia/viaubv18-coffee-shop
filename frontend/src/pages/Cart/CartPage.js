import React, { useEffect } from 'react';
import { useCart } from '../../hooks/useCart';
import { Link, useNavigate } from 'react-router-dom';
import classes from './cartPage.module.css';
import Title from '../../components/Title/Title';
import Price from '../../components/Price/Price';
import NotFound from '../../components/NotFound/NotFound';
import { useAuth } from '../../hooks/useAuth';
import { toast } from 'react-toastify';

export function CartPage() {
  const { fetchPendingOrdersOfUser } = useAuth();
  const { cart, removeFromCart, changeQuantity } = useCart();

  const navigate = useNavigate();

  useEffect(() => {
    
  }, [cart]);

  const checkoutLinkClickHandler = async () => {
    try {
        const hasPendingOrder = await fetchPendingOrdersOfUser();
        if (hasPendingOrder.data === false) {
            navigate('/checkout');
        } else {
            toast.warning("You have already an Order that is in Pending state");
            navigate('/track/orders');
        }
    } catch (error) {
        toast.error(error.message);
    }
};


  return (
    <div className={classes.page}>
      <Title title="Cart Page" padding="3rem" />
      {cart.products.length === 0 ? (
        <NotFound message={"Cart Page Is Empty"} lintText={"Go Back To Home Page"} />
      ) : (
        <div className={classes.container}>
          <ul className={classes.list}>
            {cart.products.map(item => <li key={item.product.id}>
              <div>
                <img src={item.product.productImage ? `${item.product.productImage}` : "/no_image_placeholder.svg"} alt={item.product.name} />
              </div>
              <div>
                <Link to={`/product/${item.product.id}`}>{item.product.name}</Link>
              </div>
              <div>
                <select value={item.qty} onChange={e => { changeQuantity(item, Number(e.target.value)) }}>
                  {Array.from({ length: item.product.qtyInStock }, (_, index) => (
                    <option key={index + 1} value={index + 1}>{index + 1}</option>
                  ))}
                </select>
              </div>
              <div>
                <Price price={item.qty * item.product.price} />
              </div>
              <div>
                <button className={classes.remove_button} onClick={() => removeFromCart(item.product.id)}>Remove</button>
              </div>
            </li>)}
          </ul>
          <div className={classes.checkout}>
            <div>
              <div className={classes.products_count}>{cart.totalCount}</div>
              <div className={classes.total_price}>
                <Price price={cart.totalPrice} />
              </div>
            </div>
            <Link onClick={checkoutLinkClickHandler}>Proceed To Checkout</Link>
          </div>
        </div>
        )}
    </div>
  );
};
