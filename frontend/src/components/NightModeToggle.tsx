import { IconButton, Tooltip } from "@mui/material";
import Brightness4Icon from "@mui/icons-material/Brightness4";
import Brightness7Icon from "@mui/icons-material/Brightness7";
import { useThemeContext } from "../theme/ThemeContextProvider";

const NightModeToggle = () => {
  const { mode, toggleColorMode } = useThemeContext();

  return (
    <>
      <Tooltip title={`Toggle ${mode === "dark" ? "light" : "dark"} mode`}>
        <IconButton sx={{ ml: 1, marginLeft: 'auto' }} onClick={toggleColorMode} color="inherit">
          {mode === "dark" ? <Brightness7Icon /> : <Brightness4Icon />}
        </IconButton>
      </Tooltip>
    </>
  );
};

export default NightModeToggle;