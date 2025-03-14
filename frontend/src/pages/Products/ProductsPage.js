import React, { useEffect, useReducer } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import Search from '../../components/Search/Search';
import NotFound from '../../components/NotFound/NotFound';
import Thumbnails from '../../components/Thumbnails/Thumbnails';
import Pagination from '@mui/material/Pagination';
import { toast } from 'react-toastify';
import * as productService from '../../api/productService';
import * as productReviewService from '../../api/productReviewService';
import classes from './productsPage.module.css'

const initialState = { page: [] };

const reducer = (state, action) => {
    switch (action.type) {
        case 'PAGE_LOADED':
            return { ...state, page: action.payload };
        default:
             return state;
    }
};

export function ProductsPage() {
    const [state, dispatch] = useReducer(reducer, initialState);
    const { page } = state;
    const { searchTerm } = useParams();

    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const pageNumber = queryParams.get('page') || 1;
    const productsNumber = queryParams.get('products') || 8;

    useEffect(() => {
        const loadPage = searchTerm ? productService.getBySearchTerm(searchTerm, pageNumber - 1, productsNumber) : productService.getProductsPage(pageNumber - 1, productsNumber);
        loadPage
            .then(async page => {
                const productIds = page.data.content.map(product => product.id);
                const reviewsPromises = productIds.map(productId => productReviewService.getReviewsByProductId(productId));
                const reviewsResponses = await Promise.all(reviewsPromises);
                
                const productsWithReviews = page.data.content.map((product, index) => {
                    return {
                        ...product,
                        reviews: reviewsResponses[index].data
                    };
                });
                
                const pageWithReviews = {
                    ...page,
                    data: {
                        ...page.data,
                        content: productsWithReviews
                    }
                };

                dispatch({ type: 'PAGE_LOADED', payload: pageWithReviews });
            })
            .catch(error => {
                toast.error(error.response.data.message)
                dispatch({ type: 'PAGE_LOADED', payload: null });
            });
    }, [pageNumber, productsNumber, searchTerm]);

    const handlePageChange = (event, currentPage) => {
        event.preventDefault();
        navigate(`/products?page=${currentPage}&products=${productsNumber}`);
    }

    return (
        <div className={classes.container}>
            <div className={classes.page}>
                <Search searchRoute={`/products/search`} defaultRoute={`/products?page=${pageNumber}&products=${productsNumber}`} placeholder={`Search Products`}/>
                {!page || page.length === 0 ? (
                    <NotFound linkText="Reset Search" /> 
                ) : (
                    <div>
                        <Thumbnails products={page.data.content} averageRatings={calculateAverageRatings(page.data.content)} />
                        <div className={classes.pagination_container}>
                        <Pagination
                            onChange={handlePageChange}
                            count={page.data.totalPages}
                            variant="outlined"
                            shape="rounded"
                            sx={{
                                '& .MuiPaginationItem-root': {
                                    color: 'white',
                                    backgroundColor: '#000000cc'
                                },
                                '& .MuiPaginationItem-root:hover': {
                                    color: 'black',
                                    backgroundColor: 'wheat',
                                },
                                '& .MuiPaginationItem-root.Mui-selected': {
                                    color: '111',
                                    backgroundColor: '#00000060'
                                },
                                '& .MuiPaginationItem-root.Mui-selected:hover': {
                                    color: 'black',
                                    backgroundColor: 'wheat',
                                }
                            }}
                        />
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

// Függvény a termékekhez tartozó átlagos vélemények kiszámítására
function calculateAverageRatings(products) {
    const averageRatings = {};
    products.forEach(product => {
        const totalRating = product.reviews.reduce((acc, review) => acc + Number(review.ratingValue), 0);
        const averageRating = totalRating / product.reviews.length;
        averageRatings[product.id] = averageRating;
    });
    return averageRatings;
}
