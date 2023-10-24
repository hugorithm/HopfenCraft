import { Alert, Avatar, Box, Button, Container, Grid, TextField, Typography, Zoom } from "@mui/material";
import ReCAPTCHA from "react-google-recaptcha";
import Copyright from "../components/Copyright";
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useResetPasswordMutation } from "../app/api/passwordResetApi";
import { PasswordReset } from "../types/password/passwordReset";
import { useThemeContext } from "../theme/ThemeContextProvider";
import { Link as RouterLink } from 'react-router-dom';
import { useEffect, useState } from "react";

const PasswordResetForm = () => {
  const { mode } = useThemeContext();
  const [error, setError] = useState<string | null>(null);
  const [theme, setTheme] = useState<any>(mode);
  const [captachaDone, setCaptchaDone] = useState<boolean>(false);
  
  const [resetPassword, {
    isSuccess,
    isError,
    error: resetPasswordError,
  }] = useResetPasswordMutation();

  useEffect(() => {
    if (isSuccess) {
      localStorage.removeItem("resetToken");
    }
  }, [isSuccess]);

  useEffect(() => {
    if (isError) {
      console.error(resetPasswordError);
      setError("Password reset failed! Please make sure the passwords match and ensure your password meets the following criteria:\n\n" +
        "- At least 8 characters in length.\n" +
        "- Contains at least 1 digit (0-9).\n" +
        "- Contains at least 1 uppercase letter (A-Z).\n" +
        "- Contains at least 1 special character (e.g., !, @, #, $, %, etc.).");
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


  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const body: PasswordReset = {
      newPassword: data.get('newPassword') as string,
      newPasswordConfirmation: data.get('newPasswordConfirmation') as string
    };

    resetPassword(body);
  };

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
                  margin="normal"
                  required
                  fullWidth
                  name="newPassword"
                  label="New Password"
                  type="password"
                  id="newPassword"
                  autoComplete="current-password"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="newPasswordConfirmation"
                  label="Confirm New Password"
                  type="password"
                  id="newPasswordConfirmation"
                  autoComplete="current-password"
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
              Password reset Successful! Please Login.
            </Alert>
            <Button
              variant="contained"
              fullWidth
              sx={{ mt: 2 }}
              component={RouterLink}
              to="/login"
            >
              Login
            </Button>
          </>
        )}
      </Box>
      <Copyright sx={{ mt: 5 }} />
    </Container>
  );
}

export default PasswordResetForm;