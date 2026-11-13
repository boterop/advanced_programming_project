import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  CircularProgress,
  Container,
  Grid,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

const Home = () => {
  const navigate = useNavigate();

  return (
    <Box sx={{ bgcolor: "background.default", minHeight: "100vh", py: 4 }}>
      {/* Hero Section */}
      <Box
        sx={{
          backgroundImage: "url('https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          height: "70vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "white",
          textAlign: "center",
          position: "relative",
        }}
      >
        <Box
          sx={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: "rgba(0,0,0,0.5)",
          }}
        />
        <Container maxWidth="lg" sx={{ position: "relative", zIndex: 1 }}>
          <Typography variant="h2" component="h1" gutterBottom>
            Encuentra tu nuevo hogar
          </Typography>
          <Typography variant="h5" gutterBottom>
            Alquiler de apartamentos en UQBNB
          </Typography>
          <Button
            variant="contained"
            size="large"
            sx={{ mt: 3, px: 4, py: 2 }}
            onClick={() => navigate("/search")}
          >
            Buscar apartamentos
          </Button>
        </Container>
      </Box>

      {/* Search Section */}
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box sx={{ display: "flex", justifyContent: "center", mb: 4 }}>
          <TextField
            fullWidth
            variant="outlined"
            placeholder="Buscar por ubicación, precio, tipo..."
            sx={{ maxWidth: 600 }}
            InputProps={{
              endAdornment: <SearchIcon />
            }}
          />
        </Box>
      </Container>

      {/* Featured Apartments */}
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h4" component="h2" gutterBottom textAlign="center" mb={4}>
          Apartamentos destacados
        </Typography>
        
        <Grid container spacing={4}>
          {[1, 2, 3, 4].map((item) => (
            <Grid item xs={12} sm={6} md={3} key={item}>
              <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
                <CardMedia
                  component="img"
                  height="200"
                  image="https://images.unsplash.com/photo-1560185007-c58921c97a51?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80"
                  alt={`Apartamento ${item}`}
                />
                <CardContent sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
                  <Typography gutterBottom variant="h6" component="h3">
                    Apartamento {item}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ flexGrow: 1 }}>
                    Descripción del apartamento {item}
                  </Typography>
                  <Box sx={{ mt: 2, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Chip label="2 habitaciones" color="primary" />
                    <Typography variant="h6" color="primary">
                      $1500/mes
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>
    </Box>
  );
};

export default Home;