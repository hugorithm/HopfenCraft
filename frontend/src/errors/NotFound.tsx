import React from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Link as RouterLink } from 'react-router-dom';

const NotFound = () => {
  return (
    <>
      <Container maxWidth="sm">
        <Box
          sx={{
            mt: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Typography component="h1" variant="h2" align="center" color="text.primary" gutterBottom>
            🤖 Oops! Page Not Found
          </Typography>
          <Typography variant="h5" align="center" color="text.secondary" paragraph>
            It seems you've ventured into uncharted territory. The page you are looking for might have been moved or doesn't exist. 😭
          </Typography>
          <img
            src="/src/assets/errors/travolta.gif"
            alt="404 Not Found"
            height="300"
            style={{ marginBottom: '16px' }}
          />
          <Button
            variant="contained"
            color="primary"
            component={RouterLink}
            to="/home"
          >
            Go Back Home
          </Button>
        </Box>
      </Container>
    </>
  );
}

export default NotFound;
