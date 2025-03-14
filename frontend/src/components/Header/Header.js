import React from 'react';
import classes from './header.module.css';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../../hooks/useCart';
import { useAuth } from '../../hooks/useAuth';

export default function Header(props) {
    const navigate = useNavigate();
    const { user, logout } = useAuth();
    const { cart } = useCart();


    const logoutFunc = () => {
        logout();
        navigate("/login");
    }

    const handleScroll = (ref) => {
        window.scrollTo({
            top: ref.current?.offsetTop,
            behavior: 'smooth'
        });
    }

    const handleNavigationClick = (e, ref, path) => {
        e.preventDefault();
        if (ref.current) {
            handleScroll(ref);
        } else {
            navigate(path);
            setTimeout(() => {
                handleScroll(ref);
            }, 100);
        }
    };

    return (
        <header className={classes.header}>
            <div className={classes.container}>

                <Link to="/" className={classes.logo}>
                    <img src={'images/logo.png'} alt="Spring Coffee Shop" />
                </Link>

                <nav className={classes.nav}>
                    <ul>
                        <li><a href="#" onClick={(e) => handleNavigationClick(e, props.homeRef, "/")}>Home</a></li>
                        <li><a href="#" onClick={(e) => handleNavigationClick(e, props.aboutRef, "/")}>About</a></li>
                        <Link to="/products">Products</Link>
                        <li><a href="#" onClick={(e) => handleNavigationClick(e, props.contactRef, "/")}>Contact</a></li>
                    </ul>
                </nav>

                <nav className={classes.nav}>
                    <ul>
                        {user ? (
                            <li className={classes.menu_container}>
                                <Link to="#" onClick={e => e.preventDefault()}>{user.firstName} {user.lastName}</Link>
                                <div className={classes.menu}>
                                    <Link to="/profile">Profile</Link>
                                    <Link to="/track/orders">Orders</Link>
                                    { user.role === 'ADMIN' ? (
                                        <Link to="/managment">Managment</Link>
                                    ) : ( 
                                        <></>
                                    )}
                                    <a onClick={logoutFunc}>Logout</a>
                                </div>
                            </li>
                            ) : (
                            <Link to="/login">Login</Link>
                        )}
                        
                        { user ? (
                            <li>
                                <Link to="/cart">
                                    Cart&nbsp;
                                    {cart.totalCount > 0 && <span className={classes.cart_count}>{cart.totalCount}</span>}
                                </Link>
                            </li>
                        ) : (
                            <>{/* Empty */}</>
                        )}
                    </ul>
                </nav>
            </div>
        </header>
    );
}
