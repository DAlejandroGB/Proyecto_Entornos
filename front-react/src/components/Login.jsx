import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const API_URL = 'http://localhost:8080';

export default function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nombreUsuario: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value.trim()
    }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    if (!formData.nombreUsuario || !formData.password) {
      setError('Por favor complete todos los campos');
      setIsLoading(false);
      return;
    }

    try {
      const formBody = `usuarioNombre=${encodeURIComponent(formData.nombreUsuario)}&contrasena=${encodeURIComponent(formData.password)}`;

      const response = await fetch(`${API_URL}/credencial/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Accept': 'application/json'
        },
        body: formBody
      });

      if (!response.ok) {
        console.error('Error de respuesta:', response.status);
        throw new Error('Credenciales no válidas');
      }

      const data = await response.json();
      console.log('Respuesta del servidor:', data);
      
      if (data && data.token) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('userData', JSON.stringify({
          idUsuario: data.idUsuario,
          nombreUsuario: data.nombreUsuario,
          rolUsuario: data.rolUsuario
        }));
        navigate('/home');
      } else {
        throw new Error('Respuesta inválida del servidor');
      }
    } catch (error) {
      console.error('Error durante el login:', error);
      setError(error.message || 'Credenciales no válidas');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-left">
        <h3 className="login-title">
          <span>¿</span>
          TeFaltanPastillas
          <span>?</span>
        </h3>
        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="nombreUsuario">Nombre de usuario *</label>
            <input
              type="text"
              id="nombreUsuario"
              name="nombreUsuario"
              placeholder="username"
              value={formData.nombreUsuario}
              onChange={handleChange}
              required
              disabled={isLoading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Contraseña *</label>
            <input
              type="password"
              id="password"
              name="password"
              placeholder="********"
              value={formData.password}
              onChange={handleChange}
              required
              disabled={isLoading}
            />
          </div>
          {error && <div className="error-message">{error}</div>}
          <div className="form-buttons">
            <button 
              type="submit" 
              className="btn-login"
              disabled={isLoading}
            >
              {isLoading ? 'Iniciando sesión...' : 'INICIAR SESIÓN'}
            </button>
            <button 
              type="button" 
              className="btn-signup" 
              onClick={() => navigate('/register')}
              disabled={isLoading}
            >
              REGISTRATE
            </button>
          </div>
        </form>
      </div>
      <div className="login-right">
        <img src="/images/fondo_login.jpg" alt="Medicinas" className="medicine-icon" />
      </div>
    </div>
  );
} 