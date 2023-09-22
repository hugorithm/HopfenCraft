
import {
  Link,
  Typography,
  Stack,
  Box,
  Grid,
  CssBaseline,
  Button,
  Container,
  Paper,
  CardMedia
}
  from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

function Copyright() {
  return (
    <Typography variant="body2" color="text.secondary" align="center">
      {'Copyright © '}
      <Link color="inherit" component={RouterLink} to="/home">
        HopfenCraft
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

export default function Home() {
  return (
    <>

      <CssBaseline />
      <Box>
        <CardMedia
          component='video'
          image={"/src/assets/video/beer_pour.webm"}
          loop
          muted
          autoPlay>
        </CardMedia>
        <Box
          sx={{
            my: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            position: 'absolute',
            top: 0,
            width: '100%',
            height: '100vh',
            backgroundColor: '#00000061'
          }}
        >
          <Box
            sx={{
              // bgcolor: 'background.paper',
              pt: 8,
              pb: 6,
            }}
          >
            <Container maxWidth="sm">
              <Typography
                sx={{
                  textShadow: '1px 1px 2px black'
                }}
                component="h1"
                variant="h2"
                align="center"
                // color="text.primary"
                color="#fff"
                gutterBottom
              >
                Welcome to
                <Typography
                  sx={{
                    textShadow: '1px 1px 2px black'
                  }}
                  component="p"
                  variant="h2"
                  align="center"
                  // color="text.primary"
                  color="#fff"
                  fontWeight={600}
                  gutterBottom>
                  HopfenCraft
                </Typography>
              </Typography>
              <Typography
                sx={{
                  textShadow: '1px 1px 2px black'
                }}
                variant="h5"
                align="center"
                // color="text.secondary" 
                color="#fff"
                paragraph>
                Where beer aficionados find their liquid dreams come true. Our story is one of passion, craftsmanship, and a relentless pursuit of the perfect brew.
              </Typography>
              <Typography
                sx={{
                  textShadow: '1px 1px 2px black'
                }}
                variant="h5"
                align="center"
                // color="text.secondary" 
                color="#fff"
                paragraph>
                Join us in celebrating the magic of beer – it's not just a drink; it's a way of life. HopfenCraft, where the best beer finds its true connoisseurs. Cheers to the artistry of beer!
              </Typography>

              <Stack
                sx={{ pt: 4 }}
                direction="row"
                spacing={2}
                justifyContent="center"
              >
                <Button component={RouterLink} to="/products" variant="contained">Check our Products</Button>
                <Button component={RouterLink} to="/signup" variant="outlined">Sign Up</Button>
              </Stack>
            </Container>
          </Box>
        </Box>
      </Box>
      <CssBaseline />
    </>
  );
}