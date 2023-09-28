import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { CartItem, ShoppingCartResponse } from '../../types/ShoppingCartResponse';
import { ShoppingCartResquestBody } from '../../types/ShoppingCartRequestBody';



export const shoppingCartApi = createApi({
  reducerPath: "shoppingCartApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    shoppingCartAdd: builder.mutation<ShoppingCartResponse, ShoppingCartResquestBody>({
      query: (body) => {
        const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
        // Create headers with the JWT token
        const headers = new Headers({
          'Authorization': `Bearer ${localJwt}`,
          'Content-Type': 'application/json',
        });

        return {
          url: "/cart/add",
          method: "POST",
          headers,
          body
        };
      },
    }),
    getShoppingCart: builder.query<ShoppingCartResponse, void>({
      query: () => {
        const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
        const headers = new Headers({
          'Authorization': `Bearer ${localJwt}`,
          'Content-Type': 'application/json',
        });

        return {
          url: "/cart/items",
          method: "GET",
          headers
        };
      },
    }),
    deleteShoppingCart: builder.mutation<ShoppingCartResponse, CartItem>({
      query: (cartItem) => {
        const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
        const headers = new Headers({
          'Authorization': `Bearer ${localJwt}`,
          'Content-Type': 'application/json',
        });

        return {
          url: `/cart/remove/${cartItem.cartItemId}`,
          method: "DELETE",
          headers
        };
      },
    }),
  }),

});

export const { useShoppingCartAddMutation, useGetShoppingCartQuery, useDeleteShoppingCartMutation } = shoppingCartApi;