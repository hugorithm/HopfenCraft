import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authApi } from './api/auth/authApi';
import { setupListeners } from '@reduxjs/toolkit/query/react'
import authReducer  from '../features/authSlice';
import productsReducer from '../features/productsSlice'; 
import shoppingCartReducer from '../features/shoppingCartSlice';
import { shoppingCartApi } from "./api/shoppingCartApi";
import { productApi } from "./api/productApi";

const rootReducer = combineReducers({
  auth: authReducer,
  [authApi.reducerPath]: authApi.reducer,
  [shoppingCartApi.reducerPath]: shoppingCartApi.reducer,
  [productApi.reducerPath] : productApi.reducer,
  shoppingCart: shoppingCartReducer,
  products: productsReducer, 
});

export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(authApi.middleware, shoppingCartApi.middleware, productApi.middleware),
});

// ... (AppDispatch and RootState code as shown in the previous message)
setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;