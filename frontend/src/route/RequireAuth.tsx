import { Outlet, Navigate } from 'react-router-dom';
import { selectAuth } from '../features/authSlice';
import { useSelector } from 'react-redux';

const RequireAuth = () => {
  const { jwt } = useSelector(selectAuth);
  const localJwt = JSON.parse(localStorage.getItem("user") || "{}").jwt;
  
  return (
    !jwt && !localJwt ? <Navigate to="/login" /> : <Outlet />
  )

};

export default RequireAuth;