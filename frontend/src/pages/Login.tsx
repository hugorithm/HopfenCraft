import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { Link as RouterLink, Navigate } from 'react-router-dom';
import googleLogo from './../assets/oauth2/google-logo.png';
import githubLogo from './../assets/oauth2/github-logo.png';
import { GOOGLE_AUTH_URL, GITHUB_AUTH_URL, ACCESS_TOKEN, BASE_URL } from './../config/constants';
import { Divider } from '@mui/material';
import { LoginRequestBody } from '../types/LoginRequestBody';
import { LoginResponse } from './../types/LoginResponse'


export default function Login() {
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const body = {
      username: data.get('username') as string,
      password: data.get('password') as string
    }
    login(body);
  };

  const login = async (body: LoginRequestBody) => {
    try {
      const apiUrl = BASE_URL + '/auth/login';

      const response = await fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
      });

      if (!response.ok) {
        throw new Error('Failed to login');
      }

      const data : LoginResponse = await response.json();
      if (data) {
        localStorage.setItem(ACCESS_TOKEN, data.jwt);   
      } else {
        throw new Error('Failed to login');
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  return (
    <Grid container component="main" sx={{ height: '93.35vh' }}>
      <CssBaseline />
      <Grid
        item
        xs={false}
        sm={4}
        md={7}
        sx={{
          backgroundImage: 'url(/src/assets/login_beer2.jpg)',
          backgroundRepeat: 'no-repeat',
          backgroundColor: (t) =>
            t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1, maxWidth: '400px', width: '100%', margin: '0 auto' }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Divider sx={{ width: '100%' }}>OR</Divider>
            <Button
              href={GOOGLE_AUTH_URL}
              fullWidth
              variant="outlined"
              color="primary"
              sx={{ mt: 2, height: 44 }}
              startIcon={<img src={googleLogo} alt="Google" height={32} />}
            >
              Sign in with Google
            </Button>
            <Button
              href={GITHUB_AUTH_URL}
              fullWidth
              variant="outlined"
              color="primary"
              sx={{ mt: 2, mb: 5, height: 44 }}
              startIcon={<img src={githubLogo} alt="GitHub" height={25} />}
            >
              Sign in with GitHub
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="#" variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link variant="body2" component={RouterLink} to="/signup">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Grid>
    </Grid>
  );
}
