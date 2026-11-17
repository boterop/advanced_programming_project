import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Alert, Box, Button, Chip, CircularProgress, Container, Divider, List, ListItem, ListItemText, Paper, Typography } from "@mui/material";
import { api } from "../services/api.js";

const ApartmentDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [apartment, setApartment] = useState(null);
  const [tariffs, setTariffs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    const load = async () => {
      try {
        const [apartmentResponse, tariffsResponse] = await Promise.all([
          api(`/apartamentos/${encodeURIComponent(id)}`),
          api(`/apartamentos/${encodeURIComponent(id)}/tarifas?page=0&size=100`),
        ]);
        if (!apartmentResponse.ok) throw new Error("No se encontró el apartamento.");
        const apartmentData = await apartmentResponse.json();
        const tariffPage = tariffsResponse.ok ? await tariffsResponse.json() : { content: [] };
        if (!cancelled) {
          setApartment(apartmentData);
          setTariffs(tariffPage.content ?? []);
        }
      } catch (requestError) {
        if (!cancelled) setError(requestError.message);
      } finally {
        if (!cancelled) setLoading(false);
      }
    };
    load();
    return () => { cancelled = true; };
  }, [id]);

  if (loading) return <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box>;

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Button variant="text" onClick={() => navigate(-1)} sx={{ mb: 2 }}>← Volver</Button>
      {error && <Alert severity="error">{error}</Alert>}
      {apartment && (
        <Paper sx={{ p: { xs: 2, md: 4 } }}>
          <Box
            component="img"
            src={apartment.imagenUrl}
            alt={`Imagen de ${apartment.nombre}`}
            sx={{ width: "100%", height: { xs: 220, md: 400 }, objectFit: "cover", borderRadius: 2, mb: 3 }}
          />
          <Typography variant="h4" component="h1" gutterBottom>{apartment.nombre}</Typography>
          <Typography color="text.secondary" gutterBottom>ID: {apartment.id}</Typography>
          <Box sx={{ display: "flex", gap: 1, flexWrap: "wrap", my: 2 }}>
            <Chip label={`Capacidad: ${apartment.capacidad}`} />
            <Chip label={apartment.activo ? "Activo" : "Inactivo"} color={apartment.activo ? "success" : "default"} />
            <Chip label={`Estado: ${apartment.estadoOperativo}`} />
            <Chip label={apartment.permiteMascotas ? "Admite mascotas" : "No admite mascotas"} />
          </Box>
          <Divider sx={{ my: 3 }} />
          <Typography variant="h6" component="h2">Dormitorios</Typography>
          {apartment.dormitorios?.length ? (
            <List>
              {apartment.dormitorios.map((room, index) => (
                <ListItem key={`${room.descripcion}-${index}`} disableGutters>
                  <ListItemText primary={room.descripcion} secondary={room.tipoCama} />
                </ListItem>
              ))}
            </List>
          ) : <Typography color="text.secondary" sx={{ mt: 1 }}>El backend no reporta dormitorios.</Typography>}
          <Divider sx={{ my: 3 }} />
          <Typography variant="h6" component="h2">Tarifas registradas</Typography>
          {tariffs.length ? (
            <List>
              {tariffs.map((tariff) => (
                <ListItem key={tariff.id} disableGutters>
                  <ListItemText
                    primary={`${tariff.valorPorNoche} por noche`}
                    secondary={`Vigente desde ${tariff.vigenteDesde} · Temporada ${tariff.temporadaId}`}
                  />
                </ListItem>
              ))}
            </List>
          ) : <Typography color="text.secondary" sx={{ mt: 1 }}>No hay tarifas registradas para este apartamento.</Typography>}
        </Paper>
      )}
    </Container>
  );
};

export default ApartmentDetails;
