import React, { ChangeEvent, useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, ButtonBase, TextField, styled, Box, Tooltip, InputAdornment } from '@mui/material';
import { BASE_URL } from '../config/constants';
import { Product, ProductData } from '../types/product/ProductData';
import formatDate from '../utils/dateFormatter';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import { useThemeContext } from '../theme/ThemeContextProvider';
import { useDeleteProductMutation, useUpdateProductMutation } from '../app/api/productApi';
import { Theme as ToastifyTheme, toast } from 'react-toastify';
import { buildFormDataHeadersWithJwt } from '../utils/jwtUtils';
import { ProductUpdate } from '../types/product/ProductUpdate';
import EuroIcon from '@mui/icons-material/Euro';
import { useAppDispatch } from '../app/hooks';
import { fetchProducts, resetPage, resetProducts, setProducts } from '../features/productsSlice';
import ConfirmationDialog from './ConfirmationDialog';

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

const ProductTable: React.FC<ProductTableProps> = ({ products }) => {
  const [rowEditMode, setRowEditMode] = useState<Record<number, boolean>>({});
  const [editedData, setEditedData] = useState<Record<number, Partial<Product>>>({});
  const [fileData, setFileData] = useState<Record<number, FileData>>({});
  const [isConfirmationDialogOpen, setIsConfirmationDialogOpen] = useState(false);
  const [productIdToRemove, setProductIdToRemove] = useState<number | null>(null);
  const dispatch = useAppDispatch();
  const { mode } = useThemeContext();
  const [updateProduct, {
    data,
    isSuccess,
    isError,
    error
  }] = useUpdateProductMutation();
  const [deleteProduct, {
    data: deleteData,
    isSuccess: isDeleteSuccess,
    isError: isDeleteError,
    error: deleteError
  }] = useDeleteProductMutation();

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

  useEffect(() => {
    if (isDeleteSuccess) {
      toast.success('Product Deleted Successfully!', {
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
  }, [isDeleteSuccess]);

  useEffect(() => {
    if (isDeleteError) {
      console.error(deleteError);
      toast.error('Product Deletion Failed!', {
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
  }, [isDeleteError]);

  const updateProductImage = async (productId: number) => {
    const productFile = fileData[productId];
    if (!productFile.file) return;

    const formData = new FormData();
    formData.append("file", productFile.file, productFile.fileName);
    const headers = buildFormDataHeadersWithJwt();

    const requestOptions = {
      method: 'PUT',
      body: formData,
      headers,
    }

    try {
      const response = await fetch(`${BASE_URL}/product/${productId}/image/update`, requestOptions);
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

  const handleUpdateDisabled = (productId: number) => {
    return editedData[productId] || fileData[productId] ? false : true;
  }

  const handleUpdate = (productId: number) => {
    const updatedProduct = {
      ...products.find(product => product.productId === productId),
      ...editedData[productId],
    };

    const body: ProductUpdate = {
      productId: productId,
      brand: updatedProduct.brand,
      name: updatedProduct.name,
      description: updatedProduct.description,
      quantity: updatedProduct.quantity?.toString(),
      price: updatedProduct.price
    };

    updateProduct(body)
      .unwrap()
      .then(() => {
        getProducts();
        updateProductImage(productId);
        setRowEditMode({
          ...rowEditMode,
          [productId]: false
        });

      });
  }

  const handleCancel = (productId: number) => {
    //Wipe file data on cancelation
    setFileData({});
    setEditedData({
      ...editedData,
      [productId]: {},
    });

    setRowEditMode({
      ...rowEditMode,
      [productId]: false
    })
  }

  const handleInputChange = (productId: number, field: string, value: string | number) => {
    setEditedData({
      ...editedData,
      [productId]: {
        ...editedData[productId],
        [field]: value,
      },
    });
  }

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>, productId: number) => {
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
            [productId]: {
              file: selectedFile,
              fileName: selectedFile.name,
              dataUrl: dataURL
            }
          });
        }
      }
    }
  };

  const handleRemove = (productId: number) => {
    setProductIdToRemove(productId);
    setIsConfirmationDialogOpen(true);
  };

  const confirmRemoval = () => {
    if (productIdToRemove === null) return;

    deleteProduct(productIdToRemove)
      .then(() => {
        getProducts();
        setIsConfirmationDialogOpen(false);
      });

  };

  const cancelRemoval = () => {
    setProductIdToRemove(null);
    setIsConfirmationDialogOpen(false);
  };

  return (
    <TableContainer component={Paper}>
      <ConfirmationDialog
        open={isConfirmationDialogOpen}
        onClose={cancelRemoval}
        onConfirm={confirmRemoval}
      />

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>SKU</TableCell>
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
                  <TableCell>{product.sku}</TableCell>
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
                  <TableCell>{product.sku}</TableCell>
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
                      onChange={(e) => handleInputChange(product.productId, 'brand', e.target.value)}
                    />
                  </TableCell>
                  <TableCell>
                    <ButtonBase>
                      {fileData[product.productId]?.file ? (
                        <Tooltip title="Click here to upload a different Image">
                          <Button component="label">
                            <img
                              src={fileData[product.productId]?.dataUrl}
                              style={{
                                width: 100, height: 100, objectFit: 'contain'
                              }} />
                            <VisuallyHiddenInput required id="file" type="file" accept="image/*" onChange={(event) => handleFileChange(event, product.productId)} />
                          </Button>
                        </Tooltip>
                      ) : (
                        <Button component="label" variant="outlined" startIcon={<CloudUploadIcon />}>
                          Upload Image
                          <VisuallyHiddenInput required id="file" type="file" accept="image/*" onChange={(event) => handleFileChange(event, product.productId)} />
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
                      onChange={(e) => handleInputChange(product.productId, 'name', e.target.value)}
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
                      onChange={(e) => handleInputChange(product.productId, 'description', e.target.value)}
                    />
                  </TableCell>
                  <TableCell>
                    <TextField
                      id="price"
                      name="price"
                      label="Price"
                      variant="outlined"
                      margin="normal"
                      type='number'
                      required
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <EuroIcon fontSize='small' />
                          </InputAdornment>
                        ),
                      }}
                      inputProps={{ step: "0.01", min: "0.01" }}
                      defaultValue={product.price}
                      onChange={(e) => handleInputChange(product.productId, 'price', e.target.value)}
                    />
                  </TableCell>
                  <TableCell>
                    <TextField
                      id="quantity"
                      name="quantity"
                      label="Quantity"
                      variant="outlined"
                      margin="normal"
                      type='number'
                      required
                      inputProps={{ min: "1" }}
                      defaultValue={product.quantity}
                      onChange={(e) => handleInputChange(product.productId, 'quantity', e.target.value)}
                    />
                  </TableCell>
                  <TableCell>{formatDate(product.registerDateTime)}</TableCell>
                  <TableCell>
                    <Box display={'flex'} justifyContent={'center'} alignItems={'center'} flexWrap={'nowrap'}>
                      <Button variant='contained' onClick={() => handleUpdate(product.productId)} sx={{ mr: 2 }}  disabled={handleUpdateDisabled(product.productId)}>Update</Button>
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
