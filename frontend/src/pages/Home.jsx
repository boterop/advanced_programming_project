import { useEffect, useState } from "react";
import { Alert, Box, CircularProgress, Container, Grid, Typography } from "@mui/material";
import ApartmentCard from "../components/ApartmentCard";
import { api } from "../services/api.js";

const Home = () => {
  const [apartments, setApartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    api("/apartamentos?page=0&size=8")
      .then(async (response) => {
        if (!response.ok) throw new Error("No se pudieron cargar los apartamentos.");
        return response.json();
      })
      .then((page) => {
        if (!cancelled) setApartments(page.content ?? []);
      })
      .catch((requestError) => {
        if (!cancelled) setError(requestError.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => { cancelled = true; };
  }, []);

  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <Box sx={{ py: { xs: 4, md: 7 }, textAlign: "center" }}>
        <Typography variant="h2" component="h1" gutterBottom>
          Encuentra tu próximo alojamiento
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Apartamentos registrados en UQBNB
        </Typography>
      </Box>

      <Typography variant="h4" component="h2" gutterBottom>
        Apartamentos
      </Typography>
      {loading && <Box sx={{ display: "flex", justifyContent: "center", py: 5 }}><CircularProgress /></Box>}
      {error && <Alert severity="error">{error}</Alert>}
      {!loading && !error && apartments.length === 0 && (
        <Alert severity="info">El backend todavía no tiene apartamentos registrados.</Alert>
      )}
      <Grid container spacing={3} sx={{ mt: 1 }}>
        {apartments.map((apartment) => (
          <Grid item xs={12} sm={6} md={4} key={apartment.id}>
            <ApartmentCard apartment={apartment} />
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default Home;
