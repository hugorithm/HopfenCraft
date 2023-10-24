import { useParams } from 'react-router-dom';
import { Container, Typography, CardMedia, Box, Grid, Skeleton } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { useGetProductQuery } from '../app/api/productApi';
import CustomError from '../errors/CustomError';
import NotFound from '../errors/NotFound';
import { FetchBaseQueryError } from '@reduxjs/toolkit/query';
import { Product } from '../types/product/ProductData';

const ProductDetails = () => {
  const { id } = useParams() as { id: string };

  const {
    data: product,
    isLoading,
    isError,
    error
  } = useGetProductQuery(id) as {
    data: Product | undefined,
    isLoading: boolean,
    isSuccess: boolean,
    isError: boolean,
    error: FetchBaseQueryError
  };

  if (isError) {
    if (error.status === 404) {
      return <NotFound />
    } else {
      return <CustomError />
    }
  }

  return (
    <>
      {isLoading || !product ? (
        <Skeleton variant="rectangular" width={300} height={100} />
      ) : (

        <Container maxWidth="lg">
          <Box
            sx={{
              mt: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Grid container spacing={3}>
              <Grid item xs={12} md={6}>
                <CardMedia
                  component="img"
                  alt={product.name}
                  height="500rem"
                  style={{ objectFit: 'contain' }}
                  image={`${BASE_URL}/product/${product.productId}/image`}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <Typography variant="h4" gutterBottom>
                  {product.name}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Description: {product.description}
                </Typography>
                <Typography variant="h5" gutterBottom>
                  Price: €{product.price}
                </Typography>

              </Grid>
            </Grid>
          </Box>
        </Container>
      )}
    </>
  );
};

export default ProductDetails;
