import {
  AppBar,
  Button,
  Toolbar,
  Typography,
  Tabs,
  Tab,
  IconButton,
  Badge,
  Tooltip
} from "@mui/material";
import SportsBarIcon from '@mui/icons-material/SportsBar';
import { Link, useLocation, matchPath } from 'react-router-dom';
import NightModeToggle from "./NightModeToggle";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { selectAuth } from "../features/authSlice";
import { useSelector } from "react-redux";
import { selectShoppingCart } from "../features/shoppingCartSlice";
import DropDownMenu from "./DropDownMenu";

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

const Navbar = () => {
  const { jwt } = useSelector(selectAuth);
  //match must be created based on authentication and user roles. This might be a function in the future.
  const routeMatch = jwt ? useRouteMatch(['/home', '/products', '/orders', '/contacts', '/about']) : useRouteMatch(['/home', '/products', '/contacts', '/about']);
  const currentTab = routeMatch?.pattern?.path || false;
  const routeLoginMatch = useRouteMatch(['/login', '/signup']);
  const currentLoginTab = routeLoginMatch?.pattern?.path || false;
  const { cartItems } = useSelector(selectShoppingCart);

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
          {jwt && (
            <Tab label="My Orders" value="/orders" to="/orders" component={Link} />
          )}
          <Tab label="Contacts" value="/contacts" to="/contacts" component={Link} />
          <Tab label="About" value="/about" to="/about" component={Link} />
        </Tabs>
        <NightModeToggle />
        {jwt && (
          <>
            <Tooltip title="Shopping Cart">
              <IconButton to="/shopping-cart" component={Link} color="inherit">
                <Badge color="error" badgeContent={cartItems.length}>
                  <ShoppingCartIcon />
                </Badge>
              </IconButton>
            </Tooltip>
            <DropDownMenu />
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
