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
  Skeleton,
} from "@mui/material";
import { useAppDispatch } from "../app/hooks";
import { useGetOrderQuery } from "../app/api/orderApi";
import { OrderItem } from "../types/order/Order";
import PaypalPayment from "../components/PaypalPayment";
import { selectOrder, setOrder } from "../features/orderSlice";
import { useSelector } from "react-redux";
import ProductImageLink from "../components/ProductImageLink";
import PaymentConfirmation from "../components/PaymentConfirmation";

interface OrderProps {
  onApproveCallback?: () => void;
  orderId?: number | undefined;
}

const Order: React.FC<OrderProps> = ({ onApproveCallback, orderId }) => {
  const [isPaid, setIsPaid] = useState(false);
  const dispatch = useAppDispatch();
  const { id } = useParams() as { id: string };

  const {
    data: orderData,
    isLoading,
    isSuccess,
  } = useGetOrderQuery(orderId?.toString() || id);
  const { order } = useSelector(selectOrder);

  const handleApprove = () => {
    setIsPaid(true);
    if (onApproveCallback) onApproveCallback();
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
                {order.orderItems.map((orderItem: OrderItem) => (
                  <ListItem
                    key={orderItem.orderItemId}
                    sx={{ py: 1, px: 0 }}
                    divider
                  >
                    <Box sx={{ width: "unset" }}>
                      <ProductImageLink productId={orderItem.product.productId} width="5rem" />
                    </Box>
                    <ListItemText
                      primary={orderItem.product.name}
                      secondary={orderItem.product.description}
                    />
                    <Typography variant="body2" whiteSpace={"nowrap"}>
                      {orderItem.quantity} x €{orderItem.product.price}
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
              {!isPaid && order.orderStatus !== "PAID" ? (
                <Box sx={{ mt: 3 }}>
                  <Typography variant="h6" color="textPrimary" gutterBottom>
                    Payment
                  </Typography>
                  <PaypalPayment onApproveCallback={handleApprove} orderId={order.orderId} />
                </Box>
              ) : (
                <Box sx={{ mt: 3 }}>
                  <PaymentConfirmation />
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
