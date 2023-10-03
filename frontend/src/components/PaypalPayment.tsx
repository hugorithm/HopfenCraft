import { BASE_URL } from "../config/constants";
import {
  PayPalScriptProvider,
  PayPalButtons,
} from "@paypal/react-paypal-js";
import { PayPalScriptOptions } from "@paypal/paypal-js/types/script-options";
import { useSelector } from "react-redux";
import { selectOrder } from "../features/orderSlice";

const paypalScriptOptions: PayPalScriptOptions = {
  clientId: import.meta.env.VITE_PAYPAL_CLIENT_ID,
  currency: "EUR"
};

interface PaypalPaymentProps {
  onApproveCallback: () => void;
}

const PaypalPayment: React.FC<PaypalPaymentProps> = ({ onApproveCallback })  => {
  const { order } = useSelector(selectOrder);

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
        orderId: order?.orderId,
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
        orderId: order?.orderId,
      })
    })
      .then((response) => response.json())
      .then(() => {
          onApproveCallback();
      });
  };

  return (
    <PayPalScriptProvider options={paypalScriptOptions}>
      <PayPalButtons createOrder={createOrder} onApprove={onApprove} />
    </PayPalScriptProvider>
  );
}

export default PaypalPayment;