import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { Order } from '../types/order/Order';


export interface OrderState {
  order:  Order | null;
  isLoading:    boolean;
}

const initialState: OrderState = {
    order: null,
    isLoading:   true
}

export const OrderSlice = createSlice({
  name: "order",
  initialState,
  reducers: {
    setOrder: (state, action: PayloadAction<Order>) => {
      state.order = action.payload;
      state.isLoading = false;
    },
  },
});

type OrderSelector = (state: { order: OrderState }) => OrderState;
export const selectOrder: OrderSelector = (state) => state.order;
export const { setOrder } = OrderSlice.actions;
export default OrderSlice.reducer;