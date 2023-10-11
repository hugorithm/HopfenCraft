import React from 'react';
import { Outlet, Navigate } from 'react-router-dom';

const RequireResetToken = () => {
  
  const token = localStorage.getItem("resetToken") || "";
  
  return (
    !token ? <Navigate to="/invalid-token" /> : <Outlet />
  )

};

export default RequireResetToken;