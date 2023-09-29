import { Box, Button, Typography } from "@mui/material";

const ItemAddedToCart = () => {
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
        Item added to the Shopping Cart!
      </Typography>
      <Button
        href="/shopping-cart"
        variant="contained"
        style={{
          marginTop: "0.5rem", 
        }}
      >
        Go to Shopping Cart!
      </Button>
    </Box>
  );
};

export default ItemAddedToCart;
