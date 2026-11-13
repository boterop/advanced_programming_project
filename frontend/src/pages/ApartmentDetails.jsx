import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  Container,
  Grid,
  Typography,
  Rating,
  Divider,
  List,
  ListItem,
  ListItemText,
  Avatar,
  Paper,
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const ApartmentDetails = () => {
  const navigate = useNavigate();
  const [selectedImage, setSelectedImage] = useState(0);
  
  // Mock apartment data
  const apartment = {
    id: 1,
    title: "Apartamento Moderno 2 Habitaciones",
    price: 1500,
    location: "Edificio Central, Piso 5",
    bedrooms: 2,
    bathrooms: 2,
    area: 80,
    description: "Excelente apartamento ubicado en el edificio central. Tiene vista panorámica al parque y acceso al gimnasio del edificio. Perfecto para una familia o pareja.",
    amenities: ["WiFi", "Gym", "Balcony", "Kitchen", "Air Conditioning", "Heating"],
    owner: {
      name: "María González",
      rating: 4.8,
      reviews: 120,
      avatar: "https://images.unsplash.com/photo-1494790108377-be9c253d107f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80"
    },
    images: [
      "https://images.unsplash.com/photo-1560185007-c58921c97a51?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80",
      "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80",
      "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80"
    ],
    reviews: [
      {
        id: 1,
        user: "Carlos Rodríguez",
        rating: 5,
        comment: "Excelente apartamento, muy limpio y bien ubicado.",
        date: "2023-10-15"
      },
      {
        id: 2,
        user: "Ana Martínez",
        rating: 4,
        comment: "Muy buen ambiente, solo que el ruido del edificio es un poco fuerte.",
        date: "2023-09-20"
      }
    ]
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 2 }}>
        <Button variant="text" onClick={() => navigate(-1)}>
          ← Volver a resultados
        </Button>
      </Box>

      <Grid container spacing={4}>
        {/* Main Image */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardMedia
              component="img"
              height="500"
              image={apartment.images[selectedImage]}
              alt={apartment.title}
            />
            <Box sx={{ display: "flex", gap: 1, p: 2, overflowX: "auto" }}>
              {apartment.images.map((image, index) => (
                <Box
                  key={index}
                  sx={{
                    minWidth: 100,
                    height: 80,
                    borderRadius: 1,
                    overflow: "hidden",
                    cursor: "pointer",
                    border: selectedImage === index ? "2px solid #1976d2" : "none"
                  }}
                  onClick={() => setSelectedImage(index)}
                >
                  <CardMedia
                    component="img"
                    height="80"
                    image={image}
                    alt={`Vista ${index + 1}`}
                  />
                </Box>
              ))}
            </Box>
          </Card>
        </Grid>

        {/* Booking Info */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, height: "100%" }}>
            <Typography variant="h5" component="h2" gutterBottom>
              {apartment.title}
            </Typography>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 1 }}>
              <Typography variant="h6" color="primary">
                ${apartment.price}/mes
              </Typography>
              <Chip label="Disponible" color="success" size="small" />
            </Box>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              {apartment.location}
            </Typography>

            <Box sx={{ display: "flex", gap: 2, mb: 2 }}>
              <Chip label={`${apartment.bedrooms} habitaciones`} />
              <Chip label={`${apartment.bathrooms} baños`} />
              <Chip label={`${apartment.area} m²`} />
            </Box>

            <Typography variant="body1" sx={{ mb: 2 }}>
              {apartment.description}
            </Typography>

            <Box sx={{ mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Amenidades
              </Typography>
              <Grid container spacing={1}>
                {apartment.amenities.map((amenity, index) => (
                  <Grid item key={index}>
                    <Chip label={amenity} size="small" />
                  </Grid>
                ))}
              </Grid>
            </Box>

            <Button
              variant="contained"
              fullWidth
              size="large"
              sx={{ py: 1.5 }}
              onClick={() => alert("Reserva realizada exitosamente!")}
            >
              Reservar ahora
            </Button>
          </Paper>
        </Grid>
      </Grid>

      {/* Owner Info */}
      <Box sx={{ mt: 4 }}>
        <Typography variant="h5" component="h2" gutterBottom>
          Propietario
        </Typography>
        <Paper sx={{ p: 3, display: "flex", alignItems: "center", gap: 2 }}>
          <Avatar
            src={apartment.owner.avatar}
            alt={apartment.owner.name}
            sx={{ width: 60, height: 60 }}
          />
          <Box>
            <Typography variant="h6" component="h3">
              {apartment.owner.name}
            </Typography>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <Rating value={apartment.owner.rating} readOnly size="small" />
              <Typography variant="body2">
                {apartment.owner.rating} ({apartment.owner.reviews} reseñas)
              </Typography>
            </Box>
          </Box>
          <Button variant="outlined" sx={{ ml: "auto" }}>
            Contactar
          </Button>
        </Paper>
      </Box>

      {/* Reviews */}
      <Box sx={{ mt: 4 }}>
        <Typography variant="h5" component="h2" gutterBottom>
          Reseñas ({apartment.reviews.length})
        </Typography>
        <List>
          {apartment.reviews.map((review) => (
            <ListItem key={review.id} sx={{ py: 2, borderBottom: "1px solid #eee" }}>
              <Box sx={{ flex: 1 }}>
                <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1 }}>
                  <Typography variant="h6">{review.user}</Typography>
                  <Rating value={review.rating} readOnly size="small" />
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {review.comment}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  {review.date}
                </Typography>
              </Box>
            </ListItem>
          ))}
        </List>
      </Box>
    </Container>
  );
};

export default ApartmentDetails;