import {
  Route,
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
  Outlet
} from 'react-router-dom';
import './App.css'
import Login from './pages/Login';
import Home from './pages/Home';
import Navbar from './components/Navbar';
import About from './pages/About';
import Contacts from './pages/Contacts';
import Products from './pages/Products';
import SignUp from './pages/Signup';
import { CssBaseline, ThemeProvider } from "@mui/material";
import { useThemeContext } from "./theme/ThemeContextProvider";
import CustomError from './errors/CustomError';
import NotFound from './errors/NotFound';
import Profile from './pages/Profile';
import RequireAuth from './route/RequireAuth';
import { useAppDispatch } from './app/hooks';
import { useEffect } from 'react';
import { setUser } from './features/authSlice';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { LoginResponse } from './types/auth/LoginResponse';
import ShoppingCart from './pages/ShoppingCart';
import OAuth2RedirectHandler from './app/middleware/handlers/OAuth2RedirectHandler';
import ProductDetails from './pages/ProductDetails';
import Checkout from './pages/Checkout';
import Orders from './pages/Orders';
import Order from './pages/Order';
import PasswordResetHandler from './app/middleware/handlers/PasswordResetHandler';
import ResetPasswordResquest from './pages/ResetPasswordRequest';
import PasswordResetForm from './pages/PasswordResetForm';
import RequireResetToken from './route/RequireResetToken';
import InvalidToken from './errors/InvalidToken';
import RequireAdminRole from './route/RequireAdminRole';
import RegisterProduct from './pages/RegisterProduct';
import UpdateProduct from './pages/UpdateProduct';
import ProductsDashboard from './pages/ProductsDashboard';

const Root = () => {
  return (
    <>
      <div>
        <Navbar />
      </div>
      <div>
        <Outlet />
      </div>
    </>
  )
}

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<Root />}>
      <Route path="/" element={<Home />} errorElement={<CustomError />} />
      <Route index path="home" element={<Home />} errorElement={<CustomError />} />
      <Route path="login" element={<Login />} errorElement={<CustomError />} />
      <Route path="about" element={<About />} errorElement={<CustomError />} />
      <Route path="contacts" element={<Contacts />} errorElement={<CustomError />} />
      <Route path='products' element={<Products />} errorElement={<CustomError />} />
      <Route path="product/:id" element={<ProductDetails />} errorElement={<CustomError />} />
      <Route path='signup' element={<SignUp />} errorElement={<CustomError />} />
      <Route path="oauth2/redirect" element={<OAuth2RedirectHandler />} errorElement={<CustomError />} />
      <Route path="reset-password" element={<ResetPasswordResquest />} errorElement={<CustomError />} />
      <Route path="user/reset-password" element={<PasswordResetHandler />} errorElement={<CustomError />} />
      <Route path="checkout" element={<Checkout />} errorElement={<CustomError />} />
      <Route path="invalid-token" element={<InvalidToken />} errorElement={<CustomError />} />
      <Route element={<RequireResetToken />}>
        <Route path="user/reset-password-form" element={<PasswordResetForm />} errorElement={<CustomError />} />
      </Route>
      <Route element={<RequireAuth />}>
        <Route element={<RequireAdminRole />}>
          <Route path='product/register' element={<RegisterProduct />} errorElement={<CustomError />} />
          <Route path='product/:id/update' element={<UpdateProduct />} errorElement={<CustomError />} />
          <Route path='dashboard/products' element={<ProductsDashboard />} errorElement={<CustomError />} />
        </Route>
        <Route path='profile' element={<Profile />} errorElement={<CustomError />} />
        <Route path='shopping-cart' element={<ShoppingCart />} errorElement={<CustomError />} />
        <Route path='orders' element={<Orders />} errorElement={<CustomError />} />
        <Route path='order/:id' element={<Order />} errorElement={<CustomError />} />
      </Route>
      <Route path="*" element={<NotFound />} />
    </Route>
  )
)

const App = () => {
  const { theme } = useThemeContext();
  const dispatch = useAppDispatch();
  const user: LoginResponse = JSON.parse(localStorage.getItem("user") || "{}");

  useEffect(() => {
    dispatch(setUser(user));

  }, [])
  return (
    <>
      <ThemeProvider theme={theme}>
        <RouterProvider router={router} />
        <CssBaseline />
        <ToastContainer />
      </ThemeProvider>

    </>
  )
}

export default App
