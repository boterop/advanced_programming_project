import { useCallback, useEffect, useState } from "react";
import {
  Alert, Box, Button, Card, CardContent, Checkbox, CircularProgress, Dialog, DialogActions,
  DialogContent, DialogTitle, FormControl,
  FormControlLabel, Grid, InputLabel, MenuItem, Select, Tab, Tabs, Table, TableBody,
  TableCell, TableHead, TableRow, TextField, Typography,
} from "@mui/material";
import { api } from "../services/api.js";

const today = new Date().toISOString().slice(0, 10);
const defaultPolicy = [
  { antelacionMinimaHoras: 168, porcentajeRetencion: 0 },
  { antelacionMinimaHoras: 72, porcentajeRetencion: 0.5 },
  { antelacionMinimaHoras: 0, porcentajeRetencion: 1 },
];

const AdminDashboard = () => {
  const [tab, setTab] = useState(0);
  const [apartments, setApartments] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [accommodation, setAccommodation] = useState(null);
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [apartmentForm, setApartmentForm] = useState({ nombre: "", capacidad: 2, dormitorios: 1, permiteMascotas: false });
  const [seasonForm, setSeasonForm] = useState({ nombre: "", fechaInicio: today, fechaFin: today });
  const [tariffForm, setTariffForm] = useState({ apartamentoId: "", temporadaId: "", valorPorNoche: "", vigenteDesde: today });
  const [policyDate, setPolicyDate] = useState(today);
  const [folio, setFolio] = useState(null);
  const [folioLoading, setFolioLoading] = useState(false);
  const [paymentForm, setPaymentForm] = useState({ valor: "", medioPago: "TRANSFERENCIA", referencia: "" });

  const loadData = useCallback(async () => {
    setError("");
    try {
      const responses = await Promise.all([
        api("/apartamentos?page=0&size=100"),
        api("/reservas?page=0&size=100"),
        api("/alojamiento"),
        api("/alojamiento/politica-cancelacion?page=0&size=100"),
      ]);
      const failed = responses.find((response) => !response.ok);
      if (failed) throw new Error("No se pudieron cargar todos los datos administrativos.");
      const [apartmentPage, reservationPage, lodging, policyPage] = await Promise.all(
        responses.map((response) => response.json()),
      );
      setApartments(apartmentPage.content ?? []);
      setReservations(reservationPage.content ?? []);
      setAccommodation(lodging);
      setPolicies(policyPage.content ?? []);
      setTariffForm((current) => ({
        ...current,
        apartamentoId: current.apartamentoId || apartmentPage.content?.[0]?.id || "",
        temporadaId: current.temporadaId || lodging.temporadas?.[0]?.id || "",
      }));
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let active = true;
    Promise.resolve().then(() => active && loadData());
    return () => { active = false; };
  }, [loadData]);

  const submit = async (path, body, successText, method = "POST") => {
    setBusy(true);
    setError("");
    setMessage("");
    try {
      const response = await api(path, {
        method,
        ...(body === undefined ? {} : { body: JSON.stringify(body) }),
      });
      if (!response.ok) throw new Error("El backend rechazó la operación administrativa.");
      setMessage(successText);
      await loadData();
      return true;
    } catch (requestError) {
      setError(requestError.message);
      return false;
    } finally {
      setBusy(false);
    }
  };

  const createApartment = async (event) => {
    event.preventDefault();
    const bedrooms = Number(apartmentForm.dormitorios);
    const created = await submit("/apartamentos", {
      nombre: apartmentForm.nombre.trim(),
      capacidad: Number(apartmentForm.capacidad),
      dormitorios: Array.from({ length: bedrooms }, (_, index) => ({
        descripcion: `Dormitorio ${index + 1}`,
        tipoCama: "Cama doble",
      })),
      estadoOperativoInicial: "PREPARADO",
      permiteMascotas: apartmentForm.permiteMascotas,
    }, "Apartamento creado. Su ID UUID fue generado por el backend.");
    if (created) setApartmentForm({ nombre: "", capacidad: 2, dormitorios: 1, permiteMascotas: false });
  };

  const createSeason = async (event) => {
    event.preventDefault();
    const created = await submit("/alojamiento/temporadas", seasonForm, "Temporada registrada.");
    if (created) setSeasonForm({ nombre: "", fechaInicio: today, fechaFin: today });
  };

  const createTariff = async (event) => {
    event.preventDefault();
    const { apartamentoId, temporadaId, valorPorNoche, vigenteDesde } = tariffForm;
    if (!apartamentoId || !temporadaId) {
      setError("Selecciona un apartamento y una temporada.");
      return;
    }
    await submit(`/apartamentos/${encodeURIComponent(apartamentoId)}/tarifas`, {
      temporadaId,
      valorPorNoche: Number(valorPorNoche),
      vigenteDesde,
    }, "Tarifa registrada.");
  };

  const createPolicy = async (event) => {
    event.preventDefault();
    await submit("/alojamiento/politica-cancelacion", {
      vigenteDesde: policyDate,
      tramos: defaultPolicy,
    }, "Nueva política de cancelación registrada.");
  };

  const updateReservation = async (reservation, action) => {
    if (action === "cancelar") {
      const reason = window.prompt("Motivo de cancelación:", "Cancelada por administración");
      if (!reason) return;
      await submit(`/reservas/${encodeURIComponent(reservation.codigo)}/cancelar`, { motivo: reason }, "Reserva cancelada.", "PUT");
      return;
    }
    await submit(`/reservas/${encodeURIComponent(reservation.codigo)}/${action}`, undefined, "Reserva actualizada.", "PUT");
  };

  const openFolio = async (reservation) => {
    setFolioLoading(true);
    setError("");
    try {
      const response = await api(`/reservas/${encodeURIComponent(reservation.codigo)}/folio`);
      if (!response.ok) throw new Error("No se pudo consultar el folio de la reserva.");
      setFolio(await response.json());
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setFolioLoading(false);
    }
  };

  const registerPayment = async (event) => {
    event.preventDefault();
    if (!folio) return;
    const saved = await submit(
      `/reservas/${encodeURIComponent(folio.codigoReserva)}/folio/pagos`,
      { ...paymentForm, valor: Number(paymentForm.valor) },
      "Pago registrado.",
    );
    if (saved) {
      const response = await api(`/reservas/${encodeURIComponent(folio.codigoReserva)}/folio`);
      if (response.ok) setFolio(await response.json());
      setPaymentForm({ valor: "", medioPago: "TRANSFERENCIA", referencia: "" });
    }
  };

  if (loading) return <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box>;

  return (
    <Box sx={{ maxWidth: 1200, mx: "auto", p: { xs: 2, md: 4 } }}>
      <Typography variant="h4" component="h1" gutterBottom>Administración</Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>
        Gestiona los apartamentos, reservas y reglas que guarda el backend.
      </Typography>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {message && <Alert severity="success" sx={{ mb: 2 }}>{message}</Alert>}

      <Tabs value={tab} onChange={(_, value) => setTab(value)} sx={{ mb: 3 }} variant="scrollable">
        <Tab label="Apartamentos" />
        <Tab label="Reservas" />
        <Tab label="Alojamiento y tarifas" />
      </Tabs>

      {tab === 0 && (
        <Grid container spacing={3}>
          <Grid item xs={12} md={7}>
            <Card><CardContent>
              <Typography variant="h6" gutterBottom>Apartamentos ({apartments.length})</Typography>
              <Table size="small">
                <TableHead><TableRow><TableCell>Nombre / ID UUID</TableCell><TableCell>Capacidad</TableCell><TableCell>Estado</TableCell></TableRow></TableHead>
                <TableBody>{apartments.map((apartment) => (
                  <TableRow key={apartment.id}>
                    <TableCell>{apartment.nombre}<Typography variant="caption" display="block" color="text.secondary">{apartment.id}</Typography></TableCell>
                    <TableCell>{apartment.capacidad}</TableCell>
                    <TableCell>{apartment.estadoOperativo}</TableCell>
                  </TableRow>
                ))}</TableBody>
              </Table>
            </CardContent></Card>
          </Grid>
          <Grid item xs={12} md={5}>
            <Card><CardContent>
              <Typography variant="h6" gutterBottom>Registrar apartamento</Typography>
              <Box component="form" onSubmit={createApartment} sx={{ display: "grid", gap: 2 }}>
                <TextField required label="Nombre" value={apartmentForm.nombre} onChange={(event) => setApartmentForm({ ...apartmentForm, nombre: event.target.value })} />
                <TextField required type="number" label="Capacidad" value={apartmentForm.capacidad} onChange={(event) => setApartmentForm({ ...apartmentForm, capacidad: event.target.value })} slotProps={{ htmlInput: { min: 1 } }} />
                <TextField required type="number" label="Dormitorios" value={apartmentForm.dormitorios} onChange={(event) => setApartmentForm({ ...apartmentForm, dormitorios: event.target.value })} slotProps={{ htmlInput: { min: 1 } }} />
                <FormControlLabel control={<Checkbox checked={apartmentForm.permiteMascotas} onChange={(event) => setApartmentForm({ ...apartmentForm, permiteMascotas: event.target.checked })} />} label="Admite mascotas" />
                <Button type="submit" variant="contained" disabled={busy}>Crear apartamento</Button>
              </Box>
            </CardContent></Card>
          </Grid>
        </Grid>
      )}

      {tab === 1 && (
        <Card><CardContent>
          <Typography variant="h6" gutterBottom>Reservas ({reservations.length})</Typography>
          {reservations.length === 0 ? <Typography color="text.secondary">No hay reservas registradas.</Typography> : (
            <Table size="small">
              <TableHead><TableRow><TableCell>Código</TableCell><TableCell>Estado</TableCell><TableCell>Titular</TableCell><TableCell>Estadía</TableCell><TableCell>Acciones</TableCell></TableRow></TableHead>
              <TableBody>{reservations.map((reservation) => (
                <TableRow key={reservation.codigo}>
                  <TableCell>{reservation.codigo}</TableCell><TableCell>{reservation.estado}</TableCell>
                  <TableCell>{reservation.nombreTitular}</TableCell>
                  <TableCell>{reservation.fechaEntrada} – {reservation.fechaSalida}</TableCell>
                  <TableCell>
                    <Box sx={{ display: "flex", gap: 1, flexWrap: "wrap" }}>
                      <Button size="small" disabled={folioLoading} onClick={() => openFolio(reservation)}>Folio y pagos</Button>
                      {reservation.estado === "PENDIENTE" && <Button size="small" disabled={busy} onClick={() => updateReservation(reservation, "confirmar")}>Confirmar</Button>}
                      {!["CANCELADA", "FINALIZADA", "NO_SHOW"].includes(reservation.estado) && <Button size="small" color="error" disabled={busy} onClick={() => updateReservation(reservation, "cancelar")}>Cancelar</Button>}
                      {reservation.estado === "CONFIRMADA" && <Button size="small" disabled={busy} onClick={() => updateReservation(reservation, "registrar-llegada")}>Registrar llegada</Button>}
                      {reservation.estado === "EN_CURSO" && <Button size="small" disabled={busy} onClick={() => updateReservation(reservation, "registrar-salida")}>Registrar salida</Button>}
                    </Box>
                  </TableCell>
                </TableRow>
              ))}</TableBody>
            </Table>
          )}
        </CardContent></Card>
      )}

      {tab === 2 && (
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Card><CardContent>
              <Typography variant="h6" gutterBottom>{accommodation?.nombre}</Typography>
              <Typography color="text.secondary" gutterBottom>{accommodation?.ciudad}</Typography>
              {accommodation?.parametros && <Typography variant="body2">
                Entrada {accommodation.parametros.horaEntrada} · salida {accommodation.parametros.horaSalida} · preparación {accommodation.parametros.tiempoPreparacionHoras} h · anticipo {accommodation.parametros.exigeAnticipo ? `${accommodation.parametros.porcentajeAnticipo * 100}%` : "no requerido"}
              </Typography>}
              <Typography variant="subtitle1" sx={{ mt: 2 }}>Temporadas</Typography>
              {accommodation?.temporadas?.map((season) => <Typography key={season.id} variant="body2">{season.nombre}: {season.fechaInicio ?? "sin inicio"} – {season.fechaFin ?? "sin fin"} · ID {season.id}</Typography>)}
              <Typography variant="subtitle1" sx={{ mt: 2 }}>Política de cancelación vigente</Typography>
              {accommodation?.politicaVigente?.tramos?.map((segment) => <Typography key={segment.antelacionMinimaHoras} variant="body2">Desde {segment.antelacionMinimaHoras} h: retención {segment.porcentajeRetencion * 100}%</Typography>)}
              <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 2 }}>Versiones de política guardadas: {policies.length}</Typography>
            </CardContent></Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card><CardContent><Typography variant="h6" gutterBottom>Nueva temporada</Typography>
              <Box component="form" onSubmit={createSeason} sx={{ display: "grid", gap: 2 }}>
                <TextField required label="Nombre" value={seasonForm.nombre} onChange={(event) => setSeasonForm({ ...seasonForm, nombre: event.target.value })} />
                <TextField required type="date" label="Desde" value={seasonForm.fechaInicio} onChange={(event) => setSeasonForm({ ...seasonForm, fechaInicio: event.target.value })} slotProps={{ inputLabel: { shrink: true } }} />
                <TextField required type="date" label="Hasta" value={seasonForm.fechaFin} onChange={(event) => setSeasonForm({ ...seasonForm, fechaFin: event.target.value })} slotProps={{ inputLabel: { shrink: true } }} />
                <Button type="submit" variant="contained" disabled={busy}>Crear temporada</Button>
              </Box>
            </CardContent></Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card><CardContent><Typography variant="h6" gutterBottom>Registrar tarifa</Typography>
              <Box component="form" onSubmit={createTariff} sx={{ display: "grid", gap: 2 }}>
                <FormControl fullWidth><InputLabel>Apartamento</InputLabel><Select label="Apartamento" value={tariffForm.apartamentoId} onChange={(event) => setTariffForm({ ...tariffForm, apartamentoId: event.target.value })}>{apartments.map((apartment) => <MenuItem key={apartment.id} value={apartment.id}>{apartment.nombre}</MenuItem>)}</Select></FormControl>
                <FormControl fullWidth><InputLabel>Temporada</InputLabel><Select label="Temporada" value={tariffForm.temporadaId} onChange={(event) => setTariffForm({ ...tariffForm, temporadaId: event.target.value })}>{accommodation?.temporadas?.map((season) => <MenuItem key={season.id} value={season.id}>{season.nombre}</MenuItem>)}</Select></FormControl>
                <TextField required type="number" label="Valor por noche" value={tariffForm.valorPorNoche} onChange={(event) => setTariffForm({ ...tariffForm, valorPorNoche: event.target.value })} slotProps={{ htmlInput: { min: 1 } }} />
                <TextField required type="date" label="Vigente desde" value={tariffForm.vigenteDesde} onChange={(event) => setTariffForm({ ...tariffForm, vigenteDesde: event.target.value })} slotProps={{ inputLabel: { shrink: true } }} />
                <Button type="submit" variant="contained" disabled={busy}>Guardar tarifa</Button>
              </Box>
            </CardContent></Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card><CardContent><Typography variant="h6" gutterBottom>Publicar política</Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>Usa los tres tramos estándar del backend: 0 h (100%), 72 h (50%) y 168 h (0%).</Typography>
              <Box component="form" onSubmit={createPolicy} sx={{ display: "grid", gap: 2 }}>
                <TextField required type="date" label="Vigente desde" value={policyDate} onChange={(event) => setPolicyDate(event.target.value)} slotProps={{ inputLabel: { shrink: true } }} />
                <Button type="submit" variant="contained" disabled={busy}>Publicar política</Button>
              </Box>
            </CardContent></Card>
          </Grid>
        </Grid>
      )}

      <Dialog open={Boolean(folio)} onClose={() => setFolio(null)} fullWidth maxWidth="md">
        <DialogTitle>Folio de reserva {folio?.codigoReserva}</DialogTitle>
        <DialogContent dividers>
          {folio && <>
            <Typography>Saldo pendiente: {folio.moneda} {folio.saldoPendiente}</Typography>
            <Typography variant="subtitle1" sx={{ mt: 2 }}>Cargos</Typography>
            {(folio.cargos ?? []).map((charge) => <Typography key={charge.id} variant="body2">{charge.tipo}: {folio.moneda} {charge.valor}</Typography>)}
            <Typography variant="subtitle1" sx={{ mt: 2 }}>Pagos registrados</Typography>
            {(folio.pagos ?? []).map((payment) => <Typography key={payment.id} variant="body2">{payment.fecha} · {payment.medio}: {folio.moneda} {payment.valor}</Typography>)}
            <Box component="form" onSubmit={registerPayment} sx={{ display: "flex", gap: 2, flexWrap: "wrap", mt: 3 }}>
              <TextField required type="number" label="Valor" value={paymentForm.valor} onChange={(event) => setPaymentForm({ ...paymentForm, valor: event.target.value })} slotProps={{ htmlInput: { min: 1 } }} />
              <FormControl sx={{ minWidth: 180 }}><InputLabel>Medio de pago</InputLabel><Select label="Medio de pago" value={paymentForm.medioPago} onChange={(event) => setPaymentForm({ ...paymentForm, medioPago: event.target.value })}><MenuItem value="TRANSFERENCIA">Transferencia</MenuItem><MenuItem value="TARJETA">Tarjeta</MenuItem><MenuItem value="EFECTIVO">Efectivo</MenuItem></Select></FormControl>
              <TextField label="Referencia" value={paymentForm.referencia} onChange={(event) => setPaymentForm({ ...paymentForm, referencia: event.target.value })} />
              <Button type="submit" variant="contained" disabled={busy}>Registrar pago</Button>
            </Box>
          </>}
        </DialogContent>
        <DialogActions><Button onClick={() => setFolio(null)}>Cerrar</Button></DialogActions>
      </Dialog>
    </Box>
  );
};

export default AdminDashboard;
