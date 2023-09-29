import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../../config/constants';
import { LoginResponse } from '../../../types/auth/LoginResponse';
import { LoginRequestBody } from '../../../types/auth/LoginRequestBody';
import { SignUpResponse } from '../../../types/auth/SignUpResponse';
import { SignUpRequestBody } from '../../../types/auth/SignUpRequestBody';

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
          method: "POST",
          body
        };
      },
    }),
    signUp: builder.mutation<SignUpResponse, SignUpRequestBody>({
      query: (body) => {
        return {
          url: "/auth/register",
          method: "POST",
          body
        };
      },
    }),
  }),
});

export const { useLoginUserMutation, useSignUpMutation } = authApi;