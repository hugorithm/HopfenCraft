import React, { useState, ChangeEvent, FormEvent, useEffect } from 'react';
import {
  TextField,
  Button,
  Typography,
  Box,
  Container,
  Fade,
  CssBaseline,
  Avatar,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import { useRegisterProductMutation } from '../app/api/productApi';
import { ProductRegistration } from '../types/product/ProductRegistration';
import { Theme as ToastifyTheme, toast } from 'react-toastify';
import { useThemeContext } from '../theme/ThemeContextProvider';
import { BASE_URL } from '../config/constants';
import { buildFormDataHeadersWithJwt } from '../utils/jwtUtils';
import ProductRegistered from '../components/ProductRegistered';

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

const AvatarContainer = styled(Avatar)({
  width: '10rem',
  height: '10rem',
  borderRadius: '8px',
  backgroundColor: 'transparent',
});

const ImagePreview = styled('img')({
  width: '100%',
  height: '100%',
  objectFit: 'contain',
  borderRadius: '8px',
  marginTop: '1rem',
  marginBottom: '1rem'
});


interface FileData {
  file: File | null;
  fileName: string;
}

const initialFileData: FileData = {
  file: null,
  fileName: '',
};

const RegisterProduct: React.FC = () => {
  const [fileData, setFileData] = useState<FileData>(initialFileData);
  const { mode } = useThemeContext();
  const [registerProduct, {
    data,
    isSuccess,
    isError,
    error
  }] = useRegisterProductMutation();

  useEffect(() => {
    if (isSuccess && data) {
      toast.success(<ProductRegistered id={data.productId} />, {
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
  }, [isSuccess])

  useEffect(() => {
    if (isError) {
      console.error(error);
      toast.error('Product Registration Failed!', {
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

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const selectedFile = e.target.files[0];
      if (!selectedFile) return;
      setFileData({
        ...fileData,
        file: selectedFile,
        fileName: selectedFile.name,
      });
    }
  };

  const registerProductImage = async (id: string) => {
    if (!fileData.file) return;

    const formData = new FormData();
    formData.append("file", fileData.file, fileData.fileName);
    const headers = buildFormDataHeadersWithJwt();

    const requestOptions = {
      method: 'POST',
      body: formData,
      headers,
    }

    try {
      const response = await fetch(`${BASE_URL}/product/${id}/image/register`, requestOptions);
      if (response.ok) {
        const data = await response.json();
        return data;
      } else {
        throw new Error("Failed to register the product image");
      }
    } catch (error) {
      console.error(error);
    }

  }

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    const body: ProductRegistration = {
      brand: data.get('brand') as string,
      name: data.get('name') as string,
      description: data.get('description') as string,
      quantity: data.get('quantity') as string,
      price: data.get('price') as string
    };

    registerProduct(body)
      .unwrap()
      .then((product) => {
        registerProductImage(product.productId.toString());
      });
  };

  return (
    <>
      <CssBaseline />
      <Fade in={true} timeout={1000}>
        <Container maxWidth="sm" sx={{ mb: "1rem" }}>
          <Box
            sx={{
              mt: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Typography component="h1" variant="h2" gutterBottom>
              Product Registration
            </Typography>
            <form onSubmit={handleSubmit}>
              <TextField
                name="brand"
                label="Brand"
                variant="outlined"
                fullWidth
                margin="normal"
                value={formData.brand}
                onChange={handleInputChange}
                required
              />
              <TextField
                name="name"
                label="Name"
                variant="outlined"
                fullWidth
                margin="normal"
                value={formData.name}
                onChange={handleInputChange}
                required
              />
              <TextField
                name="description"
                label="Description"
                variant="outlined"
                fullWidth
                margin="normal"
                multiline
                rows={4}
                value={formData.description}
                onChange={handleInputChange}
                required
              />
              <TextField
                name="quantity"
                label="Quantity"
                type="number"
                variant="outlined"
                fullWidth
                margin="normal"
                value={formData.quantity}
                onChange={handleInputChange}
                required
              />
              <TextField
                name="price"
                label="Price"
                type="number"
                variant="outlined"
                fullWidth
                margin="normal"
                value={formData.price}
                onChange={handleInputChange}
                required
              />
              <Button component="label" variant="contained" startIcon={<CloudUploadIcon />}>
                {formData.fileName ? formData.fileName : 'Upload Product Image'} {/* Display the selected file name */}
                <VisuallyHiddenInput type="file" accept="image/*" onChange={handleFileChange} />
              </Button>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                style={{ marginTop: 20 }}
              >
                Register Product
              </Button>
            </form>
          </Box>
        </Container>
      </Fade>
    </>
  );
};

export default RegisterProduct;
