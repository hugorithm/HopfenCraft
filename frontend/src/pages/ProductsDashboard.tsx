import { useEffect, useRef } from "react";
import ProductTable from "../components/ProductTable";
import { fetchProducts, resetPage, resetProducts, selectProducts, setProducts } from "../features/productsSlice";
import { ProductData } from "../types/product/ProductData";
import { useSelector } from "react-redux";
import { useAppDispatch } from "../app/hooks";
import { Box, Container, Fade } from "@mui/material";
import LoadMoreButton from "../components/LoadMoreButton";

const ProductsDashboard = () => {
  const { products, loading, last } = useSelector(selectProducts);
  const dispatch = useAppDispatch();

  const getProducts = () => {
    dispatch(resetPage());
    dispatch(resetProducts());
    dispatch(fetchProducts())
      .then((data) => {
        if (data.payload) {
          const payload = data.payload as ProductData;
          dispatch(setProducts(payload.content));
        } else {
          throw new Error("Failed to fetch data");
        }
      });
  }

  const loadMore = () => {
    if (!last && loading !== 'loading') {
      dispatch(fetchProducts());
    }
  }

  const renderAfterCalled = useRef(false);
  useEffect(() => {
    if (!renderAfterCalled.current) { // This is so that React Strict Mode doesn't cause issues
      if (loading !== "loading") {
        getProducts();
      }
    }
    renderAfterCalled.current = true; // This is so that React Strict Mode doesn't cause issues
  }, [dispatch, products]);

  return (
    <>
      <Fade in={true} timeout={1000}>
        <Container maxWidth="lg" >
          <Box
            sx={{
              mt: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}>
            <ProductTable products={products} />
          </Box>
          <LoadMoreButton onClick={loadMore} disabled={last} />
        </Container>
      </Fade >
    </>
  );
}

export default ProductsDashboard;
