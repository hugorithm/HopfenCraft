import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { Product } from '../../types/product/ProductData';
import { ProductRegistration } from '../../types/product/ProductRegistration';
import { buildJsonHeadersWithJwt } from '../../utils/jwtUtils';

export const productApi = createApi({
  reducerPath: "productApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    getProduct: builder.query<Product, string>({
      query: (id: string) => {
        const headers = buildJsonHeadersWithJwt();

        return {
          url: `product/${id}`,
          method: "GET",
          headers
        };
      },
    }),
    registerProduct: builder.mutation<Product, ProductRegistration>({
      query: (body) => {
        const headers = buildJsonHeadersWithJwt();

        return {
          url: `product/register`,
          method: "POST",
          headers,
          body
        };
      }
    }),
  })
});

export const { useGetProductQuery, useRegisterProductMutation } = productApi;