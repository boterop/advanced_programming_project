import { Link as RouterLink } from "react-router-dom";
import { Box, Button, Container, Divider, Paper, Typography } from "@mui/material";

const PrivacyPolicy = () => (
  <Container maxWidth="md" sx={{ py: 5 }}>
    <Button component={RouterLink} to="/login" sx={{ mb: 2 }}>Volver</Button>
    <Paper sx={{ p: { xs: 3, md: 5 } }}>
      <Typography variant="overline">UQBNB · Información del proyecto</Typography>
      <Typography variant="h3" component="h1" gutterBottom>Política de privacidad</Typography>
      <Typography color="text.secondary" paragraph>
        Esta página explica qué información procesa la versión actual de UQBNB y para qué la usa.
      </Typography>
      <Divider sx={{ my: 3 }} />

      <Typography variant="h6" component="h2" gutterBottom>Información utilizada</Typography>
      <Typography paragraph>
        La cuenta guarda nombre, correo electrónico, contraseña protegida mediante hash y rol. Las
        reservas registran los datos del titular y acompañantes necesarios para gestionar la estadía,
        junto con sus fechas, cargos y pagos registrados.
      </Typography>

      <Typography variant="h6" component="h2" gutterBottom>Uso y acceso</Typography>
      <Typography paragraph>
        Estos datos se usan para iniciar sesión, mostrar la cuenta, consultar disponibilidad,
        cotizar, administrar reservas y mantener el estado de pagos. Las operaciones administrativas
        requieren el rol de administrador.
      </Typography>

      <Typography variant="h6" component="h2" gutterBottom>Almacenamiento en esta versión</Typography>
      <Typography paragraph>
        La configuración actual usa H2 en memoria. Los registros pueden desaparecer al detener o
        reiniciar el backend. No se integra aquí un proveedor externo de pagos ni de correo.
      </Typography>

      <Typography variant="h6" component="h2" gutterBottom>Control de la cuenta</Typography>
      <Typography paragraph>
        El usuario puede cerrar su sesión desde la navegación. La aplicación todavía no ofrece una
        pantalla para descargar o eliminar una cuenta; esas operaciones requieren soporte adicional
        del backend.
      </Typography>

      <Box sx={{ mt: 4 }}>
        <Typography variant="caption" color="text.secondary">
          Texto informativo correspondiente a la versión actual del proyecto.
        </Typography>
      </Box>
    </Paper>
  </Container>
);

export default PrivacyPolicy;
