import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Alert, Box, Button, Card, Chip, CircularProgress, Container, FormControlLabel,
  Grid, Paper, TextField, Typography, Checkbox,
} from "@mui/material";
import ApartmentOutlinedIcon from "@mui/icons-material/ApartmentOutlined";
import CalendarMonthOutlinedIcon from "@mui/icons-material/CalendarMonthOutlined";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import PetsOutlinedIcon from "@mui/icons-material/PetsOutlined";
import ScheduleOutlinedIcon from "@mui/icons-material/ScheduleOutlined";
import ApartmentCard from "../components/ApartmentCard";
import { api } from "../services/api.js";

const localDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

const tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
const dayAfter = new Date(tomorrow);
dayAfter.setDate(dayAfter.getDate() + 1);

const Home = () => {
  const navigate = useNavigate();
  const [apartments, setApartments] = useState([]);
  const [accommodation, setAccommodation] = useState(null);
  const [filters, setFilters] = useState({
    fechaEntrada: localDate(tomorrow),
    fechaSalida: localDate(dayAfter),
    adultos: 1,
    ninos: 0,
    conMascota: false,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    Promise.all([api("/apartamentos?page=0&size=100"), api("/alojamiento")])
      .then(async ([apartmentsResponse, accommodationResponse]) => {
        if (!apartmentsResponse.ok) throw new Error("No se pudieron cargar los apartamentos.");
        const [apartmentPage, lodging] = await Promise.all([
          apartmentsResponse.json(),
          accommodationResponse.ok ? accommodationResponse.json() : null,
        ]);
        if (!cancelled) {
          setApartments(apartmentPage.content ?? []);
          setAccommodation(lodging);
        }
      })
      .catch((requestError) => {
        if (!cancelled) setError(requestError.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => { cancelled = true; };
  }, []);

  const update = (event) => {
    const { name, value, checked, type } = event.target;
    setFilters((current) => ({ ...current, [name]: type === "checkbox" ? checked : value }));
  };

  const search = (event) => {
    event.preventDefault();
    if (filters.fechaSalida <= filters.fechaEntrada) {
      setError("La salida debe ser después de la entrada.");
      return;
    }
    setError("");
    const query = new URLSearchParams({
      ...filters,
      adultos: String(filters.adultos),
      ninos: String(filters.ninos),
      conMascota: String(filters.conMascota),
    });
    navigate(`/search?${query.toString()}`);
  };

  const activeApartments = apartments.filter((apartment) => apartment.activo);

  return (
    <Box sx={{ pb: { xs: 5, md: 9 } }}>
      <Box sx={{
        position: "relative", overflow: "hidden", color: "common.white",
        background: `linear-gradient(105deg, rgba(8,36,42,.96) 0%, rgba(10,62,70,.86) 55%, rgba(10,62,70,.25) 100%), url('${apartments[0]?.imagenUrl ?? "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=2200&q=85"}') center 48%/cover`,
      }}>
        <Container maxWidth="xl" sx={{ position: "relative", py: { xs: 5, md: 9 } }}>
          <Grid container spacing={{ xs: 4, md: 7 }} alignItems="center">
            <Grid item xs={12} md={6}>
              <Chip label="ESTADÍAS EN EL EDIFICIO" sx={{ bgcolor: "rgba(255,255,255,.14)", color: "white", fontWeight: 700, letterSpacing: 1, mb: 2 }} />
              <Typography variant="h2" component="h1" sx={{ fontWeight: 800, lineHeight: 1.05, fontSize: { xs: "2.55rem", md: "4.2rem" }, maxWidth: 680 }}>
                {accommodation?.nombre ?? "Encuentra tu próximo apartamento"}
              </Typography>
              <Typography sx={{ mt: 2, maxWidth: 560, color: "rgba(255,255,255,.82)", fontSize: { xs: "1rem", md: "1.15rem" } }}>
                Explora los espacios del edificio, revisa lo que ofrece cada uno y encuentra las fechas que se ajustan a tu viaje.
              </Typography>
              {accommodation?.ciudad && <Typography sx={{ mt: 1, color: "rgba(255,255,255,.72)" }}>{accommodation.ciudad}</Typography>}
            </Grid>
            <Grid item xs={12} md={6}>
              <Paper component="form" onSubmit={search} elevation={12} sx={{ p: { xs: 2, md: 3 }, borderRadius: 4, color: "text.primary" }}>
                <Typography variant="h5" sx={{ fontWeight: 800 }}>Encuentra tu estadía</Typography>
                <Typography color="text.secondary" sx={{ mt: .5, mb: 2.5 }}>Busca apartamentos disponibles para tus fechas.</Typography>
                <Grid container spacing={1.5}>
                  <Grid item xs={6}>
                    <TextField fullWidth required type="date" label="Entrada" name="fechaEntrada" value={filters.fechaEntrada} onChange={update} slotProps={{ inputLabel: { shrink: true } }} />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField fullWidth required type="date" label="Salida" name="fechaSalida" value={filters.fechaSalida} onChange={update} slotProps={{ inputLabel: { shrink: true } }} />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField fullWidth type="number" label="Adultos" name="adultos" value={filters.adultos} onChange={update} slotProps={{ htmlInput: { min: 1 } }} />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField fullWidth type="number" label="Niños" name="ninos" value={filters.ninos} onChange={update} slotProps={{ htmlInput: { min: 0 } }} />
                  </Grid>
                  <Grid item xs={12}>
                    <FormControlLabel control={<Checkbox name="conMascota" checked={filters.conMascota} onChange={update} />} label="Viajo con mascota" />
                  </Grid>
                  {error && <Grid item xs={12}><Alert severity="error">{error}</Alert></Grid>}
                  <Grid item xs={12}>
                    <Button type="submit" fullWidth size="large" variant="contained" startIcon={<SearchRoundedIcon />} sx={{ py: 1.4, borderRadius: 2.5 }}>Buscar apartamentos</Button>
                  </Grid>
                </Grid>
              </Paper>
            </Grid>
          </Grid>
          <Grid container spacing={1.5} sx={{ mt: { xs: 3, md: 5 } }}>
            <Grid item xs={12} sm={4}><Box sx={{ display: "flex", alignItems: "center", gap: 1.2 }}><ApartmentOutlinedIcon /><Typography>{activeApartments.length} apartamentos en el edificio</Typography></Box></Grid>
            {accommodation?.parametros?.horaEntrada && <Grid item xs={12} sm={4}><Box sx={{ display: "flex", alignItems: "center", gap: 1.2 }}><ScheduleOutlinedIcon /><Typography>Entrada desde las {String(accommodation.parametros.horaEntrada).slice(0, 5)}</Typography></Box></Grid>}
            {activeApartments.some((apartment) => apartment.permiteMascotas) && <Grid item xs={12} sm={4}><Box sx={{ display: "flex", alignItems: "center", gap: 1.2 }}><PetsOutlinedIcon /><Typography>Opciones que admiten mascotas</Typography></Box></Grid>}
          </Grid>
        </Container>
      </Box>

      <Container maxWidth="xl" sx={{ pt: { xs: 5, md: 7 } }}>
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: { xs: "flex-start", sm: "end" }, gap: 2, mb: 3 }}>
          <Box>
            <Typography variant="overline" color="primary.main" sx={{ fontWeight: 800, letterSpacing: 1.5 }}>ESPACIOS DEL EDIFICIO</Typography>
            <Typography variant="h4" component="h2" sx={{ fontWeight: 800 }}>Apartamentos para explorar</Typography>
            <Typography color="text.secondary" sx={{ mt: .5 }}>Compara capacidad, habitaciones y políticas para elegir con confianza.</Typography>
          </Box>
          <Button endIcon={<SearchRoundedIcon />} onClick={() => navigate("/search")} sx={{ flexShrink: 0, display: { xs: "none", sm: "inline-flex" } }}>Ver disponibilidad</Button>
        </Box>
        {loading && <Box sx={{ display: "flex", justifyContent: "center", py: 6 }}><CircularProgress /></Box>}
        {error && !loading && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        {!loading && !error && activeApartments.length === 0 && <Alert severity="info">Aún no hay apartamentos publicados en el edificio.</Alert>}
        <Grid container spacing={3}>
          {activeApartments.slice(0, 6).map((apartment) => (
            <Grid item xs={12} sm={6} lg={4} key={apartment.id}><ApartmentCard apartment={apartment} /></Grid>
          ))}
        </Grid>
        {!loading && activeApartments.length > 6 && <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}><Button variant="outlined" size="large" onClick={() => navigate("/search")}>Explorar todos los apartamentos</Button></Box>}
        <Card sx={{ mt: { xs: 5, md: 8 }, p: { xs: 2.5, md: 4 }, borderRadius: 4, bgcolor: "primary.dark", color: "white", backgroundImage: "linear-gradient(110deg, rgba(7,63,73,.98), rgba(11,93,107,.85))" }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} md={8}>
              <Typography variant="h5" sx={{ fontWeight: 800 }}>¿Ya tienes las fechas de tu viaje?</Typography>
              <Typography sx={{ color: "rgba(255,255,255,.78)", mt: .5 }}>Comprueba disponibilidad según huéspedes y necesidades de tu grupo.</Typography>
            </Grid>
            <Grid item xs={12} md={4} sx={{ textAlign: { xs: "left", md: "right" } }}>
              <Button variant="contained" color="secondary" size="large" startIcon={<CalendarMonthOutlinedIcon />} onClick={() => navigate("/search")}>Consultar fechas</Button>
            </Grid>
          </Grid>
        </Card>
      </Container>
    </Box>
  );
};

export default Home;
