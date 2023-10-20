import { Alert, Avatar, Box, Button, Container, CssBaseline, Grid, Link, TextField, Typography, Zoom } from "@mui/material";
import React, { useEffect, useState } from "react";
import ReCAPTCHA from "react-google-recaptcha";
import { PasswordResetRequest } from "../types/password/passwordResetRequest";
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useResetPasswordRequestMutation } from "../app/api/passwordResetApi";
import { Link as RouterLink } from 'react-router-dom';
import { useThemeContext } from '../theme/ThemeContextProvider';
import Copyright from "../components/Copyright";

const ResetPasswordResquest = () => {
  const [captachaDone, setCaptchaDone] = useState<boolean>(false);
  const { mode } = useThemeContext();
  const [theme, setTheme] = useState<any>(mode);
  const [error, setError] = useState<string | null>(null);

  const [resetPasswordRequest, {
    isSuccess,
    isError,
    error: resetPasswordError,
  }] = useResetPasswordRequestMutation();

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const body: PasswordResetRequest = {
      username: data.get('username') as string
    };

    resetPasswordRequest(body);
  };

  useEffect(() => {
    if (isError) {
      setError("There was an error while processing your request:\n\n" +
        "- Please make sure the username you provided is right\n");

      console.error(resetPasswordError);
    }
  }, [isError]);


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
          Reset Password
        </Typography>
        {!isSuccess && (
          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
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
            </Grid>
            <Box mt={2} display="flex" justifyContent="center" key={theme}>
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
              Reset Password
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
              We've sent you an email with more instructions! Please check your email.
            </Alert>
            <Button
              variant="contained"
              fullWidth
              sx={{ mt: 2 }}
              component={RouterLink}
              to="/home"
            >
              Go home
            </Button>
          </>
        )}
      </Box>
      <Copyright sx={{ mt: 5 }} />
    </Container>
  );
}

export default ResetPasswordResquest;
