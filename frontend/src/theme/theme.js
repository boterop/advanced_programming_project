import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    mode: "light",
    primary: {
      main: "#17624F",
      dark: "#103E34",
      light: "#4D8B73",
      contrastText: "#ffffff",
    },
    secondary: {
      main: "#C66A43",
      contrastText: "#ffffff",
    },
    background: {
      default: "#F5F4EF",
      paper: "#FFFEFB",
    },
    text: {
      primary: "#1D302A",
      secondary: "#61716B",
    },
    divider: "#E5E7DF",
    brand: {
      accent: "#E8B66B",
    },
  },
  shape: {
    borderRadius: 14,
  },
  typography: {
    fontFamily:
      'system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
    h1: { fontWeight: 800, letterSpacing: "-0.035em" },
    h2: { fontWeight: 800, letterSpacing: "-0.03em" },
    h3: { fontWeight: 800, letterSpacing: "-0.025em" },
    h4: { fontWeight: 750, letterSpacing: "-0.02em" },
    h5: { fontWeight: 750, letterSpacing: "-0.015em" },
    h6: { fontWeight: 700 },
    button: { textTransform: "none", fontWeight: 600 },
  },
});

export default theme;
