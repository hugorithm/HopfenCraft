import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import { Link as RouterLink } from 'react-router-dom';
import { Fade } from '@mui/material';

function About() {
  const handleLearnMoreClick = () => {
    console.log("Learn More clicked!");
  };

  return (
    <>
      <Fade in={true} timeout={1000}>
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
              Discover the World of HopfenCraft
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              Welcome to HopfenCraft, where the art of brewing meets the thrill of discovery. We're not just a beer shop; we're a passionate community of beer enthusiasts on a mission to share the world's finest brews with you.
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              Our shelves are a treasure trove of flavors, each bottle a testament to the craft and dedication of master brewers worldwide. From the hoppy delights of the Pacific Northwest to the velvety stouts of Ireland, every sip is a journey.
            </Typography>
            <Stack
              sx={{ pt: 4 }}
              direction="row"
              spacing={2}
              justifyContent="center"
            >
              <Button
                variant="contained"
                color="primary"
                component={RouterLink}
                to="/products"
              >
                Explore Our Products
              </Button>
              <Button
                variant="outlined"
                onClick={handleLearnMoreClick}
              >
                Learn More
              </Button>
            </Stack>
          </Box>
        </Container>
      </Fade>
    </>
  );
}

export default About;
