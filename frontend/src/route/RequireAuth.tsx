import React from 'react';
import { Outlet, Navigate } from 'react-router-dom';
import { isAuthenticated } from '../auth/auth';


const RequireAuth = () => {
  return (
    isAuthenticated() ? <Outlet/> : <Navigate to="/login"/>
  )

};

export default RequireAuth;