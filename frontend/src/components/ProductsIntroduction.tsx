import { Box, Container, Typography } from "@mui/material";

const ProductsIntroduction = () => {
  return (
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
  );
}

export default ProductsIntroduction;