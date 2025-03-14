import React, { useEffect, useReducer, useState } from 'react';
import classes from './productPage.module.css';
import { useNavigate, useParams } from 'react-router-dom';
import { useCart } from '../../hooks/useCart';
import { getProductByName } from '../../api/productService';
import Price from '../../components/Price/Price';
import NotFound from '../../components/NotFound/NotFound';
import ProductReview from '../../components/ProductReview/ProductReview'
import { toast } from 'react-toastify';
import { Rating } from "@mui/material";

import { useAuth } from '../../hooks/useAuth';
import * as productReviewService from '../../api/productReviewService';
import { makeStyles } from '@mui/styles';

const initialState = { reviews: [] };

const reducer = (state, action) => {
    switch (action.type) {
        case 'REVIEWS_LOADED':
            return { ...state, reviews: [...state.reviews, ...action.payload] };
        default:
             return state;
    }
};

export function ProductPage() {
    const [product, setProduct] = useState({});
    const [ratingValue, setRatingValue] = useState();

    const [state, dispatch] = useReducer(reducer, initialState);
    const { reviews } = state;
    
    const { user } = useAuth();

    const { name } = useParams();
    const { addToCart } = useCart();
    const navigate = useNavigate();

    const useStyles = makeStyles({
        customRating: {
            '& .MuiRating-iconEmpty': {
                color: '#eee',
            },
        },
    });
    const styles = useStyles();

    const handleAddToCart = () => {
        if (user) {
            addToCart(product);
            navigate('/cart');
        } else {
            toast.error("Please login before you start filling your cart");
        }
    };

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await getProductByName(name);
                setProduct(response.data);
            } catch(error) {
                toast.error(error.response.data.message)
                navigate("/products")
            }
        };

        fetchProduct();
    }, [name]);

    useEffect(() => {
        fetchProductReviews();
      }, [product]);

    const handleReviewAdded = () => {
        fetchProductReviews();
    };

    const fetchProductReviews = async () => {
        if (product && product.id) {
            try {
                const response = await productReviewService.getReviewsByProductId(product.id);
                dispatch({ type: 'REVIEWS_LOADED', payload: response.data });

                let totalRating = response.data.reduce((acc, value) => acc + Number(value.ratingValue), 0);
                let averageRating = totalRating / response.data.length;
                averageRating = Number(averageRating > 0 ? averageRating.toFixed(2) : averageRating);
                setRatingValue(averageRating);
            } catch (error) {
                toast.error(error.response.data.message);
            }
        }
    };
      
    return (
        <>
            {!product && !reviews ? (
                <NotFound message={"Product Not Found!"} linkText={"Reset Search"} />
            ) : (
                <div className={classes.page}> 
                    <div className={classes.container}>
                        <img className={classes.image} src={product.imagePath ? `${product.imagePath}` : "/no_image_placeholder.svg"} alt={product.name} />

                        <div className={classes.details}>
                            <div className={classes.header}>
                                <span className={classes.name}>{product.name}</span>
                            </div>
                            <div className={classes.rating}>
                                <Rating className={styles.customRating} value={Number(ratingValue)} precision={0.5} readOnly size="large" />
                            </div>
                            <div className={classes.description}>
                                <p>{product.description}</p>
                            </div>
                            <div className={classes.price}>
                                <Price price={product.price} />
                            </div>
                            <div className={classes.quantity}>
                                Quantity: {product.qtyInStock}
                            </div>
                            <button onClick={handleAddToCart}>Add to cart</button>
                        </div>
                    </div>
                    <div className={classes.review_section}>
                        <ProductReview productId={product.id} productReviews={reviews} onReviewAdded={handleReviewAdded} />
                    </div>
                </div>
            )}
        </>
    );
}
