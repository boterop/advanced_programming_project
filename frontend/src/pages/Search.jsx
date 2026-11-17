import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import {
  Alert, Box, Button, Checkbox, CircularProgress, Container, FormControl,
  FormControlLabel, Grid, InputLabel, MenuItem, Select, TextField, Typography,
} from "@mui/material";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import TuneRoundedIcon from "@mui/icons-material/TuneRounded";
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

const Search = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState(() => ({
    fechaEntrada: searchParams.get("fechaEntrada") || localDate(tomorrow),
    fechaSalida: searchParams.get("fechaSalida") || localDate(dayAfter),
    adultos: searchParams.get("adultos") || 1,
    ninos: searchParams.get("ninos") || 0,
    conMascota: searchParams.get("conMascota") === "true",
  }));
  const [apartments, setApartments] = useState([]);
  const [searchedApartmentId, setSearchedApartmentId] = useState(searchParams.get("apartamentoId") || "");
  const [nameFilter, setNameFilter] = useState("");
  const [capacityFilter, setCapacityFilter] = useState("all");
  const [petFilter, setPetFilter] = useState(false);
  const [sort, setSort] = useState("recommended");
  const [searched, setSearched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const queryString = searchParams.toString();
  useEffect(() => {
    const params = new URLSearchParams(queryString);
    const arrival = params.get("fechaEntrada");
    const departure = params.get("fechaSalida");
    if (!arrival || !departure) return undefined;

    let cancelled = false;
    const fetchApartments = async () => {
      setLoading(true);
      setError("");
      setSearched(true);
      setSearchedApartmentId(params.get("apartamentoId") || "");
      setFilters({
        fechaEntrada: arrival,
        fechaSalida: departure,
        adultos: params.get("adultos") || 1,
        ninos: params.get("ninos") || 0,
        conMascota: params.get("conMascota") === "true",
      });
      try {
        const query = new URLSearchParams({
          fechaEntrada: arrival,
          fechaSalida: departure,
          adultos: params.get("adultos") || "1",
          ninos: params.get("ninos") || "0",
          conMascota: params.get("conMascota") || "false",
          page: "0",
          size: "100",
        });
        const response = await api(`/apartamentos/disponibles?${query}`);
        if (!response.ok) throw new Error("No pudimos consultar la disponibilidad. Inténtalo de nuevo.");
        const page = await response.json();
        if (!cancelled) setApartments(page.content ?? []);
      } catch (requestError) {
        if (!cancelled) setError(requestError.message);
      } finally {
        if (!cancelled) setLoading(false);
      }
    };
    fetchApartments();
    return () => { cancelled = true; };
  }, [queryString]);

  const update = (event) => {
    const { name, value, checked, type } = event.target;
    setFilters((current) => ({ ...current, [name]: type === "checkbox" ? checked : value }));
  };

  const handleSearch = (event) => {
    event.preventDefault();
    if (filters.fechaSalida <= filters.fechaEntrada) {
      setError("La fecha de salida debe ser posterior a la entrada.");
      return;
    }
    if (Number(filters.adultos) < 1 || Number(filters.ninos) < 0) {
      setError("Indica al menos un adulto y revisa la cantidad de niños.");
      return;
    }
    setError("");
    const query = new URLSearchParams({
      ...filters,
      adultos: String(filters.adultos),
      ninos: String(filters.ninos),
      conMascota: String(filters.conMascota),
      ...(searchedApartmentId ? { apartamentoId: searchedApartmentId } : {}),
    });
    setSearchParams(query);
  };

  const visibleApartments = useMemo(() => {
    const filtered = apartments.filter((apartment) =>
      (!searchedApartmentId || apartment.id === searchedApartmentId)
      && apartment.nombre.toLocaleLowerCase().includes(nameFilter.trim().toLocaleLowerCase())
      && (capacityFilter === "all" || apartment.capacidad >= Number(capacityFilter))
      && (!petFilter || apartment.permiteMascotas),
    );
    if (sort === "capacity") return [...filtered].sort((a, b) => b.capacidad - a.capacidad);
    if (sort === "name") return [...filtered].sort((a, b) => a.nombre.localeCompare(b.nombre));
    return filtered;
  }, [apartments, capacityFilter, nameFilter, petFilter, searchedApartmentId, sort]);

  return (
    <Container maxWidth="xl" sx={{ py: { xs: 3, md: 5 } }}>
      <Box sx={{ mb: 3 }}>
        <Typography variant="overline" color="primary.main" sx={{ fontWeight: 800, letterSpacing: 1.4 }}>ENCUENTRA TU ESPACIO</Typography>
        <Typography variant="h3" component="h1" sx={{ fontWeight: 800, fontSize: { xs: "2rem", md: "2.8rem" } }}>Busca apartamentos</Typography>
        <Typography color="text.secondary" sx={{ mt: .75 }}>Selecciona las fechas y el tamaño de tu grupo para consultar disponibilidad real.</Typography>
      </Box>

      <Box component="form" onSubmit={handleSearch} sx={{ p: { xs: 2, md: 3 }, bgcolor: "background.paper", border: 1, borderColor: "divider", borderRadius: 4, boxShadow: "0 12px 35px rgba(17,48,55,.06)" }}>
        <Grid container spacing={1.5} alignItems="center">
          <Grid item xs={6} md={2.3}><TextField fullWidth required type="date" name="fechaEntrada" label="Entrada" value={filters.fechaEntrada} onChange={update} slotProps={{ inputLabel: { shrink: true } }} /></Grid>
          <Grid item xs={6} md={2.3}><TextField fullWidth required type="date" name="fechaSalida" label="Salida" value={filters.fechaSalida} onChange={update} slotProps={{ inputLabel: { shrink: true } }} /></Grid>
          <Grid item xs={6} md={1.8}><TextField fullWidth required type="number" name="adultos" label="Adultos" value={filters.adultos} onChange={update} slotProps={{ htmlInput: { min: 1 } }} /></Grid>
          <Grid item xs={6} md={1.8}><TextField fullWidth type="number" name="ninos" label="Niños" value={filters.ninos} onChange={update} slotProps={{ htmlInput: { min: 0 } }} /></Grid>
          <Grid item xs={12} md={2}>
            <FormControlLabel control={<Checkbox name="conMascota" checked={filters.conMascota} onChange={update} />} label="Llevo mascota" />
          </Grid>
          <Grid item xs={12} md={1.8}><Button type="submit" fullWidth size="large" variant="contained" startIcon={<SearchRoundedIcon />} disabled={loading} sx={{ minHeight: 56 }}>Buscar</Button></Grid>
        </Grid>
        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      </Box>

      {searched && <Box sx={{ mt: 4 }}>
        <Grid container spacing={2} alignItems="end" sx={{ mb: 2.5 }}>
          <Grid item xs={12} md={5}>
            <Typography variant="h5" component="h2" sx={{ fontWeight: 800 }}>Apartamentos disponibles</Typography>
            <Typography color="text.secondary" variant="body2" sx={{ mt: .5 }}>
              {loading ? "Consultando disponibilidad…" : `${visibleApartments.length} ${visibleApartments.length === 1 ? "opción encontrada" : "opciones encontradas"} para ${filters.fechaEntrada} – ${filters.fechaSalida}`}
            </Typography>
          </Grid>
          <Grid item xs={12} sm={6} md={3.2}><TextField fullWidth size="small" label="Filtrar por nombre" value={nameFilter} onChange={(event) => setNameFilter(event.target.value)} /></Grid>
          <Grid item xs={6} sm={3} md={1.8}>
            <FormControl fullWidth size="small"><InputLabel>Capacidad</InputLabel><Select label="Capacidad" value={capacityFilter} onChange={(event) => setCapacityFilter(event.target.value)}><MenuItem value="all">Cualquiera</MenuItem><MenuItem value="2">2+ huéspedes</MenuItem><MenuItem value="4">4+ huéspedes</MenuItem><MenuItem value="6">6+ huéspedes</MenuItem></Select></FormControl>
          </Grid>
          <Grid item xs={6} sm={3} md={2}>
            <FormControl fullWidth size="small"><InputLabel>Ordenar</InputLabel><Select label="Ordenar" value={sort} onChange={(event) => setSort(event.target.value)}><MenuItem value="recommended">Recomendados</MenuItem><MenuItem value="capacity">Mayor capacidad</MenuItem><MenuItem value="name">Nombre</MenuItem></Select></FormControl>
          </Grid>
          <Grid item xs={12}><FormControlLabel control={<Checkbox checked={petFilter} onChange={(event) => setPetFilter(event.target.checked)} />} label={<Box sx={{ display: "inline-flex", alignItems: "center", gap: .5 }}><TuneRoundedIcon fontSize="small" /> Solo apartamentos que admiten mascotas</Box>} /></Grid>
        </Grid>

        {loading && <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box>}
        {!loading && !error && visibleApartments.length === 0 && <Alert severity="info">No encontramos apartamentos con esos criterios. Prueba otras fechas o ajusta los filtros.</Alert>}
        {!loading && visibleApartments.length > 0 && <Grid container spacing={3}>
          {visibleApartments.map((apartment) => <Grid item xs={12} sm={6} lg={4} key={apartment.id}><ApartmentCard apartment={apartment} searchCriteria={filters} /></Grid>)}
        </Grid>}
      </Box>}
      {!searched && <Box sx={{ textAlign: "center", py: { xs: 5, md: 9 } }}>
        <Box sx={{ display: "grid", placeItems: "center", width: 64, height: 64, mx: "auto", mb: 2, borderRadius: 3, bgcolor: "rgba(23,98,79,.09)", color: "primary.main" }}><SearchRoundedIcon fontSize="large" /></Box>
        <Typography variant="h5" sx={{ fontWeight: 800 }}>Tu búsqueda empieza con las fechas</Typography>
        <Typography color="text.secondary" sx={{ mt: 1 }}>Elige entrada, salida y huéspedes para ver los espacios disponibles.</Typography>
      </Box>}
    </Container>
  );
};

export default Search;
