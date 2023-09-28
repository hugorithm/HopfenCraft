import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CartItem, ShoppingCartResponse } from '../types/ShoppingCartResponse';

export interface ShoppingCartState {
  cartItems:  CartItem[];
}

const initialState: ShoppingCartState = {
    cartItems: []
}

export const shoppingCartSlice = createSlice({
  name: "shoppingCart",
  initialState,
  reducers: {
    setCartItems: (state, action: PayloadAction<ShoppingCartResponse>) => {
      state.cartItems = action.payload.cartItems;
    },
   
  },
});

type ShoppingCartSelector = (state: { shoppingCart: ShoppingCartState }) => ShoppingCartState;
export const selectShoppingCart: ShoppingCartSelector = (state) => state.shoppingCart;
export const { setCartItems } = shoppingCartSlice.actions;
export default shoppingCartSlice.reducer;