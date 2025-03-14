import React from 'react'

import { Swiper, SwiperSlide } from "swiper/react";

import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/free-mode";

import classes from './newsSlider.module.css'

import { FreeMode } from "swiper/modules";

import { RxArrowTopRight } from "react-icons/rx";

export default function NewsSlider({ news }) {
  return (
      news.length > 0 ? (
      <div className={classes.container}>
        <Swiper
          spaceBetween={50}
          slidesPerView={news.length < 3 ? news.length : 3}
          freeMode={true}
          centeredSlides={true} 
          modules={[FreeMode]}
          className={classes.swiper_container}
        >
          {news.map((item, index) => (
            <SwiperSlide key={index}>
              <div className={classes.slide}>
                <div className={classes.content}>
                  <h1 className={classes.title}>{item.title}</h1>
                  <p>{item.content}</p>
                </div>
                <RxArrowTopRight className={classes.arrow} />
              </div>
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
      ) : (
          <></>
      )
  );
}
