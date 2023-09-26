import * as React from 'react';
import { useEffect, useState } from 'react';
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
import { Link as RouterLink, Navigate, useNavigate } from 'react-router-dom';
import googleLogo from '../assets/oauth2/google-logo.png';
import githubLogo from '../assets/oauth2/github-logo.png';
import { GOOGLE_AUTH_URL, GITHUB_AUTH_URL } from '../config/constants';
import { Alert, Divider } from '@mui/material';
import { LoginRequestBody } from '../types/LoginRequestBody';
import { useLoginUserMutation } from '../auth/authApi';
import { useAppDispatch } from '../app/hooks';
import { setUser } from '../features/authSlice';
import { toast } from 'react-toastify';
import { useThemeContext } from '../theme/ThemeContextProvider';


export default function Login() {
  const [error, setError] = useState<string | null>(null);
  const [rememberMe, setRememberMe] = useState(false);
  const [username, setUsername] = useState('');
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { mode } = useThemeContext();

  const [loginUser,
    { data: loginData,
      isSuccess: isLoginSuccess,
      isError: isLoginError,
      error: loginError
    },
  ] = useLoginUserMutation();


  useEffect(() => {
    if (isLoginSuccess) {
      dispatch(setUser({ username: loginData!.username, email: loginData!.email, jwt: loginData!.jwt }));

      toast.success('Login Successful', {
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: false,
        pauseOnFocusLoss: false,
        progress: undefined,
        theme: mode === 'light' ? 'light' : 'dark',
      });

      navigate("/profile");
    } 
  }, [isLoginSuccess]);

  useEffect(() => {
    if (isLoginError) {
      setError("Failed to Login. Please check your credentials")
    }
  }, [isLoginError])

  useEffect(() => {
    const rememberMeValue = localStorage.getItem('rememberMe');
    const savedUsername = localStorage.getItem('savedUsername');

    if (rememberMeValue === "true" && savedUsername) {
      setRememberMe(true);
      setUsername(savedUsername);
    } else {
      setRememberMe(false);
      setUsername('');
    }
  }, []);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const body: LoginRequestBody = {
      username: data.get('username') as string,
      password: data.get('password') as string,
    };

    if (rememberMe) {
      localStorage.setItem('rememberMe', 'true');
      localStorage.setItem('savedUsername', username);
    } else {
      localStorage.removeItem('rememberMe');
      localStorage.removeItem('savedUsername');
    }

    loginUser(body);
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
              value={username}
              onChange={(e) => setUsername(e.target.value)}
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
              control={<Checkbox
                value="remember"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                color="primary" />}
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
            {error && (
              <Alert severity="error" >
                {error}
              </Alert>

            )}
            <Divider sx={{ width: '100%', mt: 2 }}>OR</Divider>
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
