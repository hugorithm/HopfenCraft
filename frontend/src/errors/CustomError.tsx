import React from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { CssBaseline } from '@mui/material';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

const CustomError = () => {
  return (
    <>
      <CssBaseline />
      <Container maxWidth="sm">
        <Box
          sx={{
            mt: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <ErrorOutlineIcon
            color="error"
            sx={{ fontSize: 80, marginBottom: '16px' }}
          />
          <Typography variant="h4" color="error" gutterBottom>
            Oops! Something went wrong.
          </Typography>
          <Typography variant="body1" paragraph>
            We apologize, but an error occurred while loading the page.
          </Typography>
          <Typography variant="body1" paragraph>
            Please try again later or contact support if the issue persists.
          </Typography>
          <img
            src="/src/assets/errors/oops.gif"
            alt="404 Not Found"
            height="300"
            style={{ marginBottom: '16px' }}
          />
          <Button
            variant="contained"
            color="primary"
            onClick={() => window.location.reload()}
          >
            Refresh Page
          </Button>
        </Box>
      </Container>
    </>
  );
}

export default CustomError;
