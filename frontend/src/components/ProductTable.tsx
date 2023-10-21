import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Typography, ButtonBase } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product } from '../types/product/ProductData';

interface ProductTableProps {
  products: Product[];
}

const ProductTable: React.FC<ProductTableProps> = ({ products }) => {
  const truncateDescription = (description: string, maxLength: number) => {
    if (description.length <= maxLength) {
      return description;
    } else {
      return description.slice(0, maxLength) + '...';
    }
  };

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Brand</TableCell>
            <TableCell>Image</TableCell>
            <TableCell>Name</TableCell>
            <TableCell>Description</TableCell>
            <TableCell>Price</TableCell>
            <TableCell>Stock Quantity</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.productId}>
              <TableCell>{product.brand}</TableCell>
              <TableCell>
                <ButtonBase>
                  <img
                    src={`${BASE_URL}/product/${product.productId}/image`}
                    alt={product.name}
                    style={{
                      width: 100, height: 100, objectFit: 'contain'
                    }}
                  />
                </ButtonBase>
              </TableCell>
              <TableCell>{product.name}</TableCell>
              <TableCell>{truncateDescription(product.description, 300)}</TableCell>
              <TableCell>€{product.price}</TableCell>
              <TableCell>{product.quantity}</TableCell>
              <TableCell>
                <Button>Edit</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ProductTable;
