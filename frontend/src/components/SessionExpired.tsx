import { Box, Button, Typography } from "@mui/material";

const SessionExpired = () => {
  return (
    <Box
      style={{
        backgroundColor: "primary",
        padding: "1rem", 
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        textAlign: "center",
      }}
    >
      <Typography
        style={{
          fontSize: "1.2rem", 
          fontWeight: "bold", 
          marginBottom: "0.5rem", 
        }}
      >
        Your session has expired!
      </Typography>
      <Button
        href="/login"
        variant="contained"
        style={{
          marginTop: "0.5rem", 
        }}
      >
        Please Login!
      </Button>
    </Box>
  );
};

export default SessionExpired;
