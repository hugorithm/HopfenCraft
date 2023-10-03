import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import ShoppingCartTable from '../components/ShoppingCartTable';
import { Button, Fade } from '@mui/material';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectShoppingCart } from '../features/shoppingCartSlice';

const ShoppingCart = () => {
  const { cartItems } = useSelector(selectShoppingCart);
  return (
    <>
      <CssBaseline />
      <Fade in={true} timeout={1000}>
        <Container>
          <Box
            sx={{
              mt: 8,
              mb: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Typography component="h1" variant="h2" align="center" color="text.primary" gutterBottom>
              Shopping Cart
            </Typography>
            <ShoppingCartTable />
            <Box mt={2}>
              {cartItems.length !== 0 && (
                <Button
                  component={Link}
                  to="/checkout"
                  variant="contained"
                  color="primary"
                  size="large"
                >
                  Go to Checkout
                </Button>
              )}
            </Box>
          </Box>
        </Container>
      </Fade>
    </>
  );
}


export default ShoppingCart;
