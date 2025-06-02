import { useNavigate } from 'react-router-dom';
import {
  Container,
  Box,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  CardMedia,
} from '@mui/material';
import { MedicationOutlined, PersonOutline } from '@mui/icons-material';

export default function Home() {
  const navigate = useNavigate();

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 8 }}>
        <Typography variant="h3" component="h1" gutterBottom align="center">
          <span style={{ color: 'green' }}>¿</span>
          TeFaltanPastillas
          <span style={{ color: 'green' }}>?</span>
        </Typography>
        <Typography variant="h5" component="h2" gutterBottom align="center" color="text.secondary">
          Sistema de Gestión de Medicamentos
        </Typography>
      </Box>

      <Grid container spacing={4} justifyContent="center">
        <Grid item xs={12} md={6}>
          <Card
            sx={{
              height: '100%',
              display: 'flex',
              flexDirection: 'column',
              cursor: 'pointer',
              '&:hover': {
                transform: 'scale(1.02)',
                transition: 'transform 0.2s ease-in-out',
              },
            }}
            onClick={() => navigate('/medicamentos')}
          >
            <CardMedia
              component="div"
              sx={{
                pt: '56.25%',
                bgcolor: 'primary.light',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <MedicationOutlined
                sx={{
                  fontSize: 80,
                  color: 'white',
                  position: 'absolute',
                  top: '50%',
                  left: '50%',
                  transform: 'translate(-50%, -50%)',
                }}
              />
            </CardMedia>
            <CardContent sx={{ flexGrow: 1, textAlign: 'center' }}>
              <Typography gutterBottom variant="h5" component="h2">
                Gestión de Medicamentos
              </Typography>
              <Typography>
                Administra el inventario de medicamentos, controla stock y realiza seguimiento.
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card
            sx={{
              height: '100%',
              display: 'flex',
              flexDirection: 'column',
              cursor: 'pointer',
              '&:hover': {
                transform: 'scale(1.02)',
                transition: 'transform 0.2s ease-in-out',
              },
            }}
            onClick={() => navigate('/usuarios')}
          >
            <CardMedia
              component="div"
              sx={{
                pt: '56.25%',
                bgcolor: 'secondary.light',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <PersonOutline
                sx={{
                  fontSize: 80,
                  color: 'white',
                  position: 'absolute',
                  top: '50%',
                  left: '50%',
                  transform: 'translate(-50%, -50%)',
                }}
              />
            </CardMedia>
            <CardContent sx={{ flexGrow: 1, textAlign: 'center' }}>
              <Typography gutterBottom variant="h5" component="h2">
                Gestión de Usuarios
              </Typography>
              <Typography>
                Administra usuarios, roles y permisos del sistema.
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Box sx={{ mt: 4, textAlign: 'center' }}>
        <Button
          variant="outlined"
          color="error"
          onClick={() => {
            localStorage.removeItem('token');
            navigate('/');
          }}
        >
          Cerrar Sesión
        </Button>
      </Box>
    </Container>
  );
} 