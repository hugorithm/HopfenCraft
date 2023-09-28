import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import { Link as RouterLink } from 'react-router-dom';
import ShoppingCartTable from '../components/ShoppingCartTable';

const ShoppingCart = () => {
  return (
    <>
      <CssBaseline />
      <Container>
        <Box
          sx={{
            mt: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Typography component="h1" variant="h2" align="center" color="text.primary" gutterBottom>
            Shopping Cart
          </Typography>
          <ShoppingCartTable />
        </Box>
      </Container>
    </>
  );
}

export default ShoppingCart;
