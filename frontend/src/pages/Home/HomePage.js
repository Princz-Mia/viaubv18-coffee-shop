import React, { useEffect, useReducer, useState } from 'react'
import classes from './homePage.module.css'
import { Link } from 'react-router-dom'
import About from '../../components/About/About'
import Contact from '../../components/Contact/Contact'
import NewsSlider from '../../components/NewsSlider/NewsSlider'

import * as newsService from '../../api/newsService'

import { toast } from 'react-toastify'

const initialState = { news: [] };

const reducer = (state, action) => {
    switch (action.type) {
        case 'NEWS_LOADED':
            return { ...state, news: action.payload };
        default:
             return state;
    }
};

export function HomePage(props) {
  const [state, dispatch] = useReducer(reducer, initialState);
  const { news } = state;

  useEffect(() => {
    const fetchNews = async () => {
      try {
        const responseNews = await newsService.getAllNews();
        dispatch({ type: 'NEWS_LOADED', payload: responseNews.data });
      } catch (error) {
        toast.error(error.message);
        dispatch({ type: 'NEWS_LOADED', payload: [] });
      }
    };
  
    fetchNews();
  }, []);
  

  return (
    <div>
        <div className={classes.container} ref={props.homeRef}>
            <section className={classes.page}>
                <div className={classes.content}>
                    <h3>Discover the Perfect Brew!</h3>
                    <p>
Welcome to our Coffee Webshop, where your coffee journey begins. Explore a world of rich flavors and aromas with our handpicked selection of premium coffee beans, blends, and brewing accessories. Whether you're a casual coffee drinker or a passionate connoisseur, we have everything you need to elevate your coffee experience. Enjoy the convenience of online shopping and fast delivery right to your doorstep. Sip, savor, and celebrate the art of coffee with us!</p>
                    <Link className={classes.btn} to="/products">Browse our goods</Link>
                </div>
            </section>
        </div>
        { news ? (
           <NewsSlider news={news} />
        ) : (
            <></>
        )}
        <About ref={props.aboutRef} />
        <Contact ref={props.contactRef} />
    </div>
  );
}