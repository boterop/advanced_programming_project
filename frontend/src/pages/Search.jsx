import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  Container,
  Grid,
  TextField,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Slider,
  Checkbox,
  FormControlLabel,
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Search = () => {
  const navigate = useNavigate();
  const [filters, setFilters] = useState({
    location: "",
    minPrice: 0,
    maxPrice: 3000,
    bedrooms: 0,
    amenities: [],
  });

  const handleFilterChange = (name, value) => {
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleAmenityChange = (amenity) => {
    setFilters(prev => ({
      ...prev,
      amenities: prev.amenities.includes(amenity)
        ? prev.amenities.filter(a => a !== amenity)
        : [...prev.amenities, amenity]
    }));
  };

  const handleSearch = () => {
    // In a real app, this would make an API call with filters
    console.log("Searching with filters:", filters);
    // For now, just navigate to results page
    navigate("/results");
  };

  const amenities = [
    "WiFi", "Parking", "Gym", "Pool", "Balcony", "Kitchen", 
    "Air Conditioning", "Heating", "Pet Friendly"
  ];

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom textAlign="center" mb={4}>
        Buscar apartamentos
      </Typography>

      <Grid container spacing={4}>
        {/* Filters Sidebar */}
        <Grid item xs={12} md={3}>
          <Box sx={{ bgcolor: "background.paper", p: 3, borderRadius: 2, height: "fit-content" }}>
            <Typography variant="h6" gutterBottom>
              Filtros
            </Typography>

            <TextField
              fullWidth
              label="Ubicación"
              margin="normal"
              value={filters.location}
              onChange={(e) => handleFilterChange("location", e.target.value)}
            />

            <Box sx={{ mt: 2 }}>
              <Typography gutterBottom>
                Precio: ${filters.minPrice} - ${filters.maxPrice}
              </Typography>
              <Slider
                value={[filters.minPrice, filters.maxPrice]}
                onChange={(e, newValue) => {
                  if (Array.isArray(newValue)) {
                    handleFilterChange("minPrice", newValue[0]);
                    handleFilterChange("maxPrice", newValue[1]);
                  }
                }}
                valueLabelDisplay="auto"
                min={0}
                max={3000}
                step={100}
              />
            </Box>

            <Box sx={{ mt: 2 }}>
              <Typography gutterBottom>
                Habitaciones
              </Typography>
              <FormControl fullWidth margin="normal">
                <InputLabel>Habitaciones</InputLabel>
                <Select
                  value={filters.bedrooms}
                  onChange={(e) => handleFilterChange("bedrooms", e.target.value)}
                >
                  <MenuItem value={0}>Cualquiera</MenuItem>
                  <MenuItem value={1}>1 habitación</MenuItem>
                  <MenuItem value={2}>2 habitaciones</MenuItem>
                  <MenuItem value={3}>3 habitaciones</MenuItem>
                  <MenuItem value={4}>4+ habitaciones</MenuItem>
                </Select>
              </FormControl>
            </Box>

            <Box sx={{ mt: 2 }}>
              <Typography gutterBottom>
                Amenidades
              </Typography>
              {amenities.map(amenity => (
                <FormControlLabel
                  key={amenity}
                  control={
                    <Checkbox
                      checked={filters.amenities.includes(amenity)}
                      onChange={() => handleAmenityChange(amenity)}
                    />
                  }
                  label={amenity}
                />
              ))}
            </Box>

            <Button
              variant="contained"
              fullWidth
              sx={{ mt: 2 }}
              onClick={handleSearch}
            >
              Aplicar filtros
            </Button>
          </Box>
        </Grid>

        {/* Search Results */}
        <Grid item xs={12} md={9}>
          <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
            <Typography variant="h6">
              Resultados de búsqueda
            </Typography>
            <Typography variant="body2">
              12 apartamentos encontrados
            </Typography>
          </Box>

          <Grid container spacing={3}>
            {[1, 2, 3, 4, 5, 6].map((item) => (
              <Grid item xs={12} sm={6} md={4} key={item}>
                <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
                  <CardMedia
                    component="img"
                    height="200"
                    image="https://images.unsplash.com/photo-1560185007-c58921c97a51?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80"
                    alt={`Apartamento ${item}`}
                  />
                  <CardContent sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                      <Typography gutterBottom variant="h6" component="h3">
                        Apartamento {item}
                      </Typography>
                      <Typography variant="h6" color="primary">
                        $1500/mes
                      </Typography>
                    </Box>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                      Ubicación: Edificio {item}
                    </Typography>
                    <Box sx={{ display: "flex", gap: 1, mt: 1 }}>
                      <Chip label="2 habitaciones" size="small" />
                      <Chip label="30 m²" size="small" />
                    </Box>
                    <Button
                      variant="outlined"
                      sx={{ mt: 2, alignSelf: "flex-start" }}
                      onClick={() => navigate(`/apartment/${item}`)}
                    >
                      Ver detalles
                    </Button>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Search;