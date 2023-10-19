import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Product, ProductData } from '../types/product/ProductData';
import { BASE_URL } from '../config/constants';
import { RootState } from '../app/store';

interface ProductsState {
  products: Product[];
  loading: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
  page: number;
  last: boolean;
}

const initialState: ProductsState = {
  products: [],
  loading: 'idle',
  error: null,
  page: 0,
  last: true,
};

// Define an async thunk for fetching products
export const fetchProducts = createAsyncThunk<ProductData, void | string, { state: RootState }>(
  'products/fetchProducts',
  async (search, { getState }) => {
    const { page } = getState().products;
    const baseApiUrl = `${BASE_URL}/product/products?page=${page}`;
    const apiUrl = search ? `${baseApiUrl}&search=${encodeURIComponent(search)}` : baseApiUrl;
    const response = await fetch(apiUrl);

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    return data;
  }
);

// Create a products slice
const productsSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    setProducts: (state, action: PayloadAction<Product[]>) => {
      state.products = action.payload;
    },
    resetPage: (state) => {
      state.page = 0;
    },
    resetProducts: (state) => {
      state.products = [];
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProducts.pending, (state) => {
        state.loading = 'loading';
      })
      .addCase(fetchProducts.fulfilled, (state, action: PayloadAction<ProductData>) => {
        state.loading = 'succeeded';
        state.page += 1;
        state.last = action.payload.last;
        state.products.push(...action.payload.content);
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = 'failed';
        state.error = action.error.message ?? 'An error occurred while fetching products.';
      });
  },
});

export const selectProducts = (state: RootState) => state.products;
export const { setProducts, resetPage, resetProducts } = productsSlice.actions;
export default productsSlice.reducer;
