import {
  AppBar,
  Button,
  Toolbar,
  Typography,
  Tabs,
  Tab,
  IconButton,
  Badge
} from "@mui/material";
import SportsBarIcon from '@mui/icons-material/SportsBar';
import { Link, useLocation, matchPath } from 'react-router-dom';
import NightModeToggle from "./NightModeToggle";
import { ACCESS_TOKEN } from "../config/constants";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useNavigate } from "react-router-dom";
import { logout, selectAuth } from "../features/authSlice";
import { useSelector } from "react-redux";
import { useAppDispatch } from "../app/hooks";
import { toast } from "react-toastify";
import { useThemeContext } from "../theme/ThemeContextProvider";
import { useState } from "react";

function useRouteMatch(patterns: readonly string[]) {
  const { pathname } = useLocation();

  for (let i = 0; i < patterns.length; i += 1) {
    const pattern = patterns[i];
    const possibleMatch = matchPath(pattern, pathname);
    if (possibleMatch !== null) {
      return possibleMatch;
    }
  }

  return null;
}

function Navbar() {
  const routeMatch = useRouteMatch(['/home', '/products', '/contacts', '/about']);
  const currentTab = routeMatch?.pattern?.path || false;
  const routeLoginMatch = useRouteMatch(['/login', '/signup']);
  const currentLoginTab = routeLoginMatch?.pattern?.path || false;
  const { jwt } = useSelector(selectAuth);
  const { mode } = useThemeContext();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [shoppingCartCounter, setShoppingCartCounter] = useState(0);

  const handleLogout = () => {
    dispatch(logout());

    toast.success('Logout Successful', {
      position: "top-right",
      autoClose: 2000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: true,
      pauseOnFocusLoss: false,
      progress: undefined,
      theme: mode === 'light' ? 'light' : 'dark',
    });

    navigate("/home");
  }

  return (
    <AppBar position="static">
      <Toolbar>
        <Button color='inherit' component={Link} to="/home">
          <SportsBarIcon sx={{ marginRight: "10px" }} />
          <Typography
            variant="h6"
            component="div"
            fontWeight={600}
            textTransform={"capitalize"}
            sx={{ flexGrow: 1 }}>
            HopfenCraft
          </Typography>
        </Button>
        <Tabs
          value={currentTab}
          TabIndicatorProps={{
            style: {
              background: "#fff"
            }
          }}
          sx={{ marginLeft: 'auto' }}>
          <Tab label="Home" value="/home" to="/home" component={Link} />
          <Tab label="Products" value="/products" to="/products" component={Link} />
          <Tab label="Contacts" value="/contacts" to="/contacts" component={Link} />
          <Tab label="About" value="/about" to="/about" component={Link} />
        </Tabs>
        <NightModeToggle />
        {jwt && (
          <>
            <IconButton to="/profile" component={Link} color="inherit">
              <AccountCircleIcon />
            </IconButton>
            <IconButton to="/cart" component={Link} color="inherit">
              <Badge color="error" badgeContent={shoppingCartCounter}>
                <ShoppingCartIcon />
              </Badge>
            </IconButton>
            <Button color="inherit" onClick={handleLogout}>Logout</Button>
          </>
        )}
        {!jwt && (
          <Tabs
            value={currentLoginTab}
            sx={{ marginLeft: '10px' }}
            TabIndicatorProps={{
              style: {
                background: "#fff"
              }
            }}>
            <Tab label="Login" value="/login" to="/login" component={Link} />
            <Tab label="Sign Up" value="/signup" to="/signup" component={Link} />
          </Tabs>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
