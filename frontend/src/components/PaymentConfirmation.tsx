import { Typography } from "@mui/material";
import React from "react";

const PaymentConfirmation = () => {
    return (
        <React.Fragment>
          <Typography variant="h5" gutterBottom>
            Thank you!
          </Typography>
          <Typography variant="subtitle1">
            Thank you for shopping with us! We will email you as soon a the package is shipped.
          </Typography>
          <Typography variant="subtitle1" mt={2} fontWeight={500}>
            Cheers! 🍻
          </Typography>
        </React.Fragment>
       
    )
}

export default PaymentConfirmation;