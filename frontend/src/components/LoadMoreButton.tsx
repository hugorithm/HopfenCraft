import { Button, Container } from "@mui/material";
import React from "react";

interface LoadMoreButtonProps {
    onClick: () => void;
    disabled: boolean;
}

const LoadMoreButton: React.FC<LoadMoreButtonProps> = ({ onClick, disabled }) => {
    return (
        <Container
            sx={{ my: 5 }}
            style={{ display: 'flex', justifyContent: 'center' }}>
            <Button
                onClick={onClick}
                disabled={disabled}
                variant="contained"
                sx={{ mt: 2 }}
            >
                Load More
            </Button>
        </Container>
    )
}

export default LoadMoreButton;