import React, { useEffect } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import { Link as RouterLink } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { selectAuth } from '../features/authSlice';
import { useGetShoppingCartMutation } from '../app/api/shoppingCartApi';
import { selectShoppingCart, setCartItems } from '../features/shoppingCartSlice';
import { useSelector } from 'react-redux';

const Profile = () => {
  const { username, email } = useAppSelector(selectAuth);
  const dispatch = useAppDispatch();
  const { cartItems } = useSelector(selectShoppingCart);

  const [getShoppingCart,
    { data: shoppingCartData,
      isSuccess: isShoppingCartSuccess,
      isError: isShoppingCartError,
      error: shoppingCartError
    },
  ] = useGetShoppingCartMutation();

  useEffect(() => {
    if (isShoppingCartSuccess && shoppingCartData) {
      dispatch(setCartItems({ cartItems: shoppingCartData.cartItems }));
    }
  }, [isShoppingCartSuccess])

  useEffect(() => {
    if (cartItems.length === 0) {
      getShoppingCart();
    }
  }, []);

  const handleEditProfileClick = () => {
    console.log("Edit Profile clicked!");
  };

  return (
    <>
      <CssBaseline />
      <Container maxWidth="sm">
        <Box
          sx={{
            mt: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Typography component="h1" variant="h2" align="center" color="text.primary" gutterBottom>
            Welcome {username}!
          </Typography>
          <Typography variant="h5" align="center" color="text.secondary" paragraph>
            Welcome to your HopfenCraft profile. Here, you can manage your account and preferences.
          </Typography>
          <Stack
            sx={{ pt: 4 }}
            direction="row"
            spacing={2}
            justifyContent="center"
          >
            <Button
              variant="contained"
              color="primary"
              component={RouterLink}
              to="/edit-profile"
              onClick={handleEditProfileClick}
            >
              Edit Profile
            </Button>
          </Stack>
        </Box>
      </Container>
    </>
  );
}

export default Profile;
