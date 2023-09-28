import { Skeleton, TableCell, TableRow } from "@mui/material";

const RenderSkeleton = () => {
    return (
        Array.from({ length: 10 }).map((_, index) => (
        <TableRow key={index}>
            <TableCell>
            <Skeleton variant="text" animation="wave" />
            </TableCell>
            <TableCell align="right">
            <Skeleton variant="text" animation="wave" />
            </TableCell>
            <TableCell align="right">
            <Skeleton variant="text" animation="wave" />
            </TableCell>
            <TableCell align="right">
            <Skeleton variant="text" animation="wave" />
            </TableCell>
            <TableCell align="right">
            <Skeleton variant="text" animation="wave" />
            </TableCell>
        </TableRow>
        ))
    );
  };

  export default RenderSkeleton;