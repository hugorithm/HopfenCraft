import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Typography, ButtonBase } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product } from '../types/product/ProductData';
import { Link } from 'react-router-dom';
import formatDate from '../utils/dateFormatter';

interface ProductTableProps {
  products: Product[];
}

const ProductTable: React.FC<ProductTableProps> = ({ products }) => {

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Brand</TableCell>
            <TableCell>Image</TableCell>
            <TableCell>Name</TableCell>
            <TableCell>Description</TableCell>
            <TableCell>Price</TableCell>
            <TableCell>Stock Quantity</TableCell>
            <TableCell>Registration Date</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.productId}>
              <TableCell>#{product.productId}</TableCell>
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
              <TableCell>{product.description}</TableCell>
              <TableCell>€{product.price}</TableCell>
              <TableCell>{product.quantity}</TableCell>
              <TableCell>{formatDate(product.registerDateTime)}</TableCell>
              <TableCell>
                <Button variant='text' component={Link} to={`/product/${product.productId}/update`}>Edit</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ProductTable;
