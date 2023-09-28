import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import ShoppingCartTable from '../components/ShoppingCartTable';
import { Button } from '@mui/material';
import { Link } from 'react-router-dom';

const ShoppingCart = () => {
  return (
    <>
      <CssBaseline />
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
            <Button
              component={Link}
              to="/checkout" 
              variant="contained"
              color="primary"
              size="large"
            >
              Go to Checkout
            </Button>
          </Box>
        </Box>
      </Container>
    </>
  );
}


export default ShoppingCart;
