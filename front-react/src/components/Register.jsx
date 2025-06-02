import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import {
  Container,
  Box,
  TextField,
  Button,
  Typography,
  Grid,
  MenuItem,
  Stepper,
  Step,
  StepLabel,
} from '@mui/material';
import './Register.css';

const API_URL = 'http://localhost:8080';

export default function Register() {
  const navigate = useNavigate();
  const [activeStep, setActiveStep] = useState(0);
  const [error, setError] = useState('');
  const [userData, setUserData] = useState({
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    direccion: '',
    rol: '',
  });
  const [credentialData, setCredentialData] = useState({
    usuarioNombre: '',
    contrasena: '',
  });

  const handleUserDataChange = (e) => {
    const { name, value } = e.target;
    setUserData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCredentialChange = (e) => {
    const { name, value } = e.target;
    setCredentialData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleUserSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await axios.post(`${API_URL}/usuarios/`, userData);
      
      if (response.data) {
        localStorage.setItem('usuario', JSON.stringify(response.data));
        setActiveStep(1);
      }
    } catch (error) {
      setError('Error al registrar usuario');
      console.error('Error registrando usuario:', error);
    }
  };

  const handleCredentialSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const usuarioGuardado = localStorage.getItem('usuario');
    if (!usuarioGuardado) {
      setError('Error: No se encontró el usuario registrado.');
      return;
    }

    const usuario = JSON.parse(usuarioGuardado);

    try {
      const response = await axios.post(`${API_URL}/credencial/`, {
        usuario: { id: usuario.id },
        usuarioNombre: credentialData.usuarioNombre,
        contrasena: credentialData.contrasena
      });

      if (response.data) {
        localStorage.removeItem('usuario');
        navigate('/');
      }
    } catch (error) {
      setError('Error al registrar credenciales');
      console.error('Error registrando credenciales:', error);
    }
  };

  const handleCancel = async () => {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const usuario = JSON.parse(usuarioGuardado);
      try {
        await axios.delete(`${API_URL}/usuarios/${usuario.id}`);
        localStorage.removeItem('usuario');
      } catch (error) {
        console.error('Error eliminando usuario:', error);
      }
    }
    navigate('/');
  };

  const steps = ['Datos de Usuario', 'Credenciales'];

  return (
    <Container component="main" maxWidth="lg">
      <Box className="register-container">
        <Typography component="h1" variant="h4" className="register-title">
          <span style={{ color: 'var(--primary)' }}>¿</span>
          TeFaltanPastillas
          <span style={{ color: 'var(--primary)' }}>?</span>
        </Typography>
        
        <Box className="stepper-container">
          <Stepper activeStep={activeStep}>
            {steps.map((label) => (
              <Step key={label}>
                <StepLabel>{label}</StepLabel>
              </Step>
            ))}
          </Stepper>
        </Box>

        <Box className="form-container">
          {activeStep === 0 ? (
            <Box component="form" onSubmit={handleUserSubmit}>
              <Grid container spacing={1.5}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    required
                    fullWidth
                    name="nombre"
                    label="Nombre"
                    value={userData.nombre}
                    onChange={handleUserDataChange}
                    className="form-field"
                    size="small"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    required
                    fullWidth
                    name="apellido"
                    label="Apellido"
                    value={userData.apellido}
                    onChange={handleUserDataChange}
                    className="form-field"
                    size="small"
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    name="email"
                    label="Email"
                    type="email"
                    value={userData.email}
                    onChange={handleUserDataChange}
                    className="form-field"
                    size="small"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    name="telefono"
                    label="Teléfono"
                    value={userData.telefono}
                    onChange={handleUserDataChange}
                    className="form-field"
                    size="small"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    required
                    fullWidth
                    select
                    name="rol"
                    label="Rol"
                    value={userData.rol}
                    onChange={handleUserDataChange}
                    className="form-field"
                    size="small"
                    SelectProps={{
                      MenuProps: {
                        PaperProps: {
                          style: {
                            maxHeight: 200
                          }
                        }
                      }
                    }}
                  >
                    <MenuItem value="">Seleccionar rol</MenuItem>
                    <MenuItem value="gerente">Gerente</MenuItem>
                    <MenuItem value="cliente">Cliente</MenuItem>
                  </TextField>
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    name="direccion"
                    label="Dirección"
                    value={userData.direccion}
                    onChange={handleUserDataChange}
                    className="form-field"
                    size="small"
                    multiline
                    rows={2}
                  />
                </Grid>
              </Grid>
              {error && (
                <Typography className="error-message">
                  {error}
                </Typography>
              )}
              <Box className="form-buttons">
                <Button
                  type="submit"
                  fullWidth
                  className="btn-submit"
                >
                  Siguiente
                </Button>
                <Button
                  fullWidth
                  onClick={handleCancel}
                  className="btn-cancel"
                >
                  Cancelar
                </Button>
              </Box>
            </Box>
          ) : (
            <Box component="form" onSubmit={handleCredentialSubmit}>
              <Grid container spacing={1.5}>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    name="usuarioNombre"
                    label="Nombre de Usuario"
                    value={credentialData.usuarioNombre}
                    onChange={handleCredentialChange}
                    className="form-field"
                    size="small"
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    name="contrasena"
                    label="Contraseña"
                    type="password"
                    value={credentialData.contrasena}
                    onChange={handleCredentialChange}
                    className="form-field"
                    size="small"
                  />
                </Grid>
              </Grid>
              {error && (
                <Typography className="error-message">
                  {error}
                </Typography>
              )}
              <Box className="form-buttons">
                <Button
                  type="submit"
                  fullWidth
                  className="btn-submit"
                >
                  Registrar
                </Button>
                <Button
                  fullWidth
                  onClick={handleCancel}
                  className="btn-cancel"
                >
                  Cancelar
                </Button>
              </Box>
            </Box>
          )}
        </Box>
      </Box>
    </Container>
  );
} 