import * as React from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import AddressForm from '../components/AddressForm';
import Review from '../components/OrderReview';
import { useEffect, useState } from 'react';
import PaypalPayment from '../components/PaypalPayment';
import { useCreateOrderMutation } from '../app/api/orderApi';
import { useAppDispatch } from '../app/hooks';
import { setOrder } from '../features/orderSlice';
import OrderConfirmation from '../components/OrderConfirmation';


const steps = ['Shipping address', 'Review your order', 'Order confirmation', 'Payment details'];

const getStepContent = (step: number) => {
  switch (step) {
    case 0:
      return <AddressForm />;
    case 1:
      return <Review />;
    case 2:
      return <OrderConfirmation />
    case 3:
      return <PaypalPayment />;
    default:
      throw new Error('Unknown step');
  }
}

const Checkout = () => {
  const [createOrder, { data: orderData, isError, isSuccess, isLoading, error }] = useCreateOrderMutation();
  const [activeStep, setActiveStep] = useState(0);
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (isSuccess && orderData) {
      dispatch(setOrder(orderData));
    }
  }, [isSuccess]);


  const handleNext = async () => {
    if (activeStep === 1) {
      createOrder()
        .unwrap()
        .catch((error) => {
          console.error(error);
          return;
        });
    }
    setActiveStep(activeStep + 1);
  };

  const handleBack = () => {
    setActiveStep(activeStep - 1);
  };

  return (
    <React.Fragment>
      <CssBaseline />
      <Container component="main" maxWidth="sm" sx={{ mb: 4 }}>
        <Paper variant="outlined" sx={{ my: { xs: 3, md: 6 }, p: { xs: 2, md: 3 } }}>
          <Typography component="h1" variant="h4" align="center">
            Checkout
          </Typography>
          <Stepper activeStep={activeStep} sx={{ pt: 3, pb: 5 }}>
            {steps.map((label) => (
              <Step key={label}>
                <StepLabel>{label}</StepLabel>
              </Step>
            ))}
          </Stepper>
          {activeStep === steps.length ? (
            <React.Fragment>
              <Typography variant="h5" gutterBottom>
                Thank you for your order.
              </Typography>
              <Typography variant="subtitle1">
                Your order number is #2001539. We have emailed your order
                confirmation, and will send you an update when your order has
                shipped.
              </Typography>
            </React.Fragment>
          ) : (
            <React.Fragment>
              {getStepContent(activeStep)}
              <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                {activeStep !== 0 && activeStep !== 2 && (
                  <Button onClick={handleBack} sx={{ mt: 3, ml: 1 }} color='primary'>
                    Back
                  </Button>
                )}
                {activeStep !== steps.length - 1 && (
                  <Button
                    variant="contained"
                    onClick={handleNext}
                    sx={{ mt: 3, ml: 1 }}
                  >
                    {activeStep === 1 ? 'Place order' : 'Next'}
                  </Button>
                )}
              </Box>
            </React.Fragment>
          )}
        </Paper>
      </Container>
    </React.Fragment>
  );
}

export default Checkout;