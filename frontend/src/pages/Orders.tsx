import {
  Box,
  Paper,
  Container,
  CssBaseline,
  Fade,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  IconButton,
  Tooltip,
  Button
} from "@mui/material";
import { useAppDispatch } from "../app/hooks";
import { useEffect, useRef } from "react";
import { fetchOrders, resetOrders, resetPage, selectOrders, setOrders } from "../features/ordersSlice";
import { useSelector } from "react-redux";
import { OrderList } from "../types/order/OrderList";
import ShoppingCartSkeleton from "../components/ShoppingCartSkeleton";
import { Order } from "../types/order/Order";
import dateFormater from "../utils/dateFormatter";
import PaymentIcon from '@mui/icons-material/Payment';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import { Link } from "react-router-dom";
import { setOrder } from "../features/orderSlice";


const Orders = () => {
  const dispatch = useAppDispatch();
  const { orders, loading, error, page, last } = useSelector(selectOrders);

  const loadMore = () => {
    if (!last && loading !== 'loading') {
      dispatch(fetchOrders());
    }
  };

  const renderAfterCalled = useRef(false);
  useEffect(() => {
    if (!renderAfterCalled.current) { // This is so that React Strict Mode doesn't cause issues
      if (loading !== "loading") {
        dispatch(resetPage());
        dispatch(resetOrders());
        dispatch(fetchOrders()).then((data) => {
          if (data.payload) {
            const payload = data.payload as OrderList;
            dispatch(setOrders(payload.content));
          } else {
            throw new Error("Failed to fetch data");
          }
        });
      }
    }
    renderAfterCalled.current = true; // This is so that React Strict Mode doesn't cause issues
  }, [dispatch, orders]);

  return (
    <>
      <CssBaseline />
      <Fade in={true} timeout={1000}>
        <Container maxWidth="md">
          <Box
            sx={{
              mt: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Typography component="h1" variant="h2" align="center" color="text.primary" gutterBottom>
              My Orders
            </Typography>
            <TableContainer component={Paper} elevation={3} sx={{ maxWidth: 800, margin: '0 auto' }}>
              <Table aria-label="orders-table">
                <TableHead>
                  <TableRow>
                    <TableCell>Order ID</TableCell>
                    <TableCell align="center">Date</TableCell>
                    <TableCell align="center">Total</TableCell>
                    <TableCell align="center">Status</TableCell>
                    <TableCell></TableCell>
                    <TableCell></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {loading === 'loading' ? (
                    <>
                      <ShoppingCartSkeleton />
                    </>
                  ) : orders.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={5} align="center">
                        There are no orders
                      </TableCell>
                    </TableRow>
                  ) : (
                    orders.map((order: Order) => (
                      <TableRow key={order.orderId}>
                        <TableCell component="th" scope="row">
                          #{order.orderId}
                        </TableCell>
                        <TableCell align="center">{dateFormater(order.orderDate)}</TableCell>
                        <TableCell align="center">€ {order.total}</TableCell>
                        <TableCell align="center">{order.orderStatus}</TableCell>


                        {order.orderStatus !== 'PAID' ? (
                          <>
                            <TableCell align="center">
                              <Tooltip title="Payement pending">
                                <PaymentIcon />
                              </Tooltip>
                            </TableCell>
                            <TableCell>
                              <Button variant="outlined" component={Link} to={`/order/${order.orderId}`}>
                                Go to payment
                              </Button>
                            </TableCell>
                          </>
                        ) : (
                          <>
                            <TableCell align="center">
                              <Tooltip title="Paid">
                                <CheckCircleOutlineIcon />
                              </Tooltip>
                            </TableCell>
                            <TableCell>
                            </TableCell>
                          </>
                        )}
                      </TableRow>
                    )
                    ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        </Container>
      </Fade>
    </>
  )
}

export default Orders;