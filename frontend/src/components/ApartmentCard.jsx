import { Box, Button, Card, CardContent, CardMedia, Chip, Divider, Typography } from "@mui/material";
import { useLocation, useNavigate } from "react-router-dom";
import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";
import BedOutlinedIcon from "@mui/icons-material/BedOutlined";
import Groups2OutlinedIcon from "@mui/icons-material/Groups2Outlined";
import PetsOutlinedIcon from "@mui/icons-material/PetsOutlined";

const ApartmentCard = ({ apartment, searchCriteria }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const bedrooms = apartment.dormitorios ?? [];

  return (
    <Card sx={{ height: "100%", display: "flex", flexDirection: "column", borderRadius: 3, overflow: "hidden", transition: "transform .2s ease, box-shadow .2s ease", "&:hover": { transform: "translateY(-4px)", boxShadow: "0 18px 42px rgba(20,48,43,.14)" } }}>
      <Box sx={{ position: "relative", overflow: "hidden" }}>
        <CardMedia
          component="img"
          height="230"
          image={apartment.imagenUrl}
          alt={`Interior de ${apartment.nombre}`}
          sx={{ objectFit: "cover", transition: "transform .45s ease", "&:hover": { transform: "scale(1.04)" } }}
        />
        <Chip label="EN EL EDIFICIO" size="small" sx={{ position: "absolute", top: 14, left: 14, bgcolor: "rgba(255,255,255,.94)", fontWeight: 800, letterSpacing: .5 }} />
        {!apartment.activo && <Chip label="No disponible" size="small" color="default" sx={{ position: "absolute", top: 14, right: 14 }} />}
      </Box>
      <CardContent sx={{ flexGrow: 1, display: "flex", flexDirection: "column", p: 2.5 }}>
        <Typography gutterBottom variant="h6" component="h2" sx={{ fontWeight: 800, lineHeight: 1.25, minHeight: 50 }}>
          {apartment.nombre}
        </Typography>
        <Box sx={{ display: "flex", gap: 2.2, flexWrap: "wrap", alignItems: "center", mb: 2, color: "text.secondary" }}>
          <Box sx={{ display: "inline-flex", alignItems: "center", gap: .6 }}><Groups2OutlinedIcon fontSize="small" /><Typography variant="body2">{apartment.capacidad} huéspedes</Typography></Box>
          <Box sx={{ display: "inline-flex", alignItems: "center", gap: .6 }}><BedOutlinedIcon fontSize="small" /><Typography variant="body2">{bedrooms.length} habitaciones</Typography></Box>
        </Box>
        <Divider sx={{ mb: 1.5 }} />
        <Box sx={{ display: "flex", alignItems: "center", gap: .75, mb: 2, color: apartment.permiteMascotas ? "primary.main" : "text.secondary" }}>
          <PetsOutlinedIcon fontSize="small" />
          <Typography variant="body2" fontWeight={600}>{apartment.permiteMascotas ? "Admite mascotas" : "No admite mascotas"}</Typography>
        </Box>
        <Button
          variant="contained"
          fullWidth
          sx={{ mt: "auto" }}
          endIcon={<ArrowForwardRoundedIcon />}
          disabled={!apartment.activo}
          onClick={() => navigate(`/apartment/${encodeURIComponent(apartment.id)}`, {
            state: { searchCriteria, from: location.pathname },
          })}
        >
          Ver apartamento
        </Button>
      </CardContent>
    </Card>
  );
};

export default ApartmentCard;
