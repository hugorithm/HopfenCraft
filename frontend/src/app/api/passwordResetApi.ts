import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { BASE_URL } from '../../config/constants';
import { PasswordResetRequest } from '../../types/password/passwordResetRequest';
import { PasswordResetResponse } from '../../types/password/passwordResetResponse';
import { PasswordReset } from '../../types/password/passwordReset';

export const passowrdResetApi = createApi({
  reducerPath: "passowrdResetApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  endpoints: (builder) => ({
    resetPasswordRequest: builder.mutation<PasswordResetResponse, PasswordResetRequest>({
      query: (body) => {
        return {
          url: `user/reset-password-request`,
          method: "POST",
          body
        };
      },
    }),
    validateToken: builder.query<void, string>({
      query: (token) => {
        return {
          url: `user/reset-password?token=${token}`,
          method: "GET"
        };
      },
    }),
    resetPassword: builder.mutation<void, PasswordReset>({
      query: (body) => {
        const token = localStorage.getItem("resetToken");
        return {
          url: `user/reset-password?token=${token}`,
          method: "Post",
          body
        };
      },
    }),
  })
});

export const { useResetPasswordRequestMutation, useResetPasswordMutation, useValidateTokenQuery } = passowrdResetApi;