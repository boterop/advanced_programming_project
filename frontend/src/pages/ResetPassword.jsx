import { useState } from "react";
import { Link as RouterLink, useNavigate, useParams } from "react-router-dom";
import { Alert, Box, Button, Link, TextField } from "@mui/material";
import AuthLayout from "../layouts/AuthLayout";

const ResetPassword = () => {
  const navigate = useNavigate();
  const { token } = useParams();
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [formError, setFormError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setFormError("");
    
    if (password !== confirmPassword) {
      setFormError("Las contraseñas no coinciden");
      return;
    }

    setLoading(true);

    try {
      // In a real app, this would make an API call
      // For now we just simulate success
      await new Promise(resolve => setTimeout(resolve, 1000));
      setSuccess(true);
    } catch (err) {
      setFormError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <AuthLayout
        title="Contraseña restablecida"
        subtitle="Tu contraseña ha sido restablecida correctamente."
        footer={
          <>
            <Link component={RouterLink} to="/login" className="auth-link">
              Iniciar sesión
            </Link>
          </>
        }
      >
        <Box sx={{ textAlign: "center", mt: 2 }}>
          <Button
            variant="contained"
            onClick={() => navigate("/login")}
            sx={{ mt: 2 }}
          >
            Iniciar sesión
          </Button>
        </Box>
      </AuthLayout>
    );
  }

  return (
    <AuthLayout
      title="Restablecer contraseña"
      subtitle="Ingresa tu nueva contraseña para restablecerla."
      footer={
        <>
          <Link component={RouterLink} to="/login" className="auth-link">
            Volver al inicio de sesión
          </Link>
        </>
      }
    >
      <Box component="form" onSubmit={handleSubmit} noValidate>
        <TextField
          fullWidth
          label="Nueva contraseña"
          type="password"
          name="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          required
          autoComplete="new-password"
          margin="normal"
        />

        <TextField
          fullWidth
          label="Confirmar contraseña"
          type="password"
          name="confirmPassword"
          value={confirmPassword}
          onChange={(event) => setConfirmPassword(event.target.value)}
          required
          autoComplete="new-password"
          margin="normal"
        />

        {formError && (
          <Alert severity="error" sx={{ mt: 2 }}>
            {formError}
          </Alert>
        )}

        <div className="auth-form__actions">
          <Button
            fullWidth
            type="submit"
            variant="contained"
            size="large"
            disabled={loading}
          >
            {loading ? "Restableciendo…" : "Restablecer contraseña"}
          </Button>
        </div>
      </Box>
    </AuthLayout>
  );
};

export default ResetPassword;