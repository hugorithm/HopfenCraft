import React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { CssBaseline } from '@mui/material';

const CustomError = () => {
  return (
    <>
    <CssBaseline />
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      height="100vh"
    >
      <Typography variant="h4" color="error" gutterBottom>
        Oops! Something went wrong.
      </Typography>
      <Typography variant="body1" paragraph>
        We apologize, but an error occurred while loading the page.
      </Typography>
      <Typography variant="body1" paragraph>
        Please try again later or contact support if the issue persists.
      </Typography>
      <Button variant="contained" color="primary" onClick={() => window.location.reload()}>
        Refresh Page
      </Button>
    </Box>
    
    </>
  );
}

export default CustomError;
