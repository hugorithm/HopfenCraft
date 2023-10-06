import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { Order } from "../types/order/Order";
import { OrderList } from "../types/order/OrderList";
import { RootState } from "../app/store";
import { BASE_URL } from "../config/constants";

interface OrdersState {
    orders: Order[];
    loading: 'idle' | 'loading' | 'succeeded' | 'failed';
    error: string | null;
    page: number;
    last: boolean;
  }
  
  const initialState: OrdersState = {
    orders: [],
    loading: 'idle',
    error: null,
    page: 0,
    last: true,
  };

export const fetchOrders = createAsyncThunk<OrderList, void, { state: RootState }>(
    'orders/fetchOrders',
    async (_, { getState }) => {
      const { page } = getState().orders;
      const apiUrl = `${BASE_URL}/orders?page=${page}&size=15`;

      const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
      const headers = new Headers({
        'Authorization': `Bearer ${localJwt}`,
        'Content-Type': 'application/json',
      });

      const response = await fetch(apiUrl, {
        method: "GET",
        headers
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      const data = await response.json();
      return data;
    }
  );

  export const ordersSlice = createSlice({
    name: "orders",
    initialState,
    reducers: {
      setOrders: (state, action: PayloadAction<Order[]>) => {
        state.orders = action.payload;
      },
      resetPage: (state) => {
        state.page = 0;
      },
      resetOrders: (state) => {
        state.orders = [];
      }
    },
    extraReducers: (builder) => {
      builder
        .addCase(fetchOrders.pending, (state) => {
          state.loading = 'loading';
        })
        .addCase(fetchOrders.fulfilled, (state, action: PayloadAction<OrderList>) => {
          state.loading = 'succeeded';
          state.page += 1;
          state.last = action.payload.last;
          state.orders.push(...action.payload.content);
        })
        .addCase(fetchOrders.rejected, (state, action) => {
          state.loading = 'failed';
          state.error = action.error.message ?? 'An error occurred while fetching orders.';
        });
    },
  });

export const selectOrders = (state: RootState) => state.orders;
export const { setOrders, resetPage, resetOrders } = ordersSlice.actions;
export default ordersSlice.reducer;
