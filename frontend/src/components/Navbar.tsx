import {
  AppBar,
  Button,
  Stack,
  Toolbar,
  Typography,
  Tabs,
  Tab,
  IconButton
} from "@mui/material";
import SportsBarIcon from '@mui/icons-material/SportsBar';
import { Link, useLocation, matchPath } from 'react-router-dom';
import NightModeToggle from "./NightModeToggle";

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

  return (
    <AppBar position="static">
      <Toolbar>
        <Button color='inherit' component={Link} to="/home">
          <SportsBarIcon sx={{ marginRight: "10px" }} />
          <Typography
            variant="h6"
            component="div"
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
        {/* <Button
          sx={{ marginLeft: '10px' }}
          color="inherit"
          component={Link} to="/login">
          Login
        </Button>
        <Button
          sx={{ marginLeft: '10px' }}
          color="inherit"
          component={Link} to="/signup">
          Sign Up
        </Button> */}
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
