import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { Order } from '../../types/order/Order';
import { ShippingDetails } from '../../types/order/ShippingDetails';

export const orderApi = createApi({
  reducerPath: "orderApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    createOrder: builder.mutation<Order, void>({
      query: () => {
        const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
        const headers = new Headers({
          'Authorization': `Bearer ${localJwt}`,
          'Content-Type': 'application/json',
        });

        return {
          url: `order/create`,
          method: "POST",
          headers
        };
      },
    }),
  })
});

export const { useCreateOrderMutation } = orderApi;