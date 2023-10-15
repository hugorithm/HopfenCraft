import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { CartItem, ShoppingCartResponse } from '../../types/shopping/ShoppingCartResponse';
import { ShoppingCartResquestBody } from '../../types/shopping/ShoppingCartRequestBody';
import { buildJsonHeadersWithJwt } from '../../utils/jwtUtils';

export const shoppingCartApi = createApi({
  reducerPath: "shoppingCartApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    shoppingCartAdd: builder.mutation<ShoppingCartResponse, ShoppingCartResquestBody>({
      query: (body) => {
        const headers = buildJsonHeadersWithJwt();

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
        const headers = buildJsonHeadersWithJwt();

        return {
          url: "/cart/items",
          method: "GET",
          headers
        };
      },
    }),
    deleteShoppingCart: builder.mutation<ShoppingCartResponse, CartItem>({
      query: (cartItem) => {
        const headers = buildJsonHeadersWithJwt();

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