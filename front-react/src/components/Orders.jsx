import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import { useNavigate } from 'react-router-dom';

const API_URL = 'http://localhost:8080';
const Orders = () => {
    const navigate = useNavigate();
    const [usuario, setUsuario] = useState(null);
    const [error, setError] = useState(null);
    const [direccion, setDireccion] = useState('');

    useEffect(() => {
        const userData = JSON.parse(localStorage.getItem('userData'));
        const idUsuario = userData?.idUsuario;
        const token = localStorage.getItem('token');

        if (!idUsuario) {
            console.error('ID de usuario no encontrado');
            return;
        }

        axios.get(`${API_URL}/usuarios/list/${idUsuario}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then(res => {
                localStorage.setItem('usuarioCompleto', JSON.stringify(res.data));
                setDireccion(res.data.direccion);
                setUsuario(res.data);  // <-- Agrega esta línea para actualizar el estado usuario
                console.log('Usuario completo:', res.data);
            })
            .catch(err => {
                console.error('Error al obtener datos del usuario:', err);
            });
    }, []);

    return (
        <div className="home-container">
            <aside className="sidebar">
                <h1 className="logo">Panel de <span className="highlight">Manager</span></h1>
                <nav>
                    <ul>
                        <li onClick={() => navigate('/management')}>Inicio</li>
                        <li onClick={() => navigate('/perfil')}>Usuario</li>
                        <li className="active">Ordenes</li>
                    </ul>
                </nav>
                <div
                    className="logout-box"
                    style={{
                        position: 'absolute',
                        bottom: '1rem',
                        left: '10%',
                        width: '80%',
                        textAlign: 'center'
                    }}
                >
                    <button onClick={() => {
                        localStorage.clear();
                        navigate('/');
                    }}>
                        Cerrar sesión
                    </button>
                </div>
            </aside>

            <main className="main-content">
                <h3>Hola, {usuario?.nombres || 'Usuario'}</h3>
                <h2 className="section-title">Todas las ordenes recibidas</h2>
                {error ? (
                    <p className="error">{error}</p>
                ) : (
                    //crear el className orders-grid
                    <div className="orders-grid">



                    </div>
                )}
            </main>
            <aside className="order-sidebar">
                <div className="address-box">
                    <p className="section-title">Tu dirección</p>
                    <p>{direccion || 'No has configurado tu dirección'}</p>
                    <button className='small-btn' onClick={() => navigate('/Perfil')}>Cambiar</button>
                </div>

                <div className="cart-box">
                    
                </div>
            </aside>
        </div>
    )
};
export default Orders;