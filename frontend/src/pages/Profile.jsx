import { useEffect, useState } from "react";
import {
  Alert,
  Avatar,
  Box,
  Card,
  CardContent,
  CircularProgress,
  Container,
  Grid,
  Typography,
} from "@mui/material";
import { api } from "../services/api.js";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    api("/auth/me")
      .then(async (response) => {
        if (!response.ok)
          throw new Error(
            "No se pudo cargar el perfil del usuario autenticado.",
          );
        return response.json();
      })
      .then((data) => {
        if (!cancelled) setUser(data);
      })
      .catch((requestError) => {
        if (!cancelled) setError(requestError.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Mi perfil
      </Typography>
      {loading && (
        <Box sx={{ display: "flex", justifyContent: "center", py: 6 }}>
          <CircularProgress />
        </Box>
      )}
      {error && <Alert severity="error">{error}</Alert>}
      {user && (
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  py: 4,
                }}
              >
                <Avatar sx={{ width: 96, height: 96, mb: 2 }}>
                  {user.name?.trim()?.[0]?.toUpperCase() ?? "U"}
                </Avatar>
                <Typography variant="h6" component="h2" textAlign="center">
                  {user.name}
                </Typography>
                <Typography color="text.secondary" textAlign="center">
                  {user.email}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      )}
    </Container>
  );
};

export default Profile;
