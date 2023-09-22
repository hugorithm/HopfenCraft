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
      <Route index path="home" element={<Home />} />
      <Route path="login" element={<Login />} />
      <Route path="about" element={<About />} />
      <Route path="contacts" element={<Contacts />} />
      <Route path='products' element={<Products />} />
      <Route path='signup' element={<SignUp />} />
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
