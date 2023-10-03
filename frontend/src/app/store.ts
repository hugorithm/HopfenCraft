import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authApi } from './api/auth/authApi';
import { setupListeners } from '@reduxjs/toolkit/query/react'
import authReducer from '../features/authSlice';
import productsReducer from '../features/productsSlice';
import shoppingCartReducer from '../features/shoppingCartSlice';
import orderReducer from '../features/orderSlice';
import { shoppingCartApi } from "./api/shoppingCartApi";
import { productApi } from "./api/productApi";
import { rtkQueryErrorLogger } from "./middleware/ErrorHandler";
import { orderApi } from "./api/orderApi";

const rootReducer = combineReducers({
  auth: authReducer,
  [authApi.reducerPath]: authApi.reducer,
  [shoppingCartApi.reducerPath]: shoppingCartApi.reducer,
  [productApi.reducerPath]: productApi.reducer,
  [orderApi.reducerPath]: orderApi.reducer,
  shoppingCart: shoppingCartReducer,
  order: orderReducer,
  products: productsReducer,
});

export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware()
    .concat(
      authApi.middleware,
      shoppingCartApi.middleware,
      productApi.middleware,
      orderApi.middleware,
      rtkQueryErrorLogger
    ),
});


setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;