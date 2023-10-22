import React, { ChangeEvent, useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, ButtonBase, TextField, styled, Box, Tooltip } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product } from '../types/product/ProductData';
import formatDate from '../utils/dateFormatter';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import { useThemeContext } from '../theme/ThemeContextProvider';
import { useUpdateProductMutation } from '../app/api/productApi';
import { Theme as ToastifyTheme, toast } from 'react-toastify';
import { buildFormDataHeadersWithJwt } from '../utils/jwtUtils';

const VisuallyHiddenInput = styled('input')({
  clip: 'rect(0 0 0 0)',
  clipPath: 'inset(50%)',
  height: 1,
  overflow: 'hidden',
  position: 'absolute',
  bottom: 0,
  left: 0,
  whiteSpace: 'nowrap',
  width: 1,
});

interface ProductTableProps {
  products: Product[];
}

interface FileData {
  file: File | null;
  fileName: string;
  dataUrl: string;
}

const initialFileData: FileData = {
  file: null,
  fileName: '',
  dataUrl: ''
};

const ProductTable: React.FC<ProductTableProps> = ({ products }) => {
  const [rowEditMode, setRowEditMode] = useState<Record<number, boolean>>({});
  const [fileData, setFileData] = useState<FileData>(initialFileData);
  const { mode } = useThemeContext();
  const [updateProduct, {
    data,
    isSuccess,
    isError,
    error
  }] = useUpdateProductMutation();

  useEffect(() => {
    if (isSuccess && data) {
      toast.success('Product Updated Successfully!', {
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        pauseOnFocusLoss: false,
        progress: undefined,
        theme: mode as ToastifyTheme,
      });
    }
  }, [isSuccess]);

  useEffect(() => {
    if (isError) {
      console.error(error);
      toast.error('Product Update Failed!', {
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        pauseOnFocusLoss: false,
        progress: undefined,
        theme: mode as ToastifyTheme,
      });
    }
  }, [isError]);

  const updateProductImage = async (id: string) => {
    if (!fileData.file) return;

    const formData = new FormData();
    formData.append("file", fileData.file, fileData.fileName);
    const headers = buildFormDataHeadersWithJwt();

    const requestOptions = {
      method: 'PUT',
      body: formData,
      headers,
    }

    try {
      const response = await fetch(`${BASE_URL}/product/${id}/image/update`, requestOptions);
      if (response.ok) {
        const data = await response.json();
        return data;
      } else {
        throw new Error("Failed to update the product image");
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const initialEditState = products.reduce((acc, curr) => {
      acc[curr.productId] = false;
      return acc;
    }, {} as Record<number, boolean>);
    setRowEditMode(initialEditState);
  }, []);


  const handleEdit = (productId: number) => {
    setRowEditMode({
      ...rowEditMode,
      [productId]: true
    });
  }

  // const handleUpdate = () => {
  //   const data = new FormData(e.currentTarget);
  //   const body: ProductUpdate = {
  //     productId: parseInt(id),
  //     brand: data.get('brand') as string,
  //     name: data.get('name') as string,
  //     description: data.get('description') as string,
  //     quantity: data.get('quantity') as string,
  //     price: data.get('price') as string
  //   };

  //   updateProduct(body)
  //     .unwrap()
  //     .then(() => {
  //       if (productData.productId) {
  //         updateProductImage(productData.productId.toString());
  //       }
  //     });
  // }

  const handleCancel = (productId: number) => {
    //Wipe file data on cancelation
    setFileData(initialFileData);

    setRowEditMode({
      ...rowEditMode,
      [productId]: false
    })
  }

  const handleRemove = (productId: number) => {
    //TODO: Needs to open a modal confirmation
  }

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const selectedFile = e.target.files[0];

      if (!selectedFile) return;
      const reader = new FileReader();
      reader.readAsDataURL(selectedFile);

      reader.onload = (e) => {
        if (e.target && e.target.result) {
          const dataURL = e.target.result as string;
          setFileData({
            ...fileData,
            file: selectedFile,
            fileName: selectedFile.name,
            dataUrl: dataURL
          });
        }
      }
    }
  };

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
            <TableCell>Stock</TableCell>
            <TableCell>Creation Date</TableCell>
            <TableCell
              sx={{ display: "flex", justifyContent: "center" }}>
              Actions
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {products.map((product) => {
            if (!rowEditMode[product.productId]) {
              return (
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
                    <Box display={'flex'} justifyContent={'center'} alignItems={'center'}>
                      <Button sx={{ mr: 2 }} variant='outlined' onClick={() => handleEdit(product.productId)}>Edit</Button>
                      <Button variant='contained' color='error' onClick={() => handleRemove(product.productId)}>Remove</Button>
                    </Box>
                  </TableCell>
                </TableRow>
              )
            } else {
              return (
                <TableRow key={product.productId}>
                  <TableCell>#{product.productId}</TableCell>
                  <TableCell>
                    <TextField
                      id="brand"
                      name="brand"
                      label="Brand"
                      variant="outlined"
                      fullWidth
                      margin="normal"
                      required
                      defaultValue={product.brand}
                    />
                  </TableCell>
                  <TableCell>
                    <ButtonBase>
                      {fileData.file ? (
                        <Tooltip title="Click here to upload a different Image">
                          <Button component="label">
                            <img
                              src={fileData.dataUrl}
                              style={{
                                width: 100, height: 100, objectFit: 'contain'
                              }} />
                            <VisuallyHiddenInput required id="file" type="file" accept="image/*" onChange={handleFileChange} />
                          </Button>
                        </Tooltip>
                      ) : (
                        <Button component="label" variant="outlined" startIcon={<CloudUploadIcon />}>
                          Upload Image
                          <VisuallyHiddenInput required id="file" type="file" accept="image/*" onChange={handleFileChange} />
                        </Button>
                      )}
                    </ButtonBase>
                  </TableCell>
                  <TableCell>
                    <TextField
                      id="name"
                      name="name"
                      label="Name"
                      variant="outlined"
                      margin="normal"
                      required
                      defaultValue={product.name}
                    />
                  </TableCell>
                  <TableCell>
                    <TextField
                      id="description"
                      name="description"
                      label="Description"
                      variant="outlined"
                      multiline
                      rows={2}
                      margin="normal"
                      required
                      defaultValue={product.description}
                    />
                  </TableCell>
                  <TableCell>
                    <TextField
                      id="price"
                      name="price"
                      label="Price"
                      variant="outlined"
                      margin="normal"
                      required
                      inputProps={{ step: "0.01", min: "0.01" }}
                      defaultValue={product.price}
                    />
                  </TableCell>
                  <TableCell>
                    <TextField
                      id="quantity"
                      name="quantity"
                      label="Quantity"
                      variant="outlined"
                      margin="normal"
                      required
                      inputProps={{ min: "1" }}
                      defaultValue={product.quantity}
                    />
                  </TableCell>
                  <TableCell>{formatDate(product.registerDateTime)}</TableCell>
                  <TableCell>
                    <Box display={'flex'} justifyContent={'center'} alignItems={'center'} flexWrap={'nowrap'}>
                      {/* handleUpdate here */}
                      <Button variant='contained' onClick={() => {}} sx={{ mr: 2 }}>Update</Button>
                      <Button variant='outlined' onClick={() => handleCancel(product.productId)}>Cancel</Button>
                    </Box>
                  </TableCell>
                </TableRow>
              )
            }
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ProductTable;
