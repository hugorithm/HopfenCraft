import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { Product } from '../../types/product/ProductData';

export const productApi = createApi({
  reducerPath: "productApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    getProduct: builder.query<Product, string>({
      query: (id: string) => {
        const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;
        const headers = new Headers({
          'Authorization': `Bearer ${localJwt}`,
          'Content-Type': 'application/json',
        });

        return {
          url: `product/${id}`,
          method: "GET",
          headers
        };
      },
    }),
  })
});

export const { useGetProductQuery } = productApi;