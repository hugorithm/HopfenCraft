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
import PaymentConfirmation from '../components/PaymentConfirmation';
import { useGetShoppingCartQuery } from '../app/api/shoppingCartApi';
import { selectShoppingCart, setCartItems } from '../features/shoppingCartSlice';
import { ShippingDetails } from '../types/order/ShippingDetails';
import { useSelector } from 'react-redux';

const Checkout = () => {
  const [createOrder, { data: orderData, isError, isSuccess, isLoading, error }] = useCreateOrderMutation();
  const { cartItems } = useSelector(selectShoppingCart);
  const [activeStep, setActiveStep] = useState(0);
  const dispatch = useAppDispatch();
  const [isPayed, setIsPayed] = useState(false);
  const steps = ['Shipping address', 'Review your order', 'Order confirmation', 'Payment details'];

  const {
    data: shoppingCartData,
    error: shoppingCartError,
    isSuccess: isShoppingCartSuccess,
    isError: isShoppingCartError,
    refetch
  } = useGetShoppingCartQuery();

  useEffect(() => {
    if (isShoppingCartError) {
      console.error(shoppingCartError);
    }
  }, [isShoppingCartError])

  // useEffect(() => {
  //   if (cartItems.length === 0 && shoppingCartData && isShoppingCartSuccess) {
  //     dispatch(setCartItems({ cartItems: shoppingCartData.cartItems }));
  //   }
  // }, [shoppingCartData, isShoppingCartSuccess])

  const handleApprove = () => {
    setIsPayed(true);
    setActiveStep(4); //step after the last step so it shows as completed
    refetch();
  }

  const [shippingData, setShippingData] = useState({
    firstName: '',
    lastName: '',
    address1: '',
    address2: '',
    city: '',
    state: '',
    zip: '',
    country: '',
    isBilling: false
  });

  const handleFormChange = (name: string, value: string | boolean) => {
    setShippingData({
      ...shippingData,
      [name]: value,
    });
  };

  const getStepContent = (step: number) => {
    switch (step) {
      case 0:
        return <AddressForm shippingDetails={shippingData} onFormChange={handleFormChange} />;
      case 1:
        return <Review shippingDetails={shippingData} />;
      case 2:
        return <OrderConfirmation />
      case 3:
        return <PaypalPayment onApproveCallback={handleApprove} />;
      default:
        throw new Error('Unknown step');
    }
  }

  useEffect(() => {
    if (isSuccess && orderData) {
      dispatch(setOrder(orderData));
    }
  }, [isSuccess]);

  const handleNext = async () => {
    let shippingDetails: ShippingDetails;
    if (activeStep === 1) {
      if (!shippingData.isBilling) {
        shippingDetails = {
          shippingName: shippingData.firstName.concat(" ", shippingData.lastName),
          shippingAddress: shippingData.address1.concat(", ", shippingData.address2),
          shippingCity: shippingData.city,
          shippingState: shippingData.state,
          shippingPostalCode: shippingData.zip,
          shippingCountry: shippingData.country
        } as ShippingDetails
      } else {
        shippingDetails = {
          shippingName: shippingData.firstName.concat(" ", shippingData.lastName),
          shippingAddress: shippingData.address1.concat(", ", shippingData.address2),
          shippingCity: shippingData.city,
          shippingState: shippingData.state,
          shippingPostalCode: shippingData.zip,
          shippingCountry: shippingData.country,
          billingName: shippingData.firstName.concat(" ", shippingData.lastName),
          billingAddress: shippingData.address1.concat(", ", shippingData.address2),
          billingCity: shippingData.city,
          billingState: shippingData.state,
          billingPostalCode: shippingData.zip,
          billingCountry: shippingData.country
        } as ShippingDetails
      }

      createOrder(shippingDetails)
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
          {!isPayed && activeStep !== 4 ? (
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
          ) : (
            <PaymentConfirmation />
          )}
        </Paper>
      </Container>
    </React.Fragment>
  );
}

export default Checkout;