import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import {
  Alert, Box, Button, Checkbox, Chip, CircularProgress, Container, Divider,
  FormControlLabel, Grid, List, ListItem, ListItemText, Paper, TextField, Typography,
} from "@mui/material";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import PetsOutlinedIcon from "@mui/icons-material/PetsOutlined";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import { api } from "../services/api.js";

const getLocalDate = (date = new Date()) => {
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${date.getFullYear()}-${month}-${day}`;
};
const tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
const nextDay = new Date(tomorrow);
nextDay.setDate(nextDay.getDate() + 1);

const ApartmentDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [apartment, setApartment] = useState(null);
  const [currentTariff, setCurrentTariff] = useState(null);
  const [currentSeason, setCurrentSeason] = useState(null);
  const [stay, setStay] = useState(() => ({
    fechaEntrada: location.state?.searchCriteria?.fechaEntrada || getLocalDate(tomorrow),
    fechaSalida: location.state?.searchCriteria?.fechaSalida || getLocalDate(nextDay),
    adultos: location.state?.searchCriteria?.adultos || 1,
    ninos: location.state?.searchCriteria?.ninos || 0,
    conMascota: location.state?.searchCriteria?.conMascota || false,
  }));
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    const load = async () => {
      try {
        const [apartmentResponse, tariffsResponse, accommodationResponse] = await Promise.all([
          api(`/apartamentos/${encodeURIComponent(id)}`),
          api(`/apartamentos/${encodeURIComponent(id)}/tarifas?page=0&size=100`),
          api("/alojamiento"),
        ]);
        if (!apartmentResponse.ok) throw new Error("No se encontró este apartamento.");
        const apartmentData = await apartmentResponse.json();
        const tariffPage = tariffsResponse.ok ? await tariffsResponse.json() : { content: [] };
        const accommodation = accommodationResponse.ok ? await accommodationResponse.json() : null;
        const today = getLocalDate();
        const seasons = accommodation?.temporadas ?? [];
        const seasonForToday = seasons.find((season) =>
          !season.esBase && season.fechaInicio <= today && today < season.fechaFin,
        ) ?? seasons.find((season) => season.esBase) ?? null;
        const tariffForToday = seasonForToday
          ? (tariffPage.content ?? [])
              .filter((tariff) => tariff.temporadaId === seasonForToday.id && tariff.vigenteDesde <= today)
              .sort((first, second) => second.vigenteDesde.localeCompare(first.vigenteDesde))[0] ?? null
          : null;
        if (!cancelled) {
          setApartment(apartmentData);
          setCurrentSeason(seasonForToday);
          setCurrentTariff(tariffForToday);
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

  const updateStay = (event) => {
    const { name, value, checked, type } = event.target;
    setStay((current) => ({ ...current, [name]: type === "checkbox" ? checked : value }));
  };

  const checkAvailability = (event) => {
    event.preventDefault();
    if (stay.fechaSalida <= stay.fechaEntrada) {
      setError("La salida debe ser posterior a la entrada.");
      return;
    }
    const params = new URLSearchParams({
      ...stay,
      adultos: String(stay.adultos),
      ninos: String(stay.ninos),
      conMascota: String(stay.conMascota),
      apartamentoId: id,
    });
    navigate(`/search?${params.toString()}`);
  };

  if (loading) return <Box sx={{ display: "flex", justifyContent: "center", py: 10 }}><CircularProgress /></Box>;

  return (
    <Container maxWidth="xl" sx={{ py: { xs: 2, md: 4 } }}>
      <Button startIcon={<ArrowBackRoundedIcon />} onClick={() => navigate(-1)} sx={{ mb: 2 }}>Volver a los apartamentos</Button>
      {error && !apartment && <Alert severity="error">{error}</Alert>}
      {apartment && <>
        <Paper sx={{ overflow: "hidden", borderRadius: 4, mb: 3 }}>
          <Box sx={{ position: "relative", height: { xs: 260, sm: 360, md: 480 }, bgcolor: "primary.dark" }}>
            <Box component="img" src={apartment.imagenUrl} alt={`Interior de ${apartment.nombre}`} sx={{ width: "100%", height: "100%", objectFit: "cover" }} />
            <Box sx={{ position: "absolute", inset: 0, background: "linear-gradient(0deg, rgba(8,31,36,.82), transparent 60%)" }} />
            <Box sx={{ position: "absolute", bottom: { xs: 20, md: 32 }, left: { xs: 20, md: 36 }, right: 20, color: "white" }}>
              <Typography variant="overline" sx={{ letterSpacing: 1.5, fontWeight: 800 }}>APARTAMENTO DEL EDIFICIO</Typography>
              <Typography variant="h2" component="h1" sx={{ fontWeight: 800, fontSize: { xs: "2rem", md: "3rem" } }}>{apartment.nombre}</Typography>
              <Box sx={{ display: "flex", gap: 1, flexWrap: "wrap", mt: 1.5 }}>
                <Chip label={`${apartment.capacidad} huéspedes`} sx={{ bgcolor: "rgba(255,255,255,.92)", fontWeight: 700 }} />
                <Chip label={`${apartment.dormitorios?.length ?? 0} habitaciones`} sx={{ bgcolor: "rgba(255,255,255,.92)", fontWeight: 700 }} />
                <Chip icon={<PetsOutlinedIcon />} label={apartment.permiteMascotas ? "Admite mascotas" : "No admite mascotas"} sx={{ bgcolor: "rgba(255,255,255,.92)", fontWeight: 700 }} />
              </Box>
            </Box>
          </Box>
        </Paper>

        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <Grid container spacing={3} alignItems="flex-start">
          <Grid item xs={12} md={7}>
            <Paper sx={{ p: { xs: 2.5, md: 4 }, borderRadius: 4 }}>
              <Typography variant="h5" component="h2" sx={{ fontWeight: 800 }}>Un espacio para sentirte como en casa</Typography>
              <Typography color="text.secondary" sx={{ mt: 1 }}>Revisa la distribución y las características del apartamento antes de consultar tus fechas.</Typography>
              <Divider sx={{ my: 3 }} />
              <Typography variant="h6" component="h3" sx={{ fontWeight: 800 }}>Habitaciones y camas</Typography>
              {apartment.dormitorios?.length ? <List disablePadding sx={{ mt: 1 }}>
                {apartment.dormitorios.map((room, index) => <ListItem key={`${room.descripcion}-${index}`} disableGutters divider>
                  <ListItemText primary={room.descripcion} secondary={room.tipoCama || "Configuración de cama por confirmar"} />
                </ListItem>)}
              </List> : <Typography color="text.secondary" sx={{ mt: 1 }}>La información de habitaciones aún no está disponible.</Typography>}
              <Divider sx={{ my: 3 }} />
              <Typography variant="h6" component="h3" sx={{ fontWeight: 800 }}>Antes de reservar</Typography>
              <Grid container spacing={2} sx={{ mt: .5 }}>
                <Grid item xs={12} sm={6}><Typography variant="body2" color="text.secondary">Capacidad máxima</Typography><Typography fontWeight={700}>{apartment.capacidad} huéspedes</Typography></Grid>
                <Grid item xs={12} sm={6}><Typography variant="body2" color="text.secondary">Mascotas</Typography><Typography fontWeight={700}>{apartment.permiteMascotas ? "Permitidas en este apartamento" : "No permitidas"}</Typography></Grid>
                <Grid item xs={12} sm={6}><Typography variant="body2" color="text.secondary">Estado de preparación</Typography><Typography fontWeight={700}>{apartment.estadoOperativo?.replaceAll("_", " ")}</Typography></Grid>
              </Grid>
            </Paper>
          </Grid>
          <Grid item xs={12} md={5}>
            <Paper component="form" onSubmit={checkAvailability} sx={{ p: { xs: 2.5, md: 3 }, borderRadius: 4, position: { md: "sticky" }, top: 88, boxShadow: "0 16px 40px rgba(17,48,55,.09)" }}>
              <Typography variant="h6" sx={{ fontWeight: 800 }}>Consulta tu estadía</Typography>
              <Typography color="text.secondary" variant="body2" sx={{ mt: .5 }}>Comprueba si este apartamento está disponible para tu grupo.</Typography>
              {currentTariff && <Box sx={{ mt: 2, p: 2, borderRadius: 2.5, bgcolor: "rgba(23,98,79,.08)" }}>
                <Typography variant="overline" color="primary.dark" sx={{ fontWeight: 800 }}>TARIFA VIGENTE HOY · {currentSeason?.nombre}</Typography>
                <Typography variant="h5" sx={{ fontWeight: 800 }}>{currentTariff.valorPorNoche} <Typography component="span" variant="body2" color="text.secondary">por noche</Typography></Typography>
              </Box>}
              <Grid container spacing={1.5} sx={{ mt: 1 }}>
                <Grid item xs={6}><TextField fullWidth required type="date" name="fechaEntrada" label="Entrada" value={stay.fechaEntrada} onChange={updateStay} slotProps={{ inputLabel: { shrink: true } }} /></Grid>
                <Grid item xs={6}><TextField fullWidth required type="date" name="fechaSalida" label="Salida" value={stay.fechaSalida} onChange={updateStay} slotProps={{ inputLabel: { shrink: true } }} /></Grid>
                <Grid item xs={6}><TextField fullWidth type="number" name="adultos" label="Adultos" value={stay.adultos} onChange={updateStay} slotProps={{ htmlInput: { min: 1 } }} /></Grid>
                <Grid item xs={6}><TextField fullWidth type="number" name="ninos" label="Niños" value={stay.ninos} onChange={updateStay} slotProps={{ htmlInput: { min: 0 } }} /></Grid>
                <Grid item xs={12}><FormControlLabel control={<Checkbox name="conMascota" checked={stay.conMascota} onChange={updateStay} disabled={!apartment.permiteMascotas} />} label="Viajo con mascota" /></Grid>
                <Grid item xs={12}><Button fullWidth type="submit" variant="contained" size="large" startIcon={<SearchRoundedIcon />} sx={{ py: 1.4 }}>Comprobar disponibilidad</Button></Grid>
              </Grid>
            </Paper>
          </Grid>
        </Grid>
      </>}
    </Container>
  );
};

export default ApartmentDetails;
