import React from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Link as RouterLink } from 'react-router-dom';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

const InvalidToken = () => {
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
          <ErrorOutlineIcon
            color="error"
            sx={{ fontSize: 80, marginBottom: '16px' }}
          />
          <Typography variant="h4" color="error" gutterBottom>
           Invalid Token!
          </Typography>
          <Typography variant="body1" paragraph>
            The token you provided is invalid.
          </Typography>
          <Button
              variant="contained"
              sx={{ mt: 2 }}
              component={RouterLink}
              to="/home"
            >
              Go home
            </Button>
        </Box>
      </Container>
    </>
  );
}

export default InvalidToken;
