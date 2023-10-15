import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { Order } from '../../types/order/Order';
import { ShippingDetails } from '../../types/order/ShippingDetails';
import { buildJsonHeadersWithJwt } from '../../utils/jwtUtils';

export const orderApi = createApi({
  reducerPath: "orderApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    createOrder: builder.mutation<Order, ShippingDetails>({
      query: (body) => {
        const headers = buildJsonHeadersWithJwt();

        return {
          url: "order/create",
          method: "POST",
          headers,
          body
        };
      },
    }),
    getOrder: builder.query<Order, string>({
      query: (id) => {
        const headers = buildJsonHeadersWithJwt();

        return {
          url: `/order/${id}`,
          method: "GET",
          headers,
        };
      },
    }),
  }),
});

export const { useCreateOrderMutation, useGetOrderQuery } = orderApi;