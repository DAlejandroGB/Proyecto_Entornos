import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.css';

const API_URL = 'http://localhost:8080';

export default function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nombreUsuario: '',
    password: '',
  });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await axios.post(`${API_URL}/usuarios/login`, 
        new URLSearchParams({
          usuarioNombre: formData.nombreUsuario,
          contrasena: formData.password
        }),
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }
      );

      if (response.data) {
        localStorage.setItem('token', response.data);
        navigate('/home');
      }
    } catch (error) {
      setError('Credenciales inválidas');
      console.error('Error durante el login:', error);
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
            />
          </div>
          <div className="form-options">
            <div className="remember-me">
              <input
                type="checkbox"
                id="remember"
                checked
                disabled
              />
              <label htmlFor="remember">Recuerdame</label>
            </div>
            <a href="#" className="forgot-password">¿Olvidaste tu contraseña?</a>
          </div>
          {error && <div className="error-message">{error}</div>}
          <div className="form-buttons">
            <button type="submit" className="btn-login">
              INICIAR SESIÓN
            </button>
            <button type="button" className="btn-signup" onClick={() => navigate('/register')}>
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