import { BASE_URL } from "../config/constants";
import {
  PayPalScriptProvider,
  PayPalButtons,
  usePayPalScriptReducer,
} from "@paypal/react-paypal-js";
import { PayPalScriptOptions } from "@paypal/paypal-js/types/script-options";
import { Box, CircularProgress } from "@mui/material";

const paypalScriptOptions: PayPalScriptOptions = {
  clientId: import.meta.env.VITE_PAYPAL_CLIENT_ID,
  currency: "EUR"
};

interface PaypalPaymentProps {
  onApproveCallback: () => void;
  orderId: number;
}

interface ButtonWrapperProps {
  createOrder: () => Promise<string>;
  onApprove: (data: { orderID: string }) => Promise<void>;
}

const ButtonWrapper: React.FC<ButtonWrapperProps> = ({ createOrder, onApprove }) => {
  const [{ isPending }] = usePayPalScriptReducer();
  return (
    isPending ? <Box sx={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
    }}><CircularProgress /></Box>
    : <PayPalButtons createOrder={createOrder} onApprove={onApprove}  />
  )
}

const PaypalPayment: React.FC<PaypalPaymentProps> = ({ onApproveCallback, orderId }) => {
  const createOrder = () => {
    const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
    const headers = new Headers({
      'Authorization': `Bearer ${localJwt}`,
      'Content-Type': 'application/json',
    });
    // Order is created on the server and the order id is returned
    return fetch(`${BASE_URL}/paypal/api/orders/create-order`, {
      method: "POST",
      headers,
      body: JSON.stringify({
        orderId: orderId,
      }),
    })
      .then((response) => response.json())
      .then((order) => order.id);
  };

  const onApprove = (data: { orderID: string; }) => {
    const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
    const headers = new Headers({
      'Authorization': `Bearer ${localJwt}`,
      'Content-Type': 'application/json',
    });

    return fetch(`${BASE_URL}/paypal/api/orders/${data.orderID}/capture`, {
      method: "POST",
      headers,
      body: JSON.stringify({
        orderId: orderId,
      })
    })
      .then((response) => response.json())
      .then(() => {
        onApproveCallback();
      });
  };

  return (
    <PayPalScriptProvider options={paypalScriptOptions}>
      <ButtonWrapper createOrder={createOrder} onApprove={onApprove} />
    </PayPalScriptProvider>
  );
}

export default PaypalPayment;