import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Link from '@mui/material/Link';
import {Link as RouterLink} from 'react-router-dom';

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

export default function About() {
  return (
    <>
      <CssBaseline />
      <main>
        {/* Hero unit */}
        <Box
          sx={{
            bgcolor: 'background.paper',
            pt: 8,
            pb: 6,
          }}
        >
          <Container maxWidth="sm">
            <Typography
              component="h1"
              variant="h2"
              align="center"
              color="text.primary"
              gutterBottom
            >
              About 
              <Typography
               component="h1"
               variant="h2"
               align="center"
               color="text.primary"
               fontWeight={600}
               gutterBottom>
                  HopfenCraft
              </Typography>
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
            Here, you'll discover an exquisite collection of handpicked brews that span the globe, each bottle telling a tale of the brewmaster's dedication and expertise. From the hoppy IPAs of the Pacific Northwest to the rich stouts of Ireland, our shelves house the world's finest.
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
            Whether you're a seasoned beer enthusiast or a curious newcomer, our goal is simple: to elevate your beer experience. We invite you to explore our treasure trove of flavors, embark on a journey of taste, and raise a glass to the finest brews the world has to offer.
            </Typography>
            
            <Stack
              sx={{ pt: 4 }}
              direction="row"
              spacing={2}
              justifyContent="center"
            >
              <Button variant="contained">Main call to action</Button>
              <Button variant="outlined">Secondary action</Button>
            </Stack>
          </Container>
        </Box>
      
      </main>
      {/* Footer */}
      <Box sx={{ bgcolor: 'background.paper', p: 6 }} component="footer">
        <Typography variant="h6" align="center" gutterBottom>
          Footer
        </Typography>
        <Typography
          variant="subtitle1"
          align="center"
          color="text.secondary"
          component="p"
        >
          Something here to give the footer a purpose!
        </Typography>
        <Copyright />
      </Box>
      {/* End footer */}
    </>
  );
}