import React, { forwardRef } from 'react';
import classes from './about.module.css';

const About = forwardRef((props, ref) => {
  return (
    <section className={classes.about} id="about" ref={ref}>
      <h1 className={classes.heading}> <span>about</span> us </h1>
      <div className={classes.row}>
        <div className={classes.image}>
          <img src="images/about-img.jpeg" alt="About us" />
        </div>
        <div className={classes.content}>
          <h3>What makes our coffee special?</h3>
          <p>At Coffee Webshop, we believe that every cup of coffee tells a story. Founded with a passion for excellence and a love for the perfect brew, we are dedicated to bringing you the finest coffee from around the world. Our team carefully selects each product to ensure it meets our high standards for quality and flavor.</p>
          <p>From the heart of coffee-growing regions to your home, we strive to deliver an unparalleled coffee experience. We offer a wide range of products, including single-origin beans, unique blends, and top-notch brewing equipment. Our commitment to quality, sustainability, and customer satisfaction drives everything we do.</p>
          <p>
Join us on this flavorful journey and discover the joy of exceptional coffee. Whether you’re a seasoned barista or just starting to explore the world of coffee, we are here to guide you every step of the way. Welcome to Coffee Webshop – where every sip is a delight!</p>
        </div>
      </div>
    </section>
  );
});

export default About;