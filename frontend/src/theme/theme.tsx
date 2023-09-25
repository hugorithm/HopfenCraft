import { PaletteMode } from "@mui/material";
import { amber, grey } from "@mui/material/colors";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

const theme = {
  palette: {
    primary: amber,
  },
};

export const getDesignTokens = (mode: PaletteMode) => ({
  palette: {
    mode,
    ...(mode === "light"
      ? {
        // palette values for light mode
        primary: amber,
        secondary: grey, 
        divider: amber[200],
        text: {
          primary: grey[900],
          secondary: grey[800],
        },
      }
      : {
        // palette values for dark mode
        primary: amber,
        secondary: amber,
        divider: amber[700],
        background: {
          default: grey[900],
          paper: grey[900],
        },
        text: {
          primary: "#fff",
          secondary: grey[200],
        },
      }),
  },
  components: {
    typography: {
      fontFamily: [
        'Roboto',
        'sans-serif'
      ]
    },
    MuiTab: {
      mode,
      ...(mode === "light")
        ? {
          styleOverrides: {
            root: {
              "&.Mui-selected": {
                color: "#fff"
              },
              "&:not(.Mui-selected)": {
                color: grey[900]
              }
            }
          }
        } 
        : {
          styleOverrides: {
            root: {
              "&.Mui-selected": {
                color: amber
              },
              "&:not(.Mui-selected)": {
                color: "#fff"
              }
            }
          }
        }
    }
  }
});

export default theme;