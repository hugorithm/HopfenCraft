import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useValidateTokenQuery } from '../../api/passwordResetApi';

const PasswordResetHandler = () => {
  const location = useLocation();
  const token = new URLSearchParams(location.search).get("token") ?? "";
  const navigate = useNavigate();
  const encodedToken = encodeURIComponent(token).replace(".", "%2E");
  localStorage.setItem("resetToken", encodedToken);
  const {isSuccess, isError, error} = useValidateTokenQuery(encodedToken);

  useEffect(() => {
    if (isError) {
      console.error(error);
      navigate("/invalid-token");
    }
  }, [isError]);

  useEffect(() => {
    if (isSuccess) {
      navigate("/user/reset-password-form");
    }
  }, [isSuccess]);

  return null;
}

export default PasswordResetHandler;
