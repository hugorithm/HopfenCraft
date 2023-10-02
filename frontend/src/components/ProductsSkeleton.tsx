import { ButtonBase, Card, CardActions, CardContent, Grid, Skeleton, Typography } from "@mui/material";
import { BASE_URL } from "../config/constants";

const ProductsSkeleton = () => {

  return (
    Array.from({ length: 15 }).map((_, index) => (
      <Grid item key={index} xs={12} sm={6} md={4}>
        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
          <Skeleton variant="rectangular" height={135} />
          <CardContent sx={{ flexGrow: 1 }}>
            <Typography>
              <Skeleton />
            </Typography>
            <Typography gutterBottom variant="h6" component="h2">
              <Skeleton />
            </Typography>
            <Typography gutterBottom>
              <Skeleton />
            </Typography>
            <Typography sx={{ fontWeight: 500 }}>
              <Skeleton />
            </Typography>
          </CardContent>
          <CardActions>
            <ButtonBase sx={{
              display: 'block',
              textAlign: 'initial'
            }}>
              <Skeleton variant="rectangular" height={40} />
            </ButtonBase>
          </CardActions>
        </Card>
      </Grid>
    ))
  )
}


export default ProductsSkeleton;