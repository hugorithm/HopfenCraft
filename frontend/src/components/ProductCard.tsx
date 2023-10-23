// ProductCard.tsx
import React from 'react';
import { Card, CardMedia, CardContent, Typography, ButtonBase, CardActions, Box, Button, Fade } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product } from '../types/product/ProductData';
import CustomNumberInput from '../components/CustomNumberInput';
import { Link as RouterLink } from 'react-router-dom';
import { grey } from '@mui/material/colors';

interface ProductCardProps {
  product: Product;
  handleImageClick: (product: Product) => void;
  handleChange: (productId: number, newValue: number | undefined) => void;
  addToCart: (productId: number) => void;
  jwt: string | null;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, handleImageClick, handleChange, addToCart, jwt }) => {

  const truncateDescription = (description: string, maxLength: number) => {
    if (description.length <= maxLength) {
      return description;
    } else {
      return description.slice(0, maxLength) + '...';
    }
  };

  return (
    <Fade in={true} timeout={1000}>
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
          <Typography gutterBottom variant="caption" component="p" fontWeight={700} color={grey[800]}>
            SKU: {product.sku}
          </Typography>
          <Typography gutterBottom variant="body2">{truncateDescription(product.description, 300)}</Typography>
          <Typography fontWeight={500} variant="body1">€{product.price}</Typography>
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
    </Fade>
  );
};

export default ProductCard;