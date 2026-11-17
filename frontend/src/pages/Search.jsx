import { useState } from "react";
import {
  Alert, Box, Button, Checkbox, CircularProgress, Container, FormControlLabel,
  Grid, TextField, Typography,
} from "@mui/material";
import ApartmentCard from "../components/ApartmentCard";
import { api } from "../services/api.js";

const today = new Date();
const dateValue = (date) => {
  const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
  return local.toISOString().slice(0, 10);
};
const initialStart = new Date(today);
initialStart.setDate(today.getDate() + 1);
const initialEnd = new Date(today);
initialEnd.setDate(today.getDate() + 2);

const Search = () => {
  const [filters, setFilters] = useState({
    fechaEntrada: dateValue(initialStart),
    fechaSalida: dateValue(initialEnd),
    adultos: 1,
    ninos: 0,
    conMascota: false,
  });
  const [apartments, setApartments] = useState([]);
  const [searched, setSearched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const update = (event) => {
    const { name, value, checked, type } = event.target;
    setFilters((current) => ({ ...current, [name]: type === "checkbox" ? checked : value }));
  };

  const handleSearch = async (event) => {
    event.preventDefault();
    setError("");
    if (filters.fechaSalida <= filters.fechaEntrada) {
      setError("La fecha de salida debe ser posterior a la fecha de entrada.");
      return;
    }

    setLoading(true);
    setSearched(true);
    try {
      const query = new URLSearchParams({
        ...filters,
        adultos: String(filters.adultos),
        ninos: String(filters.ninos),
        conMascota: String(filters.conMascota),
        page: "0",
        size: "50",
      });
      const response = await api(`/apartamentos/disponibles?${query}`);
      if (!response.ok) throw new Error("No se pudieron buscar apartamentos disponibles.");
      const page = await response.json();
      setApartments(page.content ?? []);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>Buscar apartamentos</Typography>
      <Box component="form" onSubmit={handleSearch} sx={{ bgcolor: "background.paper", p: 3, borderRadius: 2, mb: 4 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={6} md={3}>
            <TextField fullWidth required type="date" name="fechaEntrada" label="Entrada" value={filters.fechaEntrada} onChange={update} slotProps={{ inputLabel: { shrink: true } }} />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <TextField fullWidth required type="date" name="fechaSalida" label="Salida" value={filters.fechaSalida} onChange={update} slotProps={{ inputLabel: { shrink: true } }} />
          </Grid>
          <Grid item xs={6} md={2}>
            <TextField fullWidth required type="number" name="adultos" label="Adultos" value={filters.adultos} onChange={update} slotProps={{ htmlInput: { min: 1 } }} />
          </Grid>
          <Grid item xs={6} md={2}>
            <TextField fullWidth type="number" name="ninos" label="Niños" value={filters.ninos} onChange={update} slotProps={{ htmlInput: { min: 0 } }} />
          </Grid>
          <Grid item xs={12} md={2}>
            <FormControlLabel control={<Checkbox name="conMascota" checked={filters.conMascota} onChange={update} />} label="Viajo con mascota" />
          </Grid>
          <Grid item xs={12}>
            <Button type="submit" variant="contained" disabled={loading}>Buscar disponibilidad</Button>
          </Grid>
        </Grid>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {loading && <Box sx={{ display: "flex", justifyContent: "center", py: 4 }}><CircularProgress /></Box>}
      {!loading && searched && !error && (
        <>
          <Typography variant="h6" gutterBottom>{apartments.length} resultados devueltos por el servidor</Typography>
          {apartments.length === 0 && <Alert severity="info">No hay apartamentos disponibles con esos criterios.</Alert>}
          <Grid container spacing={3}>
            {apartments.map((apartment) => (
              <Grid item xs={12} sm={6} md={4} key={apartment.identificacion}>
                <ApartmentCard apartment={apartment} />
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Container>
  );
};

export default Search;
