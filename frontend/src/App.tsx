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
import { ThemeProvider } from "@mui/material";
import { useThemeContext } from "./theme/ThemeContextProvider";
import CustomError from './errors/CustomError';
import NotFound from './errors/404LandingPage';
import Profile from './pages/Profile';
import RequireAuth from './route/RequireAuth';
import { useAppDispatch } from './app/hooks';
import { useEffect } from 'react';
import { setUser } from './features/authSlice';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { LoginResponse } from './types/LoginResponse';
import { useGetShoppingCartMutation } from './app/api/shoppingCartApi';
import { selectShoppingCart, setCartItems } from './features/shoppingCartSlice';
import { useSelector } from 'react-redux';

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
      <Route path='signup' element={<SignUp />} errorElement={<CustomError />} />
      <Route element={<RequireAuth />}>
        <Route path='profile' element={<Profile />} errorElement={<CustomError />} />
      </Route>
      <Route path="*" element={<NotFound />} />
    </Route>
  )
)

const App = () => {
  const { theme } = useThemeContext();
  const dispatch = useAppDispatch();
  const user: LoginResponse = JSON.parse(localStorage.getItem("user") || "{}");
  const localJwt = JSON.parse(localStorage.getItem("user") || "{}").jwt;
  const { cartItems } = useSelector(selectShoppingCart);

  const [getShoppingCart,
    { data: shoppingCartData,
      isSuccess: isShoppingCartSuccess,
      isError: isShoppingCartError,
      error: shoppingCartError
    },
  ] = useGetShoppingCartMutation();

  useEffect(() => {
    if (isShoppingCartSuccess && shoppingCartData) {
      dispatch(setCartItems({ cartItems: shoppingCartData.cartItems }));
    }
  }, [isShoppingCartSuccess])

  useEffect(() => {
    dispatch(setUser(user));

    if (localJwt && cartItems.length === 0) {
      getShoppingCart();
    }
  }, [])
  return (
    <>
      <ThemeProvider theme={theme}>
        <RouterProvider router={router} />
        <ToastContainer />
      </ThemeProvider>

    </>
  )
}

export default App
