import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LoginResponse } from '../types/auth/LoginResponse';

export interface AuthState {
  username:  string | null;
  email:     string | null;
  jwt:       string | null;
}

const initialState: AuthState = {
  username: null,
  email:    null,
  jwt:      null
}

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<LoginResponse>) => {
      localStorage.setItem(
        "user",
        JSON.stringify({
          username: action.payload.username,
          email: action.payload.email,
          jwt: action.payload.jwt
        }),
      );
      state.username = action.payload.username;
      state.email = action.payload.email;
      state.jwt = action.payload.jwt;
    },
    logout: (state) => {
      localStorage.removeItem("user");
      state.username = null;
      state.email = null;
      state.jwt = null;
    },
  },
});

type AuthSelector = (state: { auth: AuthState }) => AuthState;
export const selectAuth: AuthSelector = (state) => state.auth;
export const { setUser, logout } = authSlice.actions;
export default authSlice.reducer;