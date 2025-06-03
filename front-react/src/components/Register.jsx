import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Register.css';

const API_URL = 'http://localhost:8080';

// Configuración global de axios
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.post['Accept'] = 'application/json';

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
      [name]: value.trim()
    }));
    setError('');
  };

  const handleCredentialChange = (e) => {
    const { name, value } = e.target;
    setCredentialData(prev => ({
      ...prev,
      [name]: value.trim()
    }));
    setError('');
  };

  const handleUserSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!userData.nombre || !userData.apellido || !userData.email || !userData.rol) {
      setError('Por favor complete todos los campos requeridos');
      return;
    }

    try {
      // Convertir el rol de string a number
      const idRol = userData.rol === 'GERENTE' ? 1 : 2;

      const userDataToSend = {
        nombres: userData.nombre,
        apellidos: userData.apellido,
        email: userData.email,
        telefono: userData.telefono || '',
        direccion: userData.direccion || '',
        idRol: idRol
      };

      console.log('Enviando datos de usuario:', userDataToSend);

      const response = await axios.post(`${API_URL}/usuarios`, userDataToSend);
      
      console.log('Respuesta del servidor (usuario):', response.data);

      if (response.data && response.data.id) {
        localStorage.setItem('usuario', JSON.stringify(response.data));
        setActiveStep(1);
      } else {
        throw new Error('No se recibió el ID del usuario del servidor');
      }
    } catch (error) {
      console.error('Error completo:', error);
      console.error('Error registrando usuario:', error.response?.data || error.message);
      setError(error.response?.data?.message || error.message || 'Error al registrar usuario');
    }
  };

  const handleCredentialSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!credentialData.usuarioNombre || !credentialData.contrasena) {
      setError('Por favor complete todos los campos');
      return;
    }

    const usuarioGuardado = localStorage.getItem('usuario');
    if (!usuarioGuardado) {
      setError('Error: No se encontró el usuario registrado.');
      return;
    }

    const usuario = JSON.parse(usuarioGuardado);

    try {
      const credentialDataToSend = {
        idUsuario: usuario.id,
        nombreUsuario: credentialData.usuarioNombre,
        contrasena: credentialData.contrasena
      };

      console.log('Enviando datos de credenciales:', credentialDataToSend);

      const response = await axios.post(`${API_URL}/credencial`, credentialDataToSend);

      console.log('Respuesta del servidor (credenciales):', response.data);

      if (response.data) {
        localStorage.removeItem('usuario');
        navigate('/');
      } else {
        throw new Error('No se recibió confirmación del servidor');
      }
    } catch (error) {
      console.error('Error completo:', error);
      console.error('Error registrando credenciales:', error.response?.data || error.message);
      setError(error.response?.data?.message || error.message || 'Error al registrar credenciales');
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

  return (
    <div className="register-container">
      <h3 className="register-title">
        <span>¿</span>
        TeFaltanPastillas
        <span>?</span>
      </h3>

      {activeStep === 0 ? (
        <form className="register-form" onSubmit={handleUserSubmit}>
          <div className="form-group">
            <label htmlFor="nombre">Nombre *</label>
            <input
              type="text"
              id="nombre"
              name="nombre"
              placeholder="Nombre"
              value={userData.nombre}
              onChange={handleUserDataChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="apellido">Apellido *</label>
            <input
              type="text"
              id="apellido"
              name="apellido"
              placeholder="Apellido"
              value={userData.apellido}
              onChange={handleUserDataChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="correo@ejemplo.com"
              value={userData.email}
              onChange={handleUserDataChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="telefono">Teléfono</label>
            <input
              type="tel"
              id="telefono"
              name="telefono"
              placeholder="Teléfono"
              value={userData.telefono}
              onChange={handleUserDataChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="direccion">Dirección</label>
            <input
              type="text"
              id="direccion"
              name="direccion"
              placeholder="Dirección"
              value={userData.direccion}
              onChange={handleUserDataChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="rol">Rol *</label>
            <select
              id="rol"
              name="rol"
              value={userData.rol}
              onChange={handleUserDataChange}
              required
            >
              <option value="">Seleccione un rol</option>
              <option value="GERENTE">Gerente</option>
              <option value="CLIENTE">Cliente</option>
            </select>
          </div>
          {error && <div className="error-message">{error}</div>}
          <div className="form-buttons">
            <button type="submit" className="btn-register">
              Siguiente
            </button>
            <button type="button" className="btn-back" onClick={handleCancel}>
              Cancelar
            </button>
          </div>
        </form>
      ) : (
        <form className="register-form" onSubmit={handleCredentialSubmit}>
          <div className="form-group">
            <label htmlFor="usuarioNombre">Nombre de usuario *</label>
            <input
              type="text"
              id="usuarioNombre"
              name="usuarioNombre"
              placeholder="username"
              value={credentialData.usuarioNombre}
              onChange={handleCredentialChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="contrasena">Contraseña *</label>
            <input
              type="password"
              id="contrasena"
              name="contrasena"
              placeholder="********"
              value={credentialData.contrasena}
              onChange={handleCredentialChange}
              required
            />
          </div>
          {error && <div className="error-message">{error}</div>}
          <div className="form-buttons">
            <button type="submit" className="btn-register">
              Registrarse
            </button>
            <button type="button" className="btn-back" onClick={() => setActiveStep(0)}>
              Atrás
            </button>
          </div>
        </form>
      )}
    </div>
  );
} 