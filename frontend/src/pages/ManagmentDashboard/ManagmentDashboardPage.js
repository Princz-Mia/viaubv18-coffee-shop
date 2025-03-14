import React from 'react';
import classes from './managmentDashboardPage.module.css';
import { Link } from 'react-router-dom';

import { FaUsers , FaParachuteBox , FaCoffee, FaNewspaper  } from 'react-icons/fa'
import { toast } from 'react-toastify';

export function ManagmentDashboardPage() {
  return (
    <div className={classes.container}>
      <div className={classes.menu}>
        {allItems
          .map(item => (
            <Link
              key={item.title}
              to={item.url}
              onClick={item.onClickEvent}
              style={{
                backgroundColor: item.bgColor,
                color: item.color,
              }}
            >
              <item.icon className={classes.icon} />
              <h2>{item.title}</h2>
            </Link>
          ))}
      </div>
    </div>
  );
}

const allItems = [
  /*
  {
    title: 'Users',
    url: '/managment',
    bgColor: '#13131a',
    color: 'white',
    icon: FaUsers,
    onClickEvent: () => toast.warning("Feature is not avaiable at the moment")
  },
  */
  {
    title: 'Orders',
    url: '/managment/orders',
    bgColor: '#13131a',
    color: 'white',
    icon: FaParachuteBox,
  },
  {
    title: 'Products',
    url: '/managment/products',
    bgColor: '#13131a',
    color: 'white',
    icon: FaCoffee,
  },
  {
    title: 'News',
    url: '/managment/news',
    bgColor: '#13131a',
    color: 'white',
    icon: FaNewspaper,
  },
];