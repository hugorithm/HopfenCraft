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
import Modal from '@mui/material/Modal';
import { Link as RouterLink, useLoaderData } from 'react-router-dom';
import { Content, Product } from '../types/Product';
import { BASE_URL } from '../config/constants';
import { ButtonBase } from '@mui/material';
import { useState } from 'react';


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
  const initialData = useLoaderData() as Product;
  const [data, setData] = useState<Product>(initialData);
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [selectedProduct, setSelectedProduct] = useState<Content | null>(null);
  const [page, setPage] = useState<number>(0);

  const loadMore = async () => {
    try {
      const nextPage = page + 1;
      const apiUrl = BASE_URL + `/product/products?page=${nextPage}&size=15`;
      const response = await fetch(apiUrl);

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const newData = await response.json();
      // Update the data state with the new items
      setData((prevData: Product) => ({
        ...prevData,
        content: [...prevData.content, ...newData.content],
        last: newData.last,
      }));
      setPage(nextPage);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleImageClick = (product: Content) => {
    setSelectedProduct(product);
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedProduct(null);
  };

  return (
    <>
      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', bgcolor: 'background.paper', boxShadow: 24, p: 4, maxWidth: '80vw' }}>
          {/* Image content goes here */}
          <img src={`${BASE_URL}/product/${selectedProduct ? selectedProduct.productId : null}/image`} alt={selectedProduct ? selectedProduct.name : ''}
            style={{ maxWidth: '100%', maxHeight: '800px' }}
          />
        </Box>
      </Modal>
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
                  <ButtonBase sx={{
                    display: 'block',
                    textAlign: 'initial'
                  }}
                    onClick={() => handleImageClick(product)}
                  >

                    <CardMedia
                      component="div"
                      sx={{
                        // 16:9
                        pt: '135.25%',
                        backgroundSize: 'contain'
                      }}

                      image={`${BASE_URL}/product/${product.productId}/image`}
                    />
                  </ButtonBase>
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
                    <Button component={RouterLink} to="/login" size="small" variant='contained'>Add to Cart</Button>
                  </CardActions>
                </Card>
              </Grid>
            ))}
          </Grid>
          <Container
            sx={{ my: 5 }}
            style={{ display: 'flex', justifyContent: 'center' }}>
            <Button
              onClick={loadMore}
              disabled={data.last}
              variant="contained"
              sx={{ mt: 2 }}
            >
              Load More
            </Button>
          </Container>
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