import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import {
  Box,
  Container,
  CssBaseline,
  Fade,
  List,
  ListItem,
  ListItemText,
  Typography,
  Paper,
  Divider,
  Button,
  Skeleton,
} from "@mui/material";
import { useAppDispatch } from "../app/hooks";
import { useGetOrderQuery } from "../app/api/orderApi";
import { CartItem } from "../types/order/Order";
import PaypalPayment from "../components/PaypalPayment";
import { selectOrder, setOrder } from "../features/orderSlice";
import { useSelector } from "react-redux";

const Order = () => {
  const [isPaid, setIsPaid] = useState(false);
  const dispatch = useAppDispatch();
  const { id } = useParams() as { id: string };
  const {
    data: orderData,
    isLoading,
    isSuccess,
    isError,
    error,
  } = useGetOrderQuery(id);
  const { order } = useSelector(selectOrder);

  const handleApprove = () => {
    setIsPaid(true);
  };

  useEffect(() => {
    if (isSuccess && orderData) {
      dispatch(setOrder(orderData));
    }
  }, [isSuccess, orderData]);

  return (
    <>
      <CssBaseline />
      {isLoading || order === null ? (
        <Fade in={true} timeout={1000}>
          <Container maxWidth="sm">
            <Box
              sx={{
                mt: 8,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <Skeleton variant="rectangular" width={300} height={100} />
            </Box>
          </Container>
        </Fade>
      ) : (
        <Fade in={true} timeout={1000}>
          <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 3, mt: 8 }}>
              <Typography
                component="h1"
                variant="h4"
                align="center"
                color="textPrimary"
                gutterBottom
              >
                Order # {order.orderId}
              </Typography>
              <Divider />
              <List disablePadding>
                {order.cartItems.map((cartItem: CartItem) => (
                  <ListItem
                    key={cartItem.cartItemId}
                    sx={{ py: 1, px: 0 }}
                    divider
                  >
                    <ListItemText
                      primary={cartItem.product.name}
                      secondary={cartItem.product.description}
                    />
                    <Typography variant="body2">
                      {cartItem.quantity} x €{cartItem.product.price}
                    </Typography>
                  </ListItem>
                ))}
              </List>
              <Divider />
              <Box sx={{ display: "flex", justifyContent: "flex-end" }}>
                <Typography
                  variant="h6"
                  color="textSecondary"
                  sx={{ mt: 2, ml: 2 }}
                >
                  Total: €{order.total}
                </Typography>
              </Box>
              <Divider sx={{ mt: 2 }} />
              {!isPaid && (
                <Box sx={{ mt: 3 }}>
                  <Typography variant="h6" color="textPrimary" gutterBottom>
                    Payment
                  </Typography>
                  <PaypalPayment onApproveCallback={handleApprove} />
                </Box>
              )}
            </Paper>
          </Container>
        </Fade>
      )}
    </>
  );
};

export default Order;
