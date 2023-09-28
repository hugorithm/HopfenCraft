import Button from '@mui/material/Button';;
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Link from '@mui/material/Link';
import Modal from '@mui/material/Modal';
import { Link as RouterLink } from 'react-router-dom';
import { Product, ProductData } from '../types/ProductData';
import { BASE_URL } from '../config/constants';
import { useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import { selectAuth } from '../features/authSlice';
import { fetchProducts, selectProducts, setProducts } from '../features/productsSlice';
import { useAppDispatch } from '../app/hooks';
import ProductsSkeleton from '../components/ProductsSkeleton';
import ProductCard from '../components/ProductCard';

const Products = () => {
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [productQuantities, setProductQuantities] = useState<{ [productId: string]: number }>({});
  const { jwt } = useSelector(selectAuth);

  const dispatch = useAppDispatch();
  const { products, loading, error, page, last } = useSelector(selectProducts);

  // Function to load more products
  const loadMore = () => {
    if (!last && loading !== 'loading') {
      dispatch(fetchProducts());
    }
  };

  const renderAfterCalled = useRef(false);
  useEffect(() => {
    if (!renderAfterCalled.current) { // This is so that React Strict Mode doesn't cause issues

      if (products.length === 0) {
        dispatch(fetchProducts()).then((data) => {

          if (data.payload) {
            const payload = data.payload as ProductData;
            dispatch(setProducts(payload.content));
          } else {
            throw new Error("Failed to fetch data");
          }
        });
      }
    }
    renderAfterCalled.current = true; // This is so that React Strict Mode doesn't cause issues
  }, [dispatch, products]);


  const handleChange = (productId: number, newValue: number | undefined) => {
    if (newValue === undefined) return;

    setProductQuantities((prevQuantities) => ({
      ...prevQuantities,
      [productId]: newValue,
    }));
  };


  const addToCart = (productId: number) => {
    const quantity = productQuantities[productId];


  }

  const handleImageClick = (product: Product) => {
    setSelectedProduct(product);
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedProduct(null);
  };


  return (
    <>
      <CssBaseline />
      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', bgcolor: 'background.paper', boxShadow: 24, p: 4, maxWidth: '80vw' }}>
          <img src={`${BASE_URL}/product/${selectedProduct ? selectedProduct.productId : null}/image`} alt={selectedProduct ? selectedProduct.name : ''}
            style={{ maxWidth: '100%', maxHeight: '800px' }}
          />
        </Box>
      </Modal>
      <main>
        <Box
          sx={{
            bgcolor: 'background.paper',
            mt: 8,
            mb: 6,
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
          <Grid container spacing={5}>
            {products.length === 0 && loading !== 'succeeded' ? (
              // Render Skeletons when products are loading
              <ProductsSkeleton />
            ) : (
              // Render actual products when data is loaded
              products.map((product) => (
                <Grid item key={product.productId} xs={12} sm={6} md={4}>
                  <ProductCard
                    product={product}
                    jwt={jwt}
                    handleImageClick={handleImageClick}
                    handleChange={handleChange}
                    addToCart={addToCart} />
                </Grid>
              ))
            )}
          </Grid>
          <Container
            sx={{ my: 5 }}
            style={{ display: 'flex', justifyContent: 'center' }}>
            <Button
              onClick={loadMore}
              disabled={last}
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

export default Products;