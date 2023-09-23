import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Link from '@mui/material/Link';
import { Link as RouterLink, useLoaderData } from 'react-router-dom';
import { Content, Product } from '../types/Product';
import { BASE_URL } from '../config/constants';


export const productDataLoader = async () => {
  try {
    const apiUrl = BASE_URL + '/product/products?page=0&size=15';
    const response = await fetch(apiUrl);

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching data:', error);
    return null;
  }
};



export default function Products() {
  const data = useLoaderData() as Product;


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
              Our Beers
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              Explore the world of beer,
              where craftsmanship meets creativity,
              and every bottle tells a unique story.
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              Our collection showcases a curated selection of brews
              that celebrate the artistry and passion of brewers from
              around the globe. From refreshing lagers to robust
              stouts, each beer is a journey of flavor waiting to be
              savored.
            </Typography>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              Join us in raising a glass to the diversity
              and richness of the beer world—where there's a brew
              for every palate and a story in every sip.
              Cheers to the boundless possibilities of beer!
            </Typography>
          </Container>
        </Box>
        <Container sx={{ py: 8 }} maxWidth="md">

          <Grid container spacing={4}>
            {data.content.map((product: Content) => (

              <Grid item key={product.productId} xs={12} sm={6} md={4}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column'

                  }}
                >
                  <CardMedia
                    component="div"
                    sx={{
                      // 16:9
                      pt: '135.25%',
                    }}
                    image={`https://source.unsplash.com/random?beer`}
                  />
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography>
                      {product.brand}
                    </Typography>
                    <Typography gutterBottom variant="h6" component="h2">
                      {product.name}
                    </Typography>
                    <Typography gutterBottom>
                      {product.description}
                    </Typography>
                    <Typography sx={{ fontWeight: 500 }}>
                      €{product.price}
                    </Typography>
                  </CardContent>
                  <CardActions>
                    <Button size="small" variant='contained'>Add to Cart</Button>
                  </CardActions>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </main>
      {/* Footer */}
      <Box sx={{ bgcolor: 'background.paper', p: 6 }} component="footer">
        <Typography variant="h6" align="center" gutterBottom>
          Connect with Us
        </Typography>
        <Typography
          variant="subtitle1"
          align="center"
          color="text.secondary"
          component="p"
        >
          Explore our world of craft beers and join the conversation.
        </Typography>
        <Typography
          variant="body2"
          align="center"
          color="text.secondary"
          component="p"
        >
          Follow us on{' '}
          <Link
            color="inherit"
            href="https://twitter.com/"
            target="_blank"
          >
            Twitter
          </Link>{' '}
          and{' '}
          <Link
            color="inherit"
            href="https://www.instagram.com/"
            target="_blank"
          >
            Instagram
          </Link>{' '}
          for updates and beer-related content.
        </Typography>
        <Typography
          variant="body2"
          align="center"
          color="text.secondary"
          component="p"
        >
          Copyright © {new Date().getFullYear()}{' '}
          <Link color="inherit" component={RouterLink} to="/home">
            HopfenCraft
          </Link>
          . All rights reserved.
        </Typography>
      </Box>
      {/* End footer */}
    </>
  );
}