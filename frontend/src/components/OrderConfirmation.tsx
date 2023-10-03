import { Typography } from "@mui/material"
import React from "react"
import { useSelector } from "react-redux";
import { selectOrder } from "../features/orderSlice";

const OrderConfirmation = () => {
    const { order } = useSelector(selectOrder);

    return (
        order ? (
        <React.Fragment>
          <Typography variant="h5" gutterBottom>
            Thank you for your order!
          </Typography>
          <Typography variant="subtitle1">
            Your order number is #{order.orderId} We have emailed your order
            confirmation. Please proceed to the payment.
          </Typography>
        </React.Fragment>
        ) : (
            <React.Fragment>
            <Typography variant="h5" gutterBottom>
              There was a problem with your order
            </Typography>
            <Typography variant="subtitle1">
                There was a problem with your order
            </Typography>
          </React.Fragment>
        )
    )
}

export default OrderConfirmation;