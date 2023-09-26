import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../config/constants';
import { LoginResponse } from '../types/LoginResponse';
import { LoginRequestBody } from '../types/LoginRequestBody';

export const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    loginUser: builder.mutation<LoginResponse, LoginRequestBody>({
      query: (body) => {
        return {
          url: "/auth/login",
          method: "post",
          body
        };
      },
    }),
  }),
});

export const { useLoginUserMutation } = authApi;