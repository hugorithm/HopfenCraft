import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { Link as RouterLink } from 'react-router-dom';
import { SignUpRequestBody } from '../types/auth/SignUpRequestBody';
import { useSignUpMutation } from '../app/api/auth/authApi';
import { useEffect, useState } from 'react';
import { Alert, Zoom } from '@mui/material';
import { Theme as ToastifyTheme, toast } from 'react-toastify';
import { useThemeContext } from '../theme/ThemeContextProvider';
import ReCAPTCHA from "react-google-recaptcha";
import Copyright from '../components/Copyright';


const SignUp = () => {
  const [error, setError] = useState<string | null>(null);
  const { mode } = useThemeContext();
  const [theme, setTheme] = useState<any>(mode);

  const [signUp,
    { 
      isSuccess,
      isError,
      error: signUpError
    }
  ] = useSignUpMutation();

  useEffect(() => {
    if (isSuccess) {
      toast.success('Sign up Successful!', {
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        pauseOnFocusLoss: false,
        progress: undefined,
        theme: mode as ToastifyTheme,
      });
    }
  }, [isSuccess])

  useEffect(() => {
    if (isError) {
      console.error(signUpError);
      setError("Signup failed. Please ensure your password meets the following criteria:\n\n" +
        "- At least 8 characters in length.\n" +
        "- Contains at least 1 digit (0-9).\n" +
        "- Contains at least 1 uppercase letter (A-Z).\n" +
        "- Contains at least 1 special character (e.g., !, @, #, $, %, etc.).");
    }
  }, [isError]);
  

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const body: SignUpRequestBody = {
      username: data.get('username') as string,
      password: data.get('password') as string,
      email: data.get('email') as string,
      firstName: data.get('firstName') as string,
      lastName: data.get('lastName') as string
    };

    signUp(body);
  };

  const [captachaDone, setCaptchaDone] = useState<boolean>(false);

  const onCaptchaChange = () => {
    setCaptchaDone(true);
  }

  const onCaptchaError = () => {
    setCaptchaDone(false);
  }

  useEffect(() => {
    setTheme(mode === 'light' ? 'light' : 'dark');
  }, [mode]);

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign up
        </Typography>
        {!isSuccess && (
          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="given-name"
                  name="firstName"
                  required
                  fullWidth
                  id="firstName"
                  label="First Name"
                  autoFocus
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  autoComplete="family-name"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="username"
                  label="Username"
                  name="username"
                  autoComplete="username"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                />
              </Grid>
              <Grid item xs={12}>
                <FormControlLabel
                  control={<Checkbox value="allowExtraEmails" color="primary" />}
                  label="I want to receive inspiration, marketing promotions and updates via email."
                />
              </Grid>
            </Grid>
            <Box mt={2} display="flex" justifyContent="center"  key={theme}>
              <ReCAPTCHA
                sitekey={import.meta.env.VITE_GOOGLE_RECAPTCHA_KEY}
                onChange={onCaptchaChange}
                onError={onCaptchaError}
                theme={theme}
              />
            </Box>
            <Button
              disabled={!captachaDone}
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            {error && (
              <Zoom in={!!error}>
                <Alert severity="error" sx={{
                  whiteSpace: 'pre-line'
                }}>
                  {error}
                </Alert>
              </Zoom>
            )}
          </Box>
        )}
        {isSuccess && (
          <>
            <Alert severity="success" sx={{ mt: 3 }}>
              Sign up Successful! Click below to login.
            </Alert>
            <Button
              variant="contained"
              fullWidth
              sx={{ mt: 2 }}
              component={RouterLink}
              to="/login"
            >
              Go to Login
            </Button>
          </>
        )}
        <Grid container justifyContent="flex-end">
          <Grid item>
            <Link variant="body2" component={RouterLink} to="/login">
              Already have an account? Sign in
            </Link>
          </Grid>
        </Grid>
      </Box>
      <Copyright sx={{ mt: 5 }} />
    </Container>
  );
}

export default SignUp;
