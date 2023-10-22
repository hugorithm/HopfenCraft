import * as React from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import { Avatar, Box, Divider, IconButton, ListItemIcon, Tooltip } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useThemeContext } from '../theme/ThemeContextProvider';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { logout, selectAuth } from '../features/authSlice';
import { toast } from 'react-toastify';
import { Add, Logout, Settings, Dashboard } from '@mui/icons-material';
import { useSelector } from 'react-redux';

const DropDownMenu = () => {
  const { mode } = useThemeContext();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { username } = useAppSelector(selectAuth);
  const { jwt } = useSelector(selectAuth);
  const roles: string[] = JSON.parse(localStorage.getItem("user") || "{}").roles;

  const generateCapitals = (): string => {
    if (!username) return "";
    const names = username.split(" ");
    if (names.length >= 2) {
      return names[0][0].toUpperCase() + names[1][0].toLocaleUpperCase();
    }

    return username[0].toUpperCase();
  }

  const handleLogout = () => {
    setAnchorEl(null);
    dispatch(logout());

    toast.success('Logout Successful!', {
      position: "top-right",
      autoClose: 1000,
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
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };
  return (
    <>
      <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center' }}>
        <Tooltip title="Account Settings">
          <IconButton
            onClick={handleClick}
            size="small"
            sx={{ ml: 2 }}
            aria-controls={open ? 'account-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={open ? 'true' : undefined}
          >
            <Avatar sx={{ width: 32, height: 32 }}>{generateCapitals()}</Avatar>
          </IconButton>
        </Tooltip>
      </Box>
      <Menu
        anchorEl={anchorEl}
        id="account-menu"
        open={open}
        onClose={handleClose}
        onClick={handleClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: 'visible',
            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
            mt: 1.5,
            '& .MuiAvatar-root': {
              width: 32,
              height: 32,
              ml: -0.5,
              mr: 1,
            },
            '&:before': {
              content: '""',
              display: 'block',
              position: 'absolute',
              top: 0,
              right: 14,
              width: 10,
              height: 10,
              bgcolor: 'background.paper',
              transform: 'translateY(-50%) rotate(45deg)',
              zIndex: 0,
            },
          },
        }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        <MenuItem component={Link} to="/profile">
          <Avatar /> Profile
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleClose}>
          <ListItemIcon>
            <Settings fontSize="small" />
          </ListItemIcon>
          Settings
        </MenuItem>
        {jwt && roles.includes("ADMIN") && (
          <Box>
            <MenuItem component={Link} to="/product/register">
              <ListItemIcon>
                <Add fontSize="small" />
              </ListItemIcon>
              Register a Product
            </MenuItem>
            <MenuItem component={Link} to="/dashboard/products">
              <ListItemIcon>
                <Dashboard fontSize="small" />
              </ListItemIcon>
              Products Dashboard
            </MenuItem>
          </Box>
        )}
        <MenuItem onClick={handleLogout}>
          <ListItemIcon>
            <Logout fontSize="small" />
          </ListItemIcon>
          Logout
        </MenuItem>
      </Menu>
    </>
  );
}

export default DropDownMenu;