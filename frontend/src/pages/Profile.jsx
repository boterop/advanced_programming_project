import {
  Box,
  Button,
  Card,
  CardContent,
  Container,
  Grid,
  Typography,
  Avatar,
  TextField,
  FormControlLabel,
  Checkbox,
  Divider,
} from "@mui/material";
import { useState } from "react";

const Profile = () => {
  const [profileData, setProfileData] = useState({
    name: "Juan Pérez",
    email: "juan.perez@example.com",
    phone: "+52 55 1234 5678",
    bio: "Estudiante de ingeniería buscando alojamiento en el edificio.",
    isOwner: false,
  });

  const [editing, setEditing] = useState(false);

  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = () => {
    setEditing(false);
  };

  const handleCancel = () => {
    setEditing(false);
  };

  const handleChange = (field, value) => {
    setProfileData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Mi Perfil
      </Typography>

      <Grid container spacing={4}>
        {/* Profile Card */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                <Avatar
                  sx={{ width: 100, height: 100, mb: 2 }}
                  src="https://images.unsplash.com/photo-1494790108377-be9c253d107f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80"
                />
                <Typography variant="h6" component="h2" gutterBottom>
                  {profileData.name}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {profileData.email}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {profileData.phone}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {profileData.bio}
                </Typography>
                <FormControlLabel
                  control={<Checkbox checked={profileData.isOwner} disabled />}
                  label="Soy propietario de un apartamento"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Edit Profile Form */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" component="h2" gutterBottom>
                Datos Personales
              </Typography>
              
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Nombre"
                    value={profileData.name}
                    onChange={(e) => handleChange("name", e.target.value)}
                    disabled={!editing}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Email"
                    value={profileData.email}
                    onChange={(e) => handleChange("email", e.target.value)}
                    disabled={!editing}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Teléfono"
                    value={profileData.phone}
                    onChange={(e) => handleChange("phone", e.target.value)}
                    disabled={!editing}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="¿Eres propietario?"
                    value={profileData.isOwner ? "Sí" : "No"}
                    disabled
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="Biografía"
                    multiline
                    rows={3}
                    value={profileData.bio}
                    onChange={(e) => handleChange("bio", e.target.value)}
                    disabled={!editing}
                  />
                </Grid>
              </Grid>

              <Box sx={{ mt: 3, display: "flex", justifyContent: "flex-end", gap: 2 }}>
                {editing ? (
                  <>
                    <Button variant="outlined" onClick={handleCancel}>
                      Cancelar
                    </Button>
                    <Button variant="contained" onClick={handleSave}>
                      Guardar cambios
                    </Button>
                  </>
                ) : (
                  <Button variant="contained" onClick={handleEdit}>
                    Editar perfil
                  </Button>
                )}
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Divider sx={{ my: 4 }} />

      {/* My Apartments */}
      <Typography variant="h6" component="h2" gutterBottom>
        Mis Apartamentos
      </Typography>
      
      <Grid container spacing={3}>
        {[1, 2].map((item) => (
          <Grid item xs={12} sm={6} md={4} key={item}>
            <Card>
              <Box sx={{ height: 150, bgcolor: "primary.light", display: "flex", alignItems: "center", justifyContent: "center" }}>
                <Typography variant="h6" color="primary.contrastText">
                  Imagen del apartamento
                </Typography>
              </Box>
              <CardContent>
                <Typography variant="h6" component="h3" gutterBottom>
                  Apartamento {item}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Edificio Central, Piso 5
                </Typography>
                <Box sx={{ mt: 2, display: "flex", justifyContent: "space-between" }}>
                  <Typography variant="h6" color="primary">
                    $1500/mes
                  </Typography>
                  <Button variant="outlined" size="small">
                    Editar
                  </Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default Profile;