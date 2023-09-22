import {
  AppBar,
  Button,
  Stack,
  Toolbar,
  Typography,
  Tabs,
  Tab
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
  const routeMatch = useRouteMatch(['/products', '/contacts', '/about', '/login', '/signup']);
  const currentTab = routeMatch?.pattern?.path;

  return (
    <AppBar position="static">
      <Toolbar>
        <SportsBarIcon />
        <Button color='inherit' href="/home">
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
          <Tab label="Products" value="/products" to="/products" component={Link} />
          <Tab label="Contacts" value="/contacts" to="/contacts" component={Link} />
          <Tab label="About" value="/about" to="/about" component={Link} />
        </Tabs>
        <NightModeToggle />
        <Tabs
          value={currentTab}
          sx={{ marginLeft: '10px' }}
          TabIndicatorProps={{
            style: {
              background: "#fff"
            }
          }}>
          <Tab label="Login" value="/login" to="/login" component={Link} />
          <Tab label="SignUp" value="/signup" to="/signup" component={Link} />
        </Tabs>
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
