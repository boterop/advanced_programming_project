import { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { Alert, Box, Button, Link, TextField } from "@mui/material";
import AuthLayout from "../layouts/AuthLayout";

const ForgotPassword = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [formError, setFormError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setFormError("");
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
        title="Recuperar contraseña"
        subtitle="Te hemos enviado un correo con instrucciones para recuperar tu contraseña."
        footer={
          <>
            <Link component={RouterLink} to="/login" className="auth-link">
              Volver al inicio de sesión
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
      title="Recuperar contraseña"
      subtitle="Ingresa tu correo electrónico y te enviaremos instrucciones para recuperar tu contraseña."
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
          label="Correo electrónico"
          type="email"
          name="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          required
          autoComplete="email"
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
            {loading ? "Enviando…" : "Enviar instrucciones"}
          </Button>
        </div>
      </Box>
    </AuthLayout>
  );
};

export default ForgotPassword;