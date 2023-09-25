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
import Products, { productDataLoader } from './pages/Products';
import SignUp from './pages/Signup';
import { ThemeProvider } from "@mui/material";
import { useThemeContext } from "./theme/ThemeContextProvider";
import CustomError from './errors/CustomError';
import NotFound from './errors/404LandingPage';
import Profile from './pages/Profile';
import PrivateRoutes from './route/PrivateRoutes';


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
      <Route path='products' element={<Products />} loader={productDataLoader} errorElement={<CustomError />} />
      <Route path='signup' element={<SignUp />} errorElement={<CustomError />} />
      <Route element={<PrivateRoutes/>}>
        <Route path='profile' element={<Profile />} errorElement={<CustomError/>}/>
      </Route>
      <Route path="*" element={<NotFound />} />
    </Route>
  )
)

function App() {
  const { theme } = useThemeContext();

  return (
    <>
      <ThemeProvider theme={theme}>
        <RouterProvider router={router} />
      </ThemeProvider>
    </>
  )
}

export default App
