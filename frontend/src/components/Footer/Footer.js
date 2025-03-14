import React from 'react'
import classes from './footer.module.css'
import { Link, useNavigate } from 'react-router-dom'

export default function Footer(props) {
    const navigate = useNavigate();

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
    <footer className={classes.footer}>
        <div className={classes.links}>
        <a href="#" onClick={(e) => handleNavigationClick(e, props.homeRef, "/")}>Home</a>
        <a href="#" onClick={(e) => handleNavigationClick(e, props.aboutRef, "/")}>About</a>
        <Link to="/products">Products</Link>
        <a href="#" onClick={(e) => handleNavigationClick(e, props.contactRef, "/")}>Contact</a>
        </div>
        <div className={classes.credit}>Created by <Link target={'_blank'} to='https://github.com/Princz-Mia'>Mia Princz</Link></div>
    </footer>
  )
}
