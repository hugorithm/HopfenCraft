import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { useSelector } from 'react-redux';
import { selectShoppingCart, setCartItems } from '../features/shoppingCartSlice';
import DeleteIcon from '@mui/icons-material/Delete';
import { CardMedia, IconButton, Typography } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { useDeleteShoppingCartMutation, useGetShoppingCartQuery } from '../app/api/shoppingCartApi';
import { useEffect } from 'react';
import { CartItem, Product } from '../types/ShoppingCartResponse';
import { useAppDispatch } from '../app/hooks';
import ShoppingCartSkeleton from './ShoppingCartSkeleton';


function ccyFormat(num: number) {
  return `${num.toFixed(2)}`;
}

const ShoppingCartTable = () => {
  const { cartItems } = useSelector(selectShoppingCart);
  const dispatch = useAppDispatch();
  const { data: getCartData, error, isLoading, isSuccess: getCartSuccess, } = useGetShoppingCartQuery();


  useEffect(() => {
    if (cartItems.length === 0) {
      if (getCartData) {
        dispatch(setCartItems({ cartItems: getCartData.cartItems }));
      } else if (error) {
        console.log(error);
      }
    }
  }, [getCartData]);

  const [deleteShoppingCart,
    { data: shoppingCartData,
      isSuccess: isShoppingCartSuccess,
      isError: isShoppingCartError,
      error: shoppingCartError
    },
  ] = useDeleteShoppingCartMutation();

  useEffect(() => {
    if (isShoppingCartSuccess && shoppingCartData) {
      dispatch(setCartItems({ cartItems: shoppingCartData.cartItems }));
    }
  }, [isShoppingCartSuccess])

  useEffect(() => {
    if (isShoppingCartError) {
      console.error(shoppingCartError)
    }
  }, [isShoppingCartError])

  const total = cartItems.reduce((accumulator, cartItem) => {
    const price = parseFloat(cartItem.product.price);
    const itemTotal = cartItem.quantity * price;

    return accumulator + itemTotal;
  }, 0);

  const handleDelete = (cartItem: CartItem) => {
    deleteShoppingCart(cartItem);
  }

  return (
    <TableContainer component={Paper} elevation={3} sx={{ maxWidth: 800, margin: '0 auto' }}>
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
          {isLoading ? (
            <>
              <ShoppingCartSkeleton />
            </>
          ) : cartItems.length === 0 ? (
            <TableRow>
              <TableCell colSpan={5} align="center">
                Shopping cart is empty
              </TableCell>
            </TableRow>
          ) : (
            cartItems.map((cartItem) => (
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
                <TableCell align="center">€ {cartItem.total}</TableCell>
                <TableCell align="center">
                  <IconButton onClick={() => handleDelete(cartItem)} color="secondary" aria-label="delete" size="small">
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            )
            ))}
          {!isLoading && cartItems.length > 0 && (<TableRow>
            <TableCell colSpan={2} />
            <TableCell align="right">
              <Typography variant="h6">Total</Typography>
            </TableCell>
            <TableCell align="right">
              <Typography variant="h6">€ {ccyFormat(total)}</Typography>
            </TableCell>
            <TableCell />
          </TableRow>
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
};
export default ShoppingCartTable;
