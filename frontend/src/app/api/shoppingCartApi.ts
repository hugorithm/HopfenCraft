import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { ShoppingCartResponse } from '../../types/ShoppingCartResponse';
import { ShoppingCartResquestBody } from '../../types/ShoppingCartRequestBody';

const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;

export const shoppingCartApi = createApi({
  reducerPath: "shoppingCartApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    shoppingCartAdd: builder.mutation<ShoppingCartResponse, ShoppingCartResquestBody>({
      query: (body) => {

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
    getShoppingCart: builder.mutation<ShoppingCartResponse, void>({
      query: () => {
        const headers = new Headers({
          'Authorization': `Bearer ${localJwt}`,
          'Content-Type': 'application/json',
        });

        return {
          url: "/cart/add",
          method: "GET",
          headers
        };
      },
    }),
  }),

});

export const { useShoppingCartAddMutation, useGetShoppingCartMutation } = shoppingCartApi;