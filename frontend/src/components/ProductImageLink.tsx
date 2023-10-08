import { Box, CardMedia } from "@mui/material";
import { Link as RouterLink } from 'react-router-dom';
import { BASE_URL } from "../config/constants";

interface ProductImageLinkProps {
  productId: number;
  width?: string;
}

const ProductImageLink: React.FC<ProductImageLinkProps> = ({ productId, width }) => {
  return (
    <Box sx={{ width: width}}>
      <RouterLink to={`/product/${productId}`}>
        <CardMedia
          component="div"
          sx={{
            pt: '50.25%',
            backgroundSize: 'contain',
            cursor: 'pointer',
          }}
          image={`${BASE_URL}/product/${productId}/image`}
        />
      </RouterLink>
    </Box>
  );
};

export default ProductImageLink;
