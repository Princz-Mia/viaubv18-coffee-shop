import React, { useEffect, useState } from 'react'
import classes from './managmentOrdersPage.module.css'
import { useLocation, useNavigate, useParams } from 'react-router-dom'

import * as orderService from '../../api/orderService'
import { toast } from 'react-toastify';

import NotFound from '../../components/NotFound/NotFound';
import Search from '../../components/Search/Search';
import Title from '../../components/Title/Title';
import Pagination from '@mui/material/Pagination';
import Popup from '../../components/Popup/Popup';
import OrderForm from '../../components/OrderForm/OrderForm';
import Controls from '../../components/Controls/Controls';

import EditOutlinedIcon from '@material-ui/icons/EditOutlined';
import CloseIcon from '@material-ui/icons/Close';

export function ManagmentOrdersPage() {
  const [page, setPage] = useState(null);
  const { searchedOrderId } = useParams();

  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const pageNumber = queryParams.get('page') || 1;
  const ordersNumber = queryParams.get('orders') || 8;

  const [openPopup, setOpenPopup] = useState(false);
  const [recordForEdit, setRecordForEdit] = useState(null)

  useEffect(() => {
      loadPage();
  }, [pageNumber, ordersNumber, searchedOrderId]);

  const loadPage = async () => {
      try {
          const loadPage = searchedOrderId ? await orderService.getByOrderId(searchedOrderId) : await orderService.getPageOfOrders(pageNumber - 1, ordersNumber);
          setPage(loadPage);
      } catch (error) {
          toast.error(error.message);
      }
  };

  // Without this, the page loads with error
  if (!page) {
      return <div>Loading...</div>;
  }

  const handlePageChange = (event, currentPage) => {
      event.preventDefault();
      navigate(`?page=${currentPage}&orders=${ordersNumber}`);
  };

  const openInPopup = item => {
      setRecordForEdit(item)
      setOpenPopup(true)
  };

  const edit = async (order, resetForm) => {
    try {
        const shopOrderRequest = {
            id: order.id,
            userId: order.customer.id,
            addressId: order.shippingAddress.id,
            shippingMethodId: order.shippingMethod.id,
            shopOrderStatusId: order.shopOrderStatus,
            orderDate: order.orderDate,
            orderTotal: order.orderTotal,
            firstName: order.firstName,
            lastName: order.lastName,
            phoneNumber: order.phoneNumber,
        };

        if (order.id) {
            const response = await orderService.updateOrderStatus(shopOrderRequest);
            if (response) {
                toast.success("Order successfully updated.");
            }
        }

        resetForm();
        setRecordForEdit(null);
        setOpenPopup(false);
    } catch (error) {
        toast.error("Failed to update order.");
    }
};


  const OrdersNotFound = () => {
      if (page.data && page.data.content.length > 0) {
        return (
            <div className={classes.list_item}>
                    <p>Customer</p>
                    <p>Phone Number</p>
                    <p>Shipping Method</p>
                    <p>Order Status</p>
                    <p>Order Date</p>
                    <p>Total ($)</p>
                    <p style={{ marginRight: '15rem' }}>Payment Id</p>
            </div>
        );
      }

      return searchedOrderId ? (
        <NotFound linkRoute="/managment/orders" linkText="Show All" />
      ) : (
        <NotFound linkRoute="/managment" linkText="Back to Managment Dashboard!" />
      );
  };

  const deleteOrder = async (order) => {
      const confirmed = window.confirm(`Delete News ${order.title}?`);
      if (!confirmed) return;

      await orderService.deleteById(order.id);
      toast.success(`"${order.title}" Has Been Removed From The Page!`);
  };

  return (
      <div className={classes.container}>
        <div className={classes.list}>
          <Title title="Manage Orders" margin="1rem auto" />
          <Search
            searchRoute="/managment/news/"
            defaultRoute="/managment/news"
            margin="1rem 0"
            placeholder="Search News"
          />
          <OrdersNotFound />
          {page.data.content &&
                page.data.content.map(order => {
                    const date = new Date(order.orderDate[0], order.orderDate[1] - 1, order.orderDate[2], order.orderDate[3], order.orderDate[4], order.orderDate[5]);
                    return (
                    <div key={order.id} className={classes.list_item}>
                        <p>{order.customer.firstName} {order.customer.lastName}</p>
                        <p>+{order.phoneNumber}</p>
                        <p>{order.shippingMethod.name}</p>
                        <p>{order.shopOrderStatus.name}</p>
                        <p>{`${date.toLocaleDateString()}`}</p>
                        <p>$ {order.orderTotal}</p>
                        <p>{order.paymentId ? order.paymentId : 'Not payed yet' }</p>
                        <div className={classes.actions}>
                        <Controls.ActionButton
                            color="primary"
                            onClick={() => { openInPopup(order) }}>
                            <EditOutlinedIcon fontSize="small" />
                        </Controls.ActionButton>
                        <Controls.ActionButton
                            color="secondary"
                            onClick={() => { deleteOrder(order) }}>
                            <CloseIcon fontSize="small" />
                        </Controls.ActionButton>
                        </div>
                    </div>
                    );
                })
            }

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
        <Popup
          title="Orders Details"
          openPopup={openPopup}
          setOpenPopup={setOpenPopup}
        >
            <OrderForm
                recordForEdit={recordForEdit}
                edit={edit}
            />
        </Popup>
      </div>
  );
}