import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authApi } from './api/auth/authApi';
import { setupListeners } from '@reduxjs/toolkit/query/react'
import authReducer  from '../features/authSlice';
import productsReducer from '../features/productsSlice'; 

const rootReducer = combineReducers({
  auth: authReducer,
  [authApi.reducerPath]: authApi.reducer,
  products: productsReducer, 
});

export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(authApi.middleware),
});

// ... (AppDispatch and RootState code as shown in the previous message)
setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;