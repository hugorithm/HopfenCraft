import React, { useState, ChangeEvent, FormEvent } from 'react';
import {
  TextField,
  Button,
  Typography,
  Box,
  Container,
  Fade,
  CssBaseline,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';

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

interface FormData {
  brand: string;
  name: string;
  description: string;
  quantity: number;
  price: number;
  file: File | null;
  fileName: string; // Added to store the selected file name
}

const initialFormData: FormData = {
  brand: '',
  name: '',
  description: '',
  quantity: 0,
  price: 0,
  file: null,
  fileName: '', // Initialize it as an empty string
};

const RegisterProduct: React.FC = () => {
  const [formData, setFormData] = useState<FormData>(initialFormData);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'quantity' || name === 'price' ? parseFloat(value) : value,
    });
  };

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const selectedFile = e.target.files[0];
      setFormData({
        ...formData,
        file: selectedFile,
        fileName: selectedFile.name, // Store the selected file name
      });
    }
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // Send the formData to your server here
    console.log('Form data submitted:', formData);
    // You can send the data to your server using Axios or fetch.
    // Replace the console.log with your API call.
  };

  return (
    <>
      <CssBaseline />
      <Fade in={true} timeout={1000}>
        <Container maxWidth="sm">
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
