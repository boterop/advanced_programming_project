import {
  Card,
  CardContent,
  CardMedia,
  Chip,
  Typography,
  Button,
  Box,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const ApartmentCard = ({ apartment }) => {
  const navigate = useNavigate();

  return (
    <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
      <CardMedia
        component="img"
        height="200"
        image={apartment.image}
        alt={apartment.title}
      />
      <CardContent sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
          <Typography gutterBottom variant="h6" component="h3">
            {apartment.title}
          </Typography>
          <Typography variant="h6" color="primary">
            ${apartment.price}/mes
          </Typography>
        </Box>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
          {apartment.location}
        </Typography>
        <Box sx={{ display: "flex", gap: 1, mt: 1, mb: 2 }}>
          <Chip label={`${apartment.bedrooms} habitaciones`} size="small" />
          <Chip label={`${apartment.area} m²`} size="small" />
        </Box>
        <Button
          variant="outlined"
          fullWidth
          onClick={() => navigate(`/apartment/${apartment.id}`)}
        >
          Ver detalles
        </Button>
      </CardContent>
    </Card>
  );
};

export default ApartmentCard;