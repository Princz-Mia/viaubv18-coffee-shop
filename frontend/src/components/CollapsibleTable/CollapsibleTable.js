import * as React from 'react';
import { Link } from 'react-router-dom';

import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';


function createData(referenceId, orderDate, totalPrice, shopOrderStatus, orderItems) {
  const formattedOrderItems = orderItems.map(item => ({
    productId: item.product.id,
    productName: item.product.name,
    quantity: item.qty,
    price: item.price
  }));

  return {
    referenceId,
    orderDate,
    totalPrice,
    shopOrderStatus,
    orderItems: formattedOrderItems
  };
}


function Row(props) {
  const { row } = props;
  const [open, setOpen] = React.useState(false);

  return (
    <React.Fragment>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell align="right">{row.referenceId}</TableCell>
        <TableCell align="right">{row.orderDate}</TableCell>
        <TableCell align="right">{row.totalPrice}</TableCell>
        <TableCell align="right">          
          {row.shopOrderStatus === 'PENDING' ? (
              <Link to="/payment">
                PENDING
              </Link>
            ) : (
              row.shopOrderStatus
          )}
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <Typography variant="h6" gutterBottom component="div">
                Details
              </Typography>
              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell>Reference Id</TableCell>
                    <TableCell>Name</TableCell>
                    <TableCell align="right">Quantity</TableCell>
                    <TableCell align="right">Price ($)</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row.orderItems.map((orderItemsRow) => (
                    <TableRow key={orderItemsRow.productId}>
                      <TableCell component="th" scope="row">
                        {orderItemsRow.productId}
                      </TableCell>
                      <TableCell>{orderItemsRow.productName}</TableCell>
                      <TableCell align="right">{orderItemsRow.quantity}</TableCell>
                      <TableCell align="right">
                        {orderItemsRow.price.toFixed(2)}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </React.Fragment>
  );
}

Row.propTypes = {
  row: PropTypes.shape({
    referenceId: PropTypes.number.isRequired,
    orderDate: PropTypes.string.isRequired,
    totalPrice: PropTypes.number.isRequired,
    shopOrderStatus: PropTypes.string.isRequired,
    orderItems: PropTypes.arrayOf(
      PropTypes.shape({
        productId: PropTypes.number.isRequired,
        productName: PropTypes.string.isRequired,
        quantity: PropTypes.number.isRequired,
        price: PropTypes.number.isRequired,
      }),
    ).isRequired,
  }).isRequired,
};


export default function CollapsibleTable({ shopOrders, shopOrderItems }) {
  const [rows, setRows] = React.useState([]);
  
  React.useEffect(() => {
    if (shopOrderItems) {
        const newRows = shopOrders.map((shopOrder) => {
            const orderItemsForOrder = shopOrderItems.filter((item) => item.shopOrder.id === shopOrder.id);
            const date = new Date(shopOrder.orderDate[0], shopOrder.orderDate[1] - 1, shopOrder.orderDate[2], shopOrder.orderDate[3], shopOrder.orderDate[4], shopOrder.orderDate[5]);
            const formattedDate = `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`
            return createData(
                shopOrder.id,
                formattedDate,
                shopOrder.orderTotal.toFixed(2),
                shopOrder.shopOrderStatus.name,
                orderItemsForOrder
            );
        });
        setRows(newRows);
    }
  }, [shopOrders, shopOrderItems]);

    return (
        <TableContainer component={Paper}>
        <Table aria-label="collapsible table">
            <TableHead>
            <TableRow>
                <TableCell />
                <TableCell align="right">Reference Id</TableCell>
                <TableCell align="right">Date&nbsp;</TableCell>
                <TableCell align="right">Total price ($)&nbsp;</TableCell>
                <TableCell align="right">Status&nbsp;</TableCell>
            </TableRow>
            </TableHead>
            <TableBody>
            {rows.map((row) => (
                <Row key={row.referenceId} row={row} />
            ))}
            </TableBody>
        </Table>
        </TableContainer>
  );
}