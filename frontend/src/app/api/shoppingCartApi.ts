import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { ShoppingCartResponse } from '../../types/ShoppingCartResponse';
import { ShoppingCartResquestBody } from '../../types/ShoppingCartRequestBody';
import { selectAuth } from '../../features/authSlice';
import { useSelector } from 'react-redux';

export const shoppingCartApi = createApi({
  reducerPath: "shoppingCartApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    shoppingCartAdd: builder.mutation<ShoppingCartResponse, ShoppingCartResquestBody>({
      query: (body) => {
        const { jwt } = useSelector(selectAuth);
      
        // Create headers with the JWT token
        const headers = new Headers({
          'Authorization': `Bearer ${jwt}`,
          'Content-Type': 'application/json', 
        });

        return {
          url: "/cart/add",
          method: "post",
          headers,
          body
        };
      },
    }),
  }),
});

export const { useShoppingCartAddMutation } = shoppingCartApi;