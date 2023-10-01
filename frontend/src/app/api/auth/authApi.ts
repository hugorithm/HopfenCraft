import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../../config/constants';
import { LoginResponse } from '../../../types/auth/LoginResponse';
import { LoginRequestBody } from '../../../types/auth/LoginRequestBody';
import { SignUpResponse } from '../../../types/auth/SignUpResponse';
import { SignUpRequestBody } from '../../../types/auth/SignUpRequestBody';
import { OAuth2UserResponse } from '../../../types/auth/OAuth2UserResponse';

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
    getOAuth2User : builder.query<OAuth2UserResponse, string>({
      query: (token: string) => {
        const headers = new Headers({
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        });

        return {
          url: "/oauth2/current-user",
          method: "GET",
          headers
        }
      }
    })
  }),
});

export const { useLoginUserMutation, useSignUpMutation, useGetOAuth2UserQuery } = authApi;