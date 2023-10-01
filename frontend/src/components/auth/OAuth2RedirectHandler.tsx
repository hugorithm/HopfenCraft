import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch } from '../../app/hooks';
import { setUser } from '../../features/authSlice';
import { useGetOAuth2UserQuery } from '../../app/api/auth/authApi';
import { useThemeContext } from '../../theme/ThemeContextProvider';
import { toast } from 'react-toastify';

const OAuth2RedirectHandler = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const token = new URLSearchParams(location.search).get("token") ?? "";
  const error = new URLSearchParams(location.search).get("error");
  const { data, isSuccess, isError, error: oAuth2Error } = useGetOAuth2UserQuery(token);
  const { mode }: any = useThemeContext();

  useEffect(() => {
    if (token) {
      if (data && isSuccess) {
        dispatch(setUser({ username: data.name, email: data.email, jwt: token }));
        toast.success('Login Successful!', {
          position: "top-right",
          autoClose: 2000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          pauseOnFocusLoss: false,
          progress: undefined,
          theme: mode,
        });
        navigate("/profile");
      }
    } else {
      navigate("/login");
    }
  }, [isSuccess]);

  useEffect(() => {
    if (error) {
      console.error(error);
      navigate("/login");
    }
  }, [error]);

  useEffect(() => {
    if (isError) {
      console.error(oAuth2Error);
      navigate("/login");
    }
  }, [isError]);

  return null;
}

export default OAuth2RedirectHandler;
