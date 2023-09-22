import {
  AppBar,
  Button,
  Stack,
  Toolbar,
  Typography
} from "@mui/material";
import SportsBarIcon from '@mui/icons-material/SportsBar';
import { Link } from 'react-router-dom';
import NightModeToggle from "./NightModeToggle";

function Navbar() {

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
        <Stack direction='row' spacing={2} sx={{ marginLeft: 'auto' }}>
          <Button color="inherit" component={Link} to="/products">
            <Typography variant="button">
              Products
            </Typography>
          </Button>
          <Button color="inherit" component={Link} to="/contacts">
            Contacts
          </Button>
          <Button color="inherit" component={Link} to="/about">
            About
          </Button>
        </Stack>
        <NightModeToggle />
        <Button sx={{ marginLeft: '10px' }} color="inherit" component={Link} to="/login">
          Login
        </Button>
        <Button sx={{ marginLeft: "10px" }} color="inherit" component={Link} to="/signup">
          Sign Up
        </Button>
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
