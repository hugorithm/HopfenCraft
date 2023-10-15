import { Box, Button, Typography } from "@mui/material";

interface ProductIdProps {
    id: number;
}

const ProductRegistered: React.FC<ProductIdProps> = ({id: id}) => {
  return (
    <Box
      style={{
        backgroundColor: "primary",
        padding: "1rem", 
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        textAlign: "center",
      }}
    >
      <Typography
        style={{
          fontSize: "1.2rem", 
          fontWeight: "bold", 
          marginBottom: "0.5rem", 
        }}
      >
        Product Registered Successfully!
      </Typography>
      <Button
        href={`/product/${id}`}
        variant="contained"
        style={{
          marginTop: "0.5rem", 
        }}
      >
        Go to The Product Page!
      </Button>
    </Box>
  );
};

export default ProductRegistered;
