import * as React from 'react';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Grid from '@mui/material/Grid';
import { useGetShoppingCartQuery } from '../app/api/shoppingCartApi';
import { useSelector } from 'react-redux';
import { selectShoppingCart, setCartItems } from '../features/shoppingCartSlice';
import { CartItem } from '../types/shopping/ShoppingCartResponse';
import { useEffect } from 'react';
import { useAppDispatch } from '../app/hooks';
import formatPrice from '../utils/priceFormatter';

interface AddressFormProps {
  shippingDetails: {
    firstName: string;
    lastName: string;
    address1: string;
    address2: string;
    city: string;
    state: string;
    zip: string;
    country: string;
    isBilling: boolean;
  };
}

const Review: React.FC<AddressFormProps> = ({ shippingDetails }) => {
  const { cartItems } = useSelector(selectShoppingCart);
  const { data: getCartData, error, isLoading, isSuccess: getCartSuccess, } = useGetShoppingCartQuery();
  const dispatch = useAppDispatch();

  const total = cartItems.reduce((accumulator, cartItem) => {
    const price = parseFloat(cartItem.product.price);
    const itemTotal = cartItem.quantity * price;

    return accumulator + itemTotal;
  }, 0);

  useEffect(() => {
    if (cartItems.length === 0) {
      if (getCartData) {
        dispatch(setCartItems({ cartItems: getCartData.cartItems }));
      } else if (error) {
        console.error(error);
      }
    }
  }, [getCartData]);

  return (
    <>
      <Typography variant="h6" gutterBottom>
        Order summary
      </Typography>
      <List disablePadding>
        {cartItems.map((cartItem: CartItem) => (
          <ListItem key={cartItem.cartItemId} sx={{ py: 1, px: 0 }}>
            <ListItemText primary={cartItem.product.name} secondary={cartItem.product.description} />
            <Typography variant="body2">{cartItem.quantity} x €{cartItem.product.price}</Typography>
          </ListItem>
        ))}
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText primary="Total" />
          <Typography variant="subtitle1" sx={{ fontWeight: 700 }}>
            € {formatPrice(total)}
          </Typography>
        </ListItem>
      </List>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
            Shipping
          </Typography>
          <Typography gutterBottom>{shippingDetails?.firstName.concat(" ", shippingDetails?.lastName)}</Typography>
          <Typography gutterBottom>
            {shippingDetails.address1.concat(
              ', ',
              shippingDetails.address2,
              " ",
              shippingDetails.city,
              " ",
              shippingDetails.state,
              " ",
              shippingDetails.zip,
              " ",
              shippingDetails.country
            )}
          </Typography>
        </Grid>
        <Grid item container direction="column" xs={12} sm={6}>
          <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
            Payment details
          </Typography>
          <Grid container>
            <Grid item xs={6}>
              <Typography gutterBottom>Payment type</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography gutterBottom>Paypal</Typography>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </>
  );
}

export default Review;