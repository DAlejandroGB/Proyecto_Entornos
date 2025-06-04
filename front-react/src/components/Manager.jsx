import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import './Manager.css';
import { useNavigate } from 'react-router-dom';

const API_URL = 'http://localhost:8080';

const Manager = () => {
    const [usuario, setUsuario] = useState(null);
    const [medicamentos, setMedicamentos] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();


    // Simulando obtención de datos para Manager
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

    useEffect(() => {
        const token = localStorage.getItem('token');

        fetch(`${API_URL}/api/medicamentos`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then((res) => {
                if (!res.ok) throw new Error('Error en la respuesta del servidor');
                return res.json();
            })
            .then((data) => {
                setMedicamentos(data);
            })
            .catch((err) => {
                console.error('Error al obtener medicamentos:', err);
                setError('Hubo un error al cargar los medicamentos.');
            });
    }, []);

    return (
        <div className="home-container">
            <aside className="sidebar">
                <h1 className="logo">Panel de <span className="highlight">Manager</span></h1>
                <nav>
                    <ul>
                        <li className="active">Inicio</li>
                        <li onClick={() => navigate('/perfil')}>Usuario</li>
                        <li onClick={() => navigate('/ordenes')}>Ordenes</li>
                    </ul>
                </nav>
                <div
                    className="logout-box"
                    style={{
                        position: 'absolute',
                        bottom: '1rem',
                        left: 0,
                        width: '100%',
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
                <h2 className="section-title">Todos los Medicamentos</h2>
                {error ? (
                    <p className="error">{error}</p>
                ) : (
                    <div className="product-grid">
                        {medicamentos.map((med) => {
                            return (
                                <div className="product-card" key={med.id}>

                                    <img
                                        src={med.imagenMed}
                                    />
                                    <p className="product-name">{med.nombre}</p>
                                    <p className="product-price">${med.precio.toFixed(2)}</p>

                                    <button className="btn-edit" onClick={() => editMedicamento(med)}>
                                        ✏️
                                    </button>
                                    <button className="btn-delete" onClick={() => deleteMedicamento(med)}>
                                        🗑️
                                    </button>

                                </div>
                            );
                        })}
                    </div>
                )}

            </main>

        </div>
    );
};

export default Manager;
