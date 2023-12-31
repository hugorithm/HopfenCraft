# HopfenCraft

<img align="center" alt="Logo" src="backend/src/main/resources/images/hopfencraft.jpg" width=200>    

## Website    

<img alt="Website" src="frontend/public/homepage.gif">

<p align="center">
    <img alt="Java" src="https://img.shields.io/badge/-Java-007396?style=flat&logo=java&logoColor=white"/>
    <img alt="Spring Boot" src="https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=flat&logo=spring&logoColor=white"/>
    <img alt="PostgreSQL" src="https://img.shields.io/badge/-PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white"/>
    <img alt="REST API" src="https://img.shields.io/badge/-REST%20API-FF5733?style=flat"/>
    <img alt="React" src="https://img.shields.io/badge/-React-61DAFB?style=flat&logo=react&logoColor=white"/>
    <img alt="Redux" src="https://img.shields.io/badge/-Redux-764ABC?style=flat&logo=redux&logoColor=white"/>
    <img alt="Material-UI" src="https://img.shields.io/badge/-Material%20UI-0081CB?style=flat&logo=material-ui&logoColor=white"/>
    <img alt="TypeScript" src="https://img.shields.io/badge/-TypeScript-3178C6?style=flat&logo=typescript&logoColor=white"/>
    <img alt="PayPal Integration Status" src="https://img.shields.io/badge/PayPal%20Integration-Success-brightgreen"/>
    <img alt="PayPal API Version" src="https://img.shields.io/badge/PayPal%20API%20Version-v2.0-blue"/>
    <img alt="PayPal Payment Methods" src="https://img.shields.io/badge/Payment%20Methods-PayPal%2C%20Credit%20Card-orange"/>
    <img alt="Docker" src="https://img.shields.io/badge/Docker-Container-blue?style=flat&logo=docker" />
    <img alt="GitHub OAuth2" src="https://img.shields.io/badge/GitHub%20OAuth2-Success-181717?style=flat&logo=github"/>
    <img alt="Google OAuth2" src="https://img.shields.io/badge/Google%20OAuth2-Success-4285F4?style=flat&logo=google"/>
    <img alt="Google reCAPTCHA" src="https://img.shields.io/badge/Google%20reCAPTCHA-Success-43A047?style=flat&logo=google"/>
</p>

[![CI tests](https://github.com/hugorithm/HopfenCraft/actions/workflows/maven.yml/badge.svg)](https://github.com/hugorithm/HopfenCraft/actions/workflows/maven.yml)
## Features

### User Management
- **User Accounts**: Register and log in to your personal HopfenCraft account.
- **User Profiles**: Update your user profile, change your password, and keep your information up to date.

### Admin Privileges
- **Admin Dashboard**: Admins have access to a powerful dashboard.
- **Product Management**: Add, update, or remove new beer products.
- **Stock Management**: Manage product stock levels with ease.

### Shopping Cart
- **Add to Cart**: Users can add their favorite beer products to the shopping cart.
- **Update Cart**: Modify quantities and remove items from your cart effortlessly.
- **Checkout**: Complete your beer order with our seamless checkout process.

### Orders
- **Create Orders**: Place orders for your favorite beers.
- **Order History**: View your order history and keep track of your purchases.

### Secure Payments
- **PayPal Integration**: Pay for your beer orders using PayPal or credit card.
- **Secure Transactions**: Rest assured that your payment information is handled securely.

## Installation

To run the HopfenCraft application, you'll need to set up the following environment variables in both the frontend and backend components:

### Frontend (React)

In the `frontend` directory, create a `.env` file with the following variable:

- `VITE_GOOGLE_RECAPTCHA_KEY`: Replace with your Google reCAPTCHA site key.
- `VITE_PAYPAL_CLIENT_ID`: Replace with your PayPal Client ID:

Example `.env` file content:
```env
VITE_GOOGLE_RECAPTCHA_KEY=your-recaptcha-site-key
VITE_PAYPAL_CLIENT_ID=your-paypal-client-id
```

### Backend (Java Spring Boot)

To configure the backend of the HopfenCraft application, you'll need to set the following environment variables:

- `DATABASE_URL`: Replace with your PostgreSQL database URL.
- `DB_PASSWORD`: Replace with your PostgreSQL database password.
- `DB_USERNAME`: Replace with your PostgreSQL database username.
- `EMAIL_HOST`: Replace with your email host configuration.
- `EMAIL_PASSWORD`: Replace with your email password.
- `EMAIL_PORT`: Replace with your email port number.
- `EMAIL_USERNAME`: Replace with your email username.
- `GITHUB_CLIENT`: Replace with your GitHub OAuth2 client ID.
- `GITHUB_SECRET`: Replace with your GitHub OAuth2 client secret.
- `GOOGLE_CLIENT`: Replace with your Google OAuth2 client ID.
- `GOOGLE_SECRET`: Replace with your Google OAuth2 client secret.
- `JWT_SIGNING_KEY`: Replace with your JWT signing key.
- `PAYPAL_CLIENT_ID`: Replace with your PayPal client ID.
- `PAYPAL_CLIENT_SECRET`: Replace with your PayPal client secret.
- `RSA_PRIVATE_EXPONENT`: Replace with your RSA private exponent.
- `RSA_PRIVATE_MODULUS`: Replace with your RSA private modulus.
- `RSA_PUBLIC_EXPONENT`: Replace with your RSA public exponent.
- `RSA_PUBLIC_MODULUS`: Replace with your RSA public modulus.
- `UPLOAD_FOLDER_PATH`: Replace with the path for file uploads.

Example `.env` file content for backend:
```env
DATABASE_URL=your-database-url
DB_PASSWORD=your-database-password
DB_USERNAME=your-database-username
EMAIL_HOST=your-email-host
EMAIL_PASSWORD=your-email-password
EMAIL_PORT=your-email-port
EMAIL_USERNAME=your-email-username
GITHUB_CLIENT=your-github-client-id
GITHUB_SECRET=your-github-client-secret
GOOGLE_CLIENT=your-google-client-id
GOOGLE_SECRET=your-google-client-secret
JWT_SIGNING_KEY=your-jwt-signing-key
PAYPAL_CLIENT_ID=your-paypal-client-id
PAYPAL_CLIENT_SECRET=your-paypal-client-secret
RSA_PRIVATE_EXPONENT=your-rsa-private-exponent
RSA_PRIVATE_MODULUS=your-rsa-private-modulus
RSA_PUBLIC_EXPONENT=your-rsa-public-exponent
RSA_PUBLIC_MODULUS=your-rsa-public-modulus
UPLOAD_FOLDER_PATH=your-upload-folder-path
```


## License

This repository is released under the [MIT license](https://opensource.org/licenses/MIT).

If distributing this application, credits to the original author must not be removed.
