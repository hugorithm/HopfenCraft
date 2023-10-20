import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Link from '@mui/material/Link';
import { Link as RouterLink } from 'react-router-dom';
import { Fade } from '@mui/material';

function Contacts() {
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
              Contact Us
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              We'd love to hear from you! If you have any questions, feedback, or inquiries, please feel free to reach out to us.
            </Typography>
            {/* Add your contact information here */}
            <Typography variant="body1" align="center" color="text.secondary" paragraph>
              Email: <Link color="primary" href="mailto:contact@hopfencraft.com">contact@hopfencraft.com</Link>
            </Typography>
            <Typography variant="body1" align="center" color="text.secondary" paragraph>
              Phone: <Link color="primary" href="tel:+1234567890">+351 939203429</Link>
            </Typography>
            <Typography variant="body1" align="center" color="text.secondary" paragraph>
              Address: Avenida da Liberdade, Braga, Portugal
            </Typography>
            {/* End of contact information */}
            <Button component={RouterLink} to="/home" variant="contained" color="primary">
              Back to Home
            </Button>
          </Box>
        </Container>
      </Fade>
    </>
  );
}

export default Contacts;
