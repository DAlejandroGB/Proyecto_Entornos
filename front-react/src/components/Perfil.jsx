import React, { useEffect, useState } from 'react';
import './Home.css';
import './Perfil.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const API_URL = 'http://localhost:8080';

const Perfil = () => {
  const [usuario, setUsuario] = useState(null);
  const [editando, setEditando] = useState(false);
  const [nombres, setNombres] = useState('');
  const [apellidos, setApellidos] = useState('');
  const [direccion, setDireccion] = useState('');
  const [telefono, setTelefono] = useState('');
  const [email, setEmail] = useState('');
  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const userData = JSON.parse(localStorage.getItem('userData'));
    const idUsuario = userData?.idUsuario;
    const token = localStorage.getItem('token');

    if (!idUsuario) {
      console.error('ID de usuario no encontrado');
      return;
    }

    axios.get(`${API_URL}/usuarios/list/${idUsuario}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(res => {
        const usuario = res.data;
        setUsuario(usuario);
        setNombres(usuario.nombres || '');
        setApellidos(usuario.apellidos || '');
        setDireccion(usuario.direccion || '');
        setTelefono(usuario.telefono || '');
        setEmail(usuario.email || '');
      })
      .catch(err => {
        console.error('Error al obtener datos del usuario:', err);
      });
  }, []);

  const handleGuardar = async () => {
    try {
      const token = localStorage.getItem('token');

      await axios.put(`${API_URL}/usuarios`, {
        id: usuario.id,
        nombres,
        apellidos,
        direccion,
        telefono,
        email
      }, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        }
      });

      setMensaje('Perfil actualizado correctamente');
      setEditando(false);
      setUsuario({ ...usuario, nombres, apellidos, direccion, telefono, email });
    } catch (error) {
      console.error('Error actualizando perfil:', error);
      setMensaje('No se pudo actualizar el perfil');
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  const handleRegistroClick = () => {
    navigate('/Register');
  };

  return (
    <div className="home-container">
      <aside className="sidebar">
        <h1 className="logo">TeFaltan <span className="highlight">Pastillas?</span></h1>
        <nav>
          <ul>
            <li onClick={() => navigate('/Home')}>Inicio</li>
            <li className="active">Usuario</li>
            <li onClick={() => navigate('/historial')}>Historial</li>
          </ul>
        </nav>
        <div className="register-box">
          <p>¿Eres Gerente de una Farmacia?</p>
          <button onClick={handleRegistroClick}>Regístrate</button>
        </div>
        <div className="logout-box">
          <button onClick={handleLogout}>Cerrar sesión</button>
        </div>
      </aside>

      <main className="main-content">
        <h2 className="section-title">Perfil de Usuario</h2>
        {usuario ? (
          <div className="profile-form">
            <label>Nombres:</label>
            {editando ? (
              <input
                type="text"
                name="nombres"
                value={nombres}
                onChange={e => setNombres(e.target.value)}
              />
            ) : (
              <p>{usuario.nombres}</p>
            )}

            <label>Apellidos:</label>
            {editando ? (
              <input
                type="text"
                name="apellidos"
                value={apellidos}
                onChange={e => setApellidos(e.target.value)}
              />
            ) : (
              <p>{usuario.apellidos}</p>
            )}

            <label>Dirección:</label>
            {editando ? (
              <input
                type="text"
                name="direccion"
                value={direccion}
                onChange={e => setDireccion(e.target.value)}
              />
            ) : (
              <p>{usuario.direccion}</p>
            )}

            <label>Teléfono:</label>
            {editando ? (
              <input
                type="text"
                name="telefono"
                value={telefono}
                onChange={e => setTelefono(e.target.value)}
              />
            ) : (
              <p>{usuario.telefono}</p>
            )}

            <label>Email:</label>
            <p>{usuario.email}</p>

            {editando ? (
              <button className="save-btn" onClick={handleGuardar}>Guardar cambios</button>
            ) : (
              <button className="save-btn" onClick={() => setEditando(true)}>Editar</button>
            )}
            {mensaje && <p className="info-message">{mensaje}</p>}
          </div>
        ) : (
          <p>Cargando datos del usuario...</p>
        )}
      </main>
    </div>
  );
};

export default Perfil;
