// ProductCard.tsx
import React from 'react';
import { Card, CardMedia, CardContent, Typography, ButtonBase, CardActions, Box, Button } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product } from '../types/ProductData';
import CustomNumberInput from '../components/CustomNumberInput';
import { Link as RouterLink } from 'react-router-dom';

interface ProductCardProps {
  product: Product;
  handleImageClick: (product: Product) => void;
  handleChange: (productId: number, newValue: number | undefined) => void;
  addToCart: (productId: number) => void;
  jwt: string | null;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, handleImageClick, handleChange, addToCart, jwt }) => {
  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <ButtonBase sx={{ display: 'block', textAlign: 'initial' }} onClick={() => handleImageClick(product)}>
        <CardMedia
          component="div"
          sx={{
            pt: '100.25%',
            backgroundSize: 'contain',
          }}
          image={`${BASE_URL}/product/${product.productId}/image`}
        />
      </ButtonBase>
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography>{product.brand}</Typography>
        <Typography gutterBottom variant="h6" component="h2">
          {product.name}
        </Typography>
        <Typography gutterBottom>{product.description}</Typography>
        <Typography sx={{ fontWeight: 500 }}>€{product.price}</Typography>
      </CardContent>
      <CardActions
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center', 
        }}
      >
        {jwt ? (
          <>
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <CustomNumberInput
                min={1}
                max={99}
                onChange={(_, val) => handleChange(product.productId, val)}
              />
              <Button
                sx={{ mt: 1 }}
                onClick={() => addToCart(product.productId)}
                size="small"
                variant="contained"
              >
                Add to Cart
              </Button>
            </Box>
          </>
        ) : (
          <Button component={RouterLink} to="/login" size="small" variant="contained">
            Add to Cart
          </Button>
        )}
      </CardActions>
    </Card>
  );
};

export default ProductCard;