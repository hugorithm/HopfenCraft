import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider, Link, Outlet } from 'react-router-dom';
import './App.css'
import Album from './pages/Marketplace';
import Login from './pages/Login';
import Home from './pages/Home';
import Navbar from './components/common/Navbar';


const Root = () => {
  return (
    <>
      <div>
        <Navbar/>
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
    </Route>
  )
)

function App() {


  return (
    <>
      <RouterProvider router={router} />
    </>
  )
}


export default App
