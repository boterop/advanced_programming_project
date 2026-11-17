import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  Outlet,
} from "react-router-dom";
import "./App.css";
import "./styles/auth.css";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import Search from "./pages/Search";
import ApartmentDetails from "./pages/ApartmentDetails";
import Profile from "./pages/Profile";
import PrivacyPolicy from "./pages/DataPolicy";
import AdminDashboard from "./pages/AdminDashboard";
import AppLayout from "./layouts/AppLayout";
import { useAuth } from "./hooks/useAuth";

const RequireAuth = () => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return null;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

const RequireUnAuth = () => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return null;
  }

  if (!isAuthenticated) {
    return <Outlet />;
  }

  return <Navigate to="/" replace />;
};

const RequireAdmin = () => {
  const { isAdmin, loading } = useAuth();

  if (loading) return null;
  if (!isAdmin) return <Navigate to="/" replace />;
  return <Outlet />;
};

const App = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/privacy" element={<PrivacyPolicy />} />
      <Route element={<RequireUnAuth />}>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password/:token" element={<ResetPassword />} />
      </Route>

      <Route element={<RequireAuth />}>
        <Route element={<AppLayout />}>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<Search />} />
          <Route path="/apartment/:id" element={<ApartmentDetails />} />
          <Route path="/profile" element={<Profile />} />
          <Route element={<RequireAdmin />}>
            <Route path="/admin" element={<AdminDashboard />} />
          </Route>
        </Route>
      </Route>
    </Routes>
  </BrowserRouter>
);

export default App;

