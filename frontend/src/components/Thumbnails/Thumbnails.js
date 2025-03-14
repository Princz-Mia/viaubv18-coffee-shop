import React from 'react';
import { Link } from 'react-router-dom';
import { Rating } from "@mui/material";
import { makeStyles } from '@mui/styles';

import classes from './thumbnails.module.css';
import Price from '../Price/Price';

export default function Thumbnails({ products, averageRatings }) {
    const useStyles = makeStyles({
        customRating: {
            '& .MuiRating-iconEmpty': {
                color: '#eee',
            },
        },
    });

    const styles = useStyles();

    return (
        <ul className={classes.list}>
            {products.map(product => {
                if (!product.isRemoved) { // Itt használjuk a negációs operátort
                    return (
                        <li key={product.id}>
                            <Link to={`/products/${product.name}`}>
                                <img
                                    className={classes.image}
                                    src={product.productImage ? `${product.productImage}` : "/no_image_placeholder.svg"}
                                    alt={product.name}
                                />
                                <div className={classes.content}>
                                    <div className={classes.name}>{product.name}</div>
                                    <div className={classes.stars}>
                                        <Rating
                                            className={styles.customRating}
                                            value={averageRatings[product.id]}
                                            precision={0.5}
                                            readOnly
                                            size="medium"
                                        />
                                    </div>
                                    <div className={classes.price}>
                                        <Price price={product.price} />
                                    </div>
                                </div>
                            </Link>
                        </li>
                    );
                }
                return null; // Ha a termék eltávolítva van, visszatérünk null-lal
            })}
        </ul>
    );
    
}
