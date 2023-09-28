import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { useSelector } from 'react-redux';
import { selectShoppingCart } from '../features/shoppingCartSlice';
import DeleteIcon from '@mui/icons-material/Delete';
import { CardMedia, IconButton, Typography } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product } from '../types/ProductData';

function ccyFormat(num: number) {
  return `${num.toFixed(2)}`;
}

const ShoppingCartTable = () => {
  const { cartItems } = useSelector(selectShoppingCart);

  const total = cartItems.reduce((accumulator, cartItem) => {
    const price = parseFloat(cartItem.product.price);
    const itemTotal = cartItem.quantity * price;

    return accumulator + itemTotal;
  }, 0);

  const handleDelete = (product: Product) => {
    console.log(product)
  }

  return (
    <TableContainer component={Paper} elevation={3} sx={{ maxWidth: 800, margin: '0 auto', borderRadius: 8 }}>
      <Table aria-label="shopping-cart-table">
        <TableHead>
          <TableRow>
            <TableCell>Product Details</TableCell>
            <TableCell></TableCell>
            <TableCell align="center">Quantity</TableCell>
            <TableCell align="center">Price</TableCell>
            <TableCell align="center"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {cartItems.map((cartItem) => (
            <TableRow key={cartItem.cartItemId}>
              <TableCell component="th" scope="row">
                <CardMedia
                  component="div"
                  sx={{
                    pt: '50.25%',
                    backgroundSize: 'contain',
                  }}
                  image={`${BASE_URL}/product/${cartItem.product.productId}/image`}
                />
              </TableCell>
              <TableCell>
                {cartItem.product.name}
              </TableCell>
              <TableCell align="center">{cartItem.quantity}</TableCell>
              <TableCell align="center">{ccyFormat(cartItem.quantity * parseFloat(cartItem.product.price))}</TableCell>
              <TableCell align="center">
                <IconButton onClick={() => handleDelete(cartItem.product)} color="secondary" aria-label="delete" size="small">
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
          <TableRow>
            <TableCell colSpan={2} />
            <TableCell align="center">
              <Typography variant="h6">Total</Typography>
            </TableCell>
            <TableCell align="center">
              <Typography variant="h6">€ {ccyFormat(total)}</Typography>
            </TableCell>
            <TableCell />
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
};
export default ShoppingCartTable;