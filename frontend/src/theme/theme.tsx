import { PaletteMode } from "@mui/material";
import { amber, grey } from "@mui/material/colors";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

const theme = {
  palette: {
    primary: amber,
  }
};

export const getDesignTokens = (mode: PaletteMode) => ({
  palette: {
    mode,
    ...(mode === "light"
      ? {
        // palette values for light mode
        primary: amber,
        secondary: grey,
        divider: grey[300],
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
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          ...(mode === "light") ?
            {
              scrollbarColor: "#ffffff #ffffff",
              "&::-webkit-scrollbar, & *::-webkit-scrollbar": {
                backgroundColor: "#ffffff",
                width: "8px"
              },
              "&::-webkit-scrollbar-thumb, & *::-webkit-scrollbar-thumb": {
                borderRadius: 8,
                backgroundColor: amber[500],
                minHeight: 10,
              },
              "&::-webkit-scrollbar-thumb:focus, & *::-webkit-scrollbar-thumb:focus": {
                backgroundColor: amber[500],
              },
              "&::-webkit-scrollbar-thumb:active, & *::-webkit-scrollbar-thumb:active": {
                backgroundColor: amber[500],
              },
              "&::-webkit-scrollbar-thumb:hover, & *::-webkit-scrollbar-thumb:hover": {
                backgroundColor: amber[800],
              },
              "&::-webkit-scrollbar-corner, & *::-webkit-scrollbar-corner": {
                backgroundColor: amber[500],
              },
            }
            : {
              scrollbarColor: grey[900],
              "&::-webkit-scrollbar, & *::-webkit-scrollbar": {
                backgroundColor: grey[900],
                width: "8px"
              },
              "&::-webkit-scrollbar-thumb, & *::-webkit-scrollbar-thumb": {
                borderRadius: 8,
                backgroundColor: amber[500],
                minHeight: 10,
              },
              "&::-webkit-scrollbar-thumb:focus, & *::-webkit-scrollbar-thumb:focus": {
                backgroundColor: amber[500],
              },
              "&::-webkit-scrollbar-thumb:active, & *::-webkit-scrollbar-thumb:active": {
                backgroundColor: amber[500],
              },
              "&::-webkit-scrollbar-thumb:hover, & *::-webkit-scrollbar-thumb:hover": {
                backgroundColor: amber[800],
              },
              "&::-webkit-scrollbar-corner, & *::-webkit-scrollbar-corner": {
                backgroundColor: amber[500],
              },
            }
        },
      },
    },
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