
import {
  Typography,
  Stack,
  Box,
  CssBaseline,
  Button,
  Container,
  CardMedia
}
  from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import React, { useState, useEffect } from 'react';

const cardMediaStyle = {
  height: '100vh',
  transition: 'transform 0.3s ease-in-out',
};

export default function Home() {
  const [cardMediaTransform, setCardMediaTransform] = useState('scale(1)');

  const handleResize = () => {
    const windowWidth = window.innerWidth;
    
    if (windowWidth < 768) {
      setCardMediaTransform('scale(1.2)');
    } else {
      setCardMediaTransform('scale(1)');
    }
  };

  // Add an event listener to handle resizing
  useEffect(() => {
    handleResize(); // Initial call
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <>
      <CssBaseline />
      <Box sx={{ position: 'relative', height: '100vh', overflow: 'hidden' }}>
        <CardMedia
          component="video"
          src="/src/assets/video/beer_pour.webm"
          muted
          autoPlay
          style={{
            ...cardMediaStyle,
            transform: cardMediaTransform,
            objectFit: 'cover',
          }}
        />
        <Box
          sx={{
            position: 'absolute',
            top: 0,
            width: '100%',
            height: '100%',
            backgroundColor: '#00000061',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            pt: 8,
            pb: 6,
          }}
        >
          <Container maxWidth="sm">
            <Typography
              sx={{
                textShadow: '1px 1px 2px black',
              }}
              component="h1"
              variant="h2"
              align="center"
              color="#fff"
              gutterBottom
            >
              Welcome to
              <Typography
                sx={{
                  textShadow: '1px 1px 2px black',
                }}
                component="p"
                variant="h2"
                align="center"
                color="#fff"
                fontWeight={600}
                gutterBottom
              >
                HopfenCraft
              </Typography>
            </Typography>
            <Typography
              sx={{
                textShadow: '1px 1px 2px black',
              }}
              variant="h5"
              align="center"
              color="#fff"
              paragraph
            >
              Where beer aficionados find their liquid dreams come true. Our story is one of passion, craftsmanship, and a relentless pursuit of the perfect brew.
            </Typography>
            <Typography
              sx={{
                textShadow: '1px 1px 2px black',
              }}
              variant="h5"
              align="center"
              color="#fff"
              paragraph
            >
              Join us in celebrating the magic of beer – it's not just a drink; it's a way of life. HopfenCraft, where the best beer finds its true connoisseurs. Cheers to the artistry of beer!
            </Typography>

            <Stack
              sx={{
                pt: 4,
                display: 'flex',
                justifyContent: 'center',
              }}
              direction="row"
              spacing={2}
            >
              <Button component={RouterLink} to="/products" variant="contained">
                Check our Products
              </Button>
              <Button component={RouterLink} to="/signup" variant="contained">
                Sign Up
              </Button>
            </Stack>
          </Container>
        </Box>
      </Box>
      <CssBaseline />
    </>
  );
}