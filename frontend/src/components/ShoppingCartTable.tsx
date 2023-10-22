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
import { Button, IconButton, TextField, Typography } from '@mui/material';
import { useDeleteShoppingCartMutation, useGetShoppingCartQuery, useUpdateCartItemQuantityMutation } from '../app/api/shoppingCartApi';
import { ChangeEvent, useEffect, useState } from 'react';
import { CartItem } from '../types/shopping/ShoppingCartResponse';
import { useAppDispatch } from '../app/hooks';
import ShoppingCartSkeleton from './ShoppingCartSkeleton';
import { toast } from 'react-toastify';
import { useThemeContext } from '../theme/ThemeContextProvider';
import { Link as RouterLink } from 'react-router-dom';
import formatPrice from '../utils/priceFormatter';
import ProductImageLink from './ProductImageLink';
import { CartItemUpdateQuantityRequest } from '../types/shopping/CartItemUpdateQuantityRequest';

const ShoppingCartTable = () => {
  const { cartItems } = useSelector(selectShoppingCart);
  const dispatch = useAppDispatch();
  const { data: getCartData, error, isLoading, isSuccess: getCartSuccess, refetch } = useGetShoppingCartQuery();
  const { mode }: any = useThemeContext();
  const [localQuantities, setLocalQuantities] = useState<Record<number, number>>({});

  const [updateCartItemQuantity,
    {
      data: updateData,
      isSuccess: updateIsSuccess,
      isError: updateIsError,
      error: updateError
    }
  ] = useUpdateCartItemQuantityMutation();

  const [deleteShoppingCart,
    {
      data: shoppingCartData,
      isSuccess: isShoppingCartSuccess,
      isError: isShoppingCartError,
      error: shoppingCartError
    },
  ] = useDeleteShoppingCartMutation();

  useEffect(() => {
    refetch();
  }, []);

  useEffect(() => {
    if (cartItems.length > 0) {
      const initialQuantities = cartItems.reduce((quantities, cartItem) => {
        quantities[cartItem.cartItemId] = cartItem.quantity;
        return quantities;
      }, {} as Record<number, number>);
      setLocalQuantities(initialQuantities);
    }
  }, [cartItems]);

  useEffect(() => {
    if (getCartData && getCartSuccess) {
      dispatch(setCartItems({ cartItems: getCartData.cartItems }));
    } else if (error) {
      console.error(error);
    }
  }, [getCartData, getCartSuccess]);

  useEffect(() => {
    if (isShoppingCartSuccess && shoppingCartData) {
      dispatch(setCartItems({ cartItems: shoppingCartData.cartItems }));
    }
  }, [isShoppingCartSuccess, shoppingCartData]);

  useEffect(() => {
    if (updateIsSuccess && updateData) {
      dispatch(setCartItems({ cartItems: updateData.cartItems }));
    }
  }, [updateIsSuccess, updateData]);

  useEffect(() => {
    if (updateIsError) {
      console.error(updateError)
    }
  }, [updateIsError]);

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
    deleteShoppingCart(cartItem)
      .unwrap()
      .then(() => {
        toast.success('Deleted Item!', {
          position: "top-right",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          pauseOnFocusLoss: false,
          progress: undefined,
          theme: mode,
        });
      })
      .catch(() => {
        toast.error('Failed to delete Item!', {
          position: "top-right",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          pauseOnFocusLoss: false,
          progress: undefined,
          theme: mode,
        });
      });
  }

  const updateQuantity = (cartItemId: number) => {
    const body: CartItemUpdateQuantityRequest = {
      cartItemId,
      quantity: localQuantities[cartItemId]
    }

    updateCartItemQuantity(body)
      .unwrap()
      .then(() => {
        toast.success('Quantity updated!', {
          position: "top-right",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          pauseOnFocusLoss: false,
          progress: undefined,
          theme: mode,
        });
      })
      .catch((error: { status: number, data: string }) => {
        let message = "Failed to update quantity!";

        if (error.status === 404) {
          message = "Failed to update quantity!";
        } else if (error.status === 400) {
          message = "Unable to update the quantity. The requested quantity exceeds the available stock!"
        }

        toast.error(message, {
          position: "top-right",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          pauseOnFocusLoss: false,
          progress: undefined,
          theme: mode,
        });
      });
  }

  const handleUnfocused = (cartItemId: number) => {
    const cartItem = cartItems.find((ci) => ci.cartItemId === cartItemId);

    if (cartItem && cartItem.quantity !== localQuantities[cartItemId]) {
      updateQuantity(cartItemId);
    }
  }

  const handleKKeyUp = (e: React.KeyboardEvent, cartItemId: number) => {
    if (e.key !== "Enter") return;

    e.preventDefault();
    updateQuantity(cartItemId);
  }

  const handleQuantity = (e: ChangeEvent<HTMLInputElement>, cartItemId: number) => {
    const newQuantity = parseInt(e.target.value, 10);
    setLocalQuantities({
      ...localQuantities,
      [cartItemId]: newQuantity,
    });
  };

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
            cartItems.map((cartItem: CartItem) => (
              <TableRow key={cartItem.cartItemId}>
                <TableCell component="th" scope="row">
                  <ProductImageLink productId={cartItem.product.productId} />
                </TableCell>
                <TableCell>
                  <Button
                    component={RouterLink}
                    to={`/product/${cartItem.product.productId}`}
                    color='inherit'
                    sx={{ textTransform: 'none' }}
                  >
                    {cartItem.product.name}
                  </Button>
                </TableCell>
                <TableCell align="center">
                  <TextField sx={{ width: "4rem" }}
                    id="quantity"
                    name="quantity"
                    value={localQuantities[cartItem.cartItemId] || ""}
                    onChange={(e: ChangeEvent<HTMLInputElement>) => handleQuantity(e, cartItem.cartItemId)}
                    onKeyUp={(e) => handleKKeyUp(e, cartItem.cartItemId)}
                    onBlur={() => handleUnfocused(cartItem.cartItemId)}
                  />
                </TableCell>
                <TableCell align="center">€ {cartItem.product.price}</TableCell>
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
              <Typography variant="h6">€ {formatPrice(total)}</Typography>
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