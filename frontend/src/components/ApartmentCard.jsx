import { Box, Button, Card, CardContent, CardMedia, Chip, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";

const ApartmentCard = ({ apartment }) => {
  const navigate = useNavigate();
  const bedrooms = apartment.dormitorios ?? [];

  return (
    <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
      <CardMedia
        component="img"
        height="200"
        image={apartment.imagenUrl}
        alt={`Imagen de ${apartment.nombre}`}
        sx={{ objectFit: "cover" }}
      />
      <CardContent sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        <Typography gutterBottom variant="h6" component="h2">
          {apartment.nombre}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          ID: {apartment.id}
        </Typography>
        <Box sx={{ display: "flex", gap: 1, flexWrap: "wrap", mb: 2 }}>
          <Chip label={`Capacidad: ${apartment.capacidad}`} size="small" />
          <Chip label={`${bedrooms.length} dormitorios`} size="small" />
          <Chip
            label={apartment.permiteMascotas ? "Admite mascotas" : "No admite mascotas"}
            size="small"
          />
        </Box>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Estado: {apartment.estadoOperativo}
        </Typography>
        <Button
          variant="outlined"
          fullWidth
          sx={{ mt: "auto" }}
          onClick={() => navigate(`/apartment/${encodeURIComponent(apartment.id)}`)}
        >
          Ver detalles
        </Button>
      </CardContent>
    </Card>
  );
};

export default ApartmentCard;
