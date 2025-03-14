import React, { useEffect, useReducer, useState } from 'react'
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom'

import * as productService from '../../api/productService'
import { toast } from 'react-toastify';

import NotFound from '../../components/NotFound/NotFound';
import Search from '../../components/Search/Search';
import Price from '../../components/Price/Price';
import Title from '../../components/Title/Title';
import Pagination from '@mui/material/Pagination';
import Button from '../../components/Button/Button';
import Popup from '../../components/Popup/Popup';
import ProductForm from '../../components/ProductForm/ProductForm';
import Controls from '../../components/Controls/Controls';

import EditOutlinedIcon from '@material-ui/icons/EditOutlined';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import VisibilityIcon from '@mui/icons-material/Visibility';

import classes from './managmentProductsPage.module.css'

const initialState = { page: { data: { content: [], totalPages: 1 } } };

const reducer = (state, action) => {
    switch (action.type) {
        case 'PAGE_LOADED':
            return { ...state, page: action.payload };
        default:
             return state;
    }
};

export function ManagmentProductsPage() {
  const [state, dispatch] = useReducer(reducer, initialState);
  const { page } = state;

  const { searchTerm } = useParams();

  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const pageNumber = queryParams.get('page') || 1;
  const productsNumber = queryParams.get('products') || 8;

  const [openPopup, setOpenPopup] = useState(false);
  const [recordForEdit, setRecordForEdit] = useState(null)

  useEffect(() => {
    const loadPage = searchTerm ? productService.getBySearchTerm(searchTerm, pageNumber - 1, productsNumber) : productService.getProductsPage(pageNumber - 1, productsNumber);
    loadPage
        .then(async page => {
            dispatch({ type: 'PAGE_LOADED', payload: page });
        })
        .catch(error => {
            toast.error(error.response.data.message)
            dispatch({ type: 'PAGE_LOADED', payload: { data: { content: [], totalPages: 1 } } });
        });
}, [pageNumber, productsNumber, searchTerm]);

  const handlePageChange = (event, currentPage) => {
      event.preventDefault();
      navigate(`?page=${currentPage}&products=${productsNumber}`);
  };

  const openInPopup = item => {
      setRecordForEdit(item)
      setOpenPopup(true)
  };

  const addOrEdit = async (product, resetForm) => {
    try {
        const productRequest = {
            id: product.id,
            name: product.name,
            qtyInStock: Number(product.qtyInStock),
            price: parseFloat(product.price),
            description: product.description,
            categoryId: product.category.id,
            productImage: product.productImage,
        };

        if (!product.id) {
            await productService.createNewProduct(productRequest);
        } else {
            await productService.updateProduct(productRequest);
        }

        toast.success("Product saved successfully.");
        resetForm();
        setRecordForEdit(null);
        setOpenPopup(false);
    } catch (error) {
        toast.error("Failed to save product.");
    }
};


  const ProductsNotFound = () => {
      if (page.data && page.data.content.length > 0) return;

      return searchTerm ? (
        <NotFound linkRoute="/managment/products" linkText="Show All" />
      ) : (
        <NotFound linkRoute="/managment" linkText="Back to Managment Dashboard!" />
      );
  };

  const removeProduct = async (product) => {
      const confirmed = window.confirm(`Delete Product ${product.name}?`);
      if (!confirmed) return;

      await productService.deleteProductById(product.id);
      toast.success(`"${product.name}" Has Been Removed From The Page!`);
  };

  const reAddProduct = async (product) => {
    const confirmed = window.confirm(`Restock Product ${product.name}?`);
    if (!confirmed) return;

    await productService.reStockProductById(product.id);
    toast.success(`"${product.name}" Has Been restocked From The Page!`);
};

  return (
      <div className={classes.container}>
        <div className={classes.list}>
          <Title title="Manage Products" margin="1rem auto" />
          <Search
            searchRoute="/managment/products"
            defaultRoute="/managment/products"
            placeholder="Search Products"
          />
          <Button className={classes.add_product} text="Add Product" onClick={() => setOpenPopup(true)} />
          <ProductsNotFound />
          {page.data.content &&
            page.data.content.map(product => (
              <div key={product.id} className={classes.list_item}>
                <img src={product.productImage ? `${product.productImage}` : "/no_image_placeholder.svg"} alt={product.name} />
                <Link to={'/products/' + product.name}>{product.name}</Link>
                <Price price={product.price} />
                <div className={classes.actions}>
                    <Controls.ActionButton
                        color="primary"
                        onClick={() => { openInPopup(product) }}>
                            <EditOutlinedIcon fontSize="small" />
                    </Controls.ActionButton>
                    { product.isRemoved ? (
                        <Controls.ActionButton
                            color="primary"
                            onClick={() => { reAddProduct(product) }}>
                            <VisibilityIcon fontSize="small" />
                        </Controls.ActionButton>
                    ) : (
                        <Controls.ActionButton
                            color="secondary"
                            onClick={() => { removeProduct(product) }}>
                            <VisibilityOffIcon fontSize="small" />
                        </Controls.ActionButton>
                    )}
                </div>
              </div>
          ))}
          <div>
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
        </div>
        <Popup
          title="Product Details"
          openPopup={openPopup}
          setOpenPopup={setOpenPopup}
        >
            <ProductForm
                recordForEdit={recordForEdit}
                addOrEdit={addOrEdit}
                />
        </Popup>
      </div>
  );
}