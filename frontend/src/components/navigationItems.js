import { matchPath } from "react-router-dom";
import HomeOutlinedIcon from "@mui/icons-material/HomeOutlined";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";

const matches = (pattern, pathname) =>
  matchPath({ path: pattern, end: true }, pathname) !== null;

// Fuente única de verdad de la navegación privada. Solo apunta a rutas que
// ya existen en App.jsx.
export const NAV_ITEMS = [
  {
    id: "home",
    label: "Inicio",
    to: "/",
    icon: HomeOutlinedIcon,
    isActive: (pathname) => matches("/", pathname),
  },
  {
    id: "search",
    label: "Buscar",
    to: "/search",
    icon: SearchOutlinedIcon,
    isActive: (pathname) => matches("/search", pathname),
  },
  {
    id: "profile",
    label: "Mi perfil",
    to: "/profile",
    icon: AccountCircleOutlinedIcon,
    isActive: (pathname) => matches("/profile", pathname),
  },
];

export const getNavItems = (isAdmin) =>
  NAV_ITEMS;

export const getPageTitle = (pathname) => {
  return NAV_ITEMS.find((item) => item.isActive(pathname))?.label ?? "";
};

// A dónde volver cuando no hay historial interno (p. ej. al abrir la URL
// directamente en una pestaña nueva).
export const getBackFallback = (pathname) =>
  "/";
