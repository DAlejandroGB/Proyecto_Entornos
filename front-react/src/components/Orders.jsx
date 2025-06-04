import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import './Orders.css'
import { useNavigate } from 'react-router-dom';

const API_URL = 'http://localhost:8080';
const Orders = () => {
    const navigate = useNavigate();
    const [usuario, setUsuario] = useState(null);
    const [error, setError] = useState(null);
    const [direccion, setDireccion] = useState('');
    const [ordenes, setOrdenes] = useState([]);
    const [estadoFiltro, setEstadoFiltro] = useState('TODOS');
    const [paginaActual, setPaginaActual] = useState(0);
    const [totalPaginas, setTotalPaginas] = useState(0);
    const [detalleMedicamentos, setDetalleMedicamentos] = useState([]);
    const [imagenSeleccionada, setImagenSeleccionada] = useState(null);
    const [mostrarModal, setMostrarModal] = useState(false);
    const [ordenSeleccionada, setOrdenSeleccionada] = useState(null);
    const [botonesDeshabilitados, setBotonesDeshabilitados] = useState(false);


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

    const fetchOrdenes = async (estado, pagina = 0) => {
        setError(null);
        const token = localStorage.getItem('token');
        const pageSize = 8;

        if (estado === 'TODOS') {
            let procesando = [];
            let terminado = [];

            try {
                const res1 = await axios.get(`${API_URL}/api/orden/PROCESANDO`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                procesando = res1.data.content || [];
                console.log(procesando)
            } catch (err) {
                console.warn("Error al obtener órdenes PROCESANDO:", err.response?.status || err.message);
            }

            try {
                const res2 = await axios.get(`${API_URL}/api/orden/COMPLETADA`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                terminado = res2.data.content || [];
            } catch (err) {
                console.warn("Error al obtener órdenes COMPLETADA:", err.response?.status || err.message);
            }

            const todas = [...procesando, ...terminado];
            todas.sort((a, b) => new Date(b.fechaCreacion) - new Date(a.fechaCreacion));

            const total = todas.length;
            const desde = pagina * pageSize;
            const hasta = desde + pageSize;
            const paginaContenido = todas.slice(desde, hasta);

            setOrdenes(paginaContenido);
            setPaginaActual(pagina);
            setTotalPaginas(Math.max(1, Math.ceil(total / pageSize)));

            if (procesando.length === 0 && terminado.length === 0) {
                setError("No se pudieron obtener las órdenes de ningún estado.");
            }

        } else {
            try {
                const res = await axios.get(`${API_URL}/api/orden/${estado}?page=${pagina}&size=${pageSize}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });

                const data = res.data;
                setOrdenes(data.content);
                setPaginaActual(data.number);
                setTotalPaginas(data.totalPages);
            } catch (err) {
                if (err.response?.status === 404) {
                    setOrdenes([]);
                    setPaginaActual(0);
                    setTotalPaginas(1);
                } else {
                    console.error("Error al obtener órdenes:", err);
                    setError("No se pudieron cargar las órdenes.");
                }
            }
        }
    };


    useEffect(() => {
        fetchOrdenes(estadoFiltro, 0);
    }, [estadoFiltro]);

    const fetchDetalleOrden = async (idOrden) => {
        const token = localStorage.getItem('token');
        try {
            const res = await axios.get(`${API_URL}/api/orden/idorden/${idOrden}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setDetalleMedicamentos(res.data.medicamentos);
            setOrdenSeleccionada(res.data);
            setBotonesDeshabilitados(false);
            console.log(res.data.medicamentos)
        } catch (error) {
            console.error('Error al obtener detalles de la orden:', error);
        }
    };


    const extraerBase64 = (cadena) => {
        if (!cadena) return '';
        const partes = cadena.split(',');
        return partes.length === 2 ? partes[1] : cadena; // Si viene como "data:image/jpeg;base64,..."
    };

    const handleVerOrden = (imagenBase64) => {
        setImagenSeleccionada(extraerBase64(imagenBase64));
        setMostrarModal(true);
    };

    const handleActualizarEstado = async (nuevoEstado) => {
        if (!ordenSeleccionada) return;

        setBotonesDeshabilitados(true);
        const token = localStorage.getItem('token');
        try {
            const res = await axios.put(`${API_URL}/api/orden`, null, {
                params: {
                    idOrden: ordenSeleccionada.idOrden,
                    estado: nuevoEstado
                },
                headers: { Authorization: `Bearer ${token}` }
            });
            console.log(res.data)
            // Actualizamos el listado localmente
            if (nuevoEstado === 'CANCELADA') {
                // Eliminamos la orden del listado
                setOrdenes(prev => prev.filter(o => o.idOrden !== ordenSeleccionada.idOrden));
                setDetalleMedicamentos([]);
                setOrdenSeleccionada(null);
            } else if (nuevoEstado === 'COMPLETADA') {
                // Actualizamos estado en la lista
                setOrdenes(prev => prev.map(o =>
                    o.idOrden === ordenSeleccionada.idOrden ? { ...o, estado: 'COMPLETADA' } : o
                ));
                // Actualizamos la orden seleccionada con el nuevo estado
                setOrdenSeleccionada(prev => prev ? { ...prev, estado: 'COMPLETADA' } : prev);
            }
        } catch (error) {
            console.error('Error actualizando estado:', error);
            alert('Error al actualizar estado, intenta de nuevo.');
            setBotonesDeshabilitados(false);
        }
    };

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

            <main className="main-content2">
                <h3>Hola, {usuario?.nombres || 'Usuario'}</h3>
                <h2 className="section-title">Todas las ordenes recibidas</h2>
                <div className="filter-controls">
                    <button onClick={() => setEstadoFiltro('TODOS')}>Todos</button>
                    <button onClick={() => setEstadoFiltro('PROCESANDO')}>Procesando</button>
                    <button onClick={() => setEstadoFiltro('COMPLETADA')}>Terminado</button>
                </div>
                {error ? (
                    <p className="error">{error}</p>
                ) : (

                    <div className="orders-grid">
                        {ordenes.length === 0 ? (
                            <p>No hay órdenes para mostrar.</p>
                        ) : (
                            <table className="orders-table">
                                <thead>
                                    <tr>
                                        <th>ID Orden</th>
                                        <th>Usuario</th>
                                        <th>Fecha de Creacion</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {ordenes.map(orden => (
                                        <tr key={orden.idOrden}>
                                            <td>{orden.idOrden}</td>
                                            <td>{orden.nombreUsuario}</td>
                                            <td>{orden.fechaCreacion}</td>
                                            <td>{orden.estado}</td>
                                            <td>
                                                <button className="but" onClick={() => fetchDetalleOrden(orden.idOrden)}>
                                                    Ver detalles
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>

                )}
                <div className="pagination">
                    <button disabled={paginaActual === 0} onClick={() => fetchOrdenes(estadoFiltro, paginaActual - 1)}>
                        Anterior
                    </button>
                    <span>Página {paginaActual + 1} de {totalPaginas}</span>
                    <button disabled={paginaActual + 1 >= totalPaginas} onClick={() => fetchOrdenes(estadoFiltro, paginaActual + 1)}>
                        Siguiente
                    </button>
                </div>
            </main>
            <aside className="order-sidebar2">
                <div className="address-box">
                    <p className="section-title">Tu dirección</p>
                    <p>{direccion || 'No has configurado tu dirección'}</p>
                    <button className='small-btn' onClick={() => navigate('/Perfil')}>Cambiar</button>
                </div>
                {/* crear el className detail-box */}
                <div>
                    <h3 className="medicamentos-titulo">Medicamentos</h3>
                    <div className="tabla-scrollable">
                        <table className="tabla-medicamentos">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Precio</th>
                                    <th>Cantidad</th>
                                    <th>Orden médica</th>
                                </tr>
                            </thead>
                            <tbody>
                                {detalleMedicamentos.map((med, index) => (
                                    <tr key={index}>
                                        <td>{med.nombreMedicamento}</td>
                                        <td>${med.precioMedicamento.toLocaleString()}</td>
                                        <td>{med.cantidad}</td>
                                        <td>
                                            {med.ordenMedicamento && (
                                                <button
                                                    className="btn-ver-orden"
                                                    onClick={() => handleVerOrden(med.ordenMedicamento)}
                                                >
                                                    Ver orden
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                {ordenSeleccionada && (
                    <div className="action-buttons">
                        <button
                            disabled={botonesDeshabilitados || ordenSeleccionada.estado === 'COMPLETADA'}
                            onClick={() => handleActualizarEstado('COMPLETADA')}
                        >
                            Aceptar
                        </button>
                        <button
                            disabled={botonesDeshabilitados || ordenSeleccionada.estado === 'COMPLETADA'}
                            onClick={() => handleActualizarEstado('CANCELADA')}
                        >
                            Rechazar
                        </button>
                    </div>
                )}
            </aside>
            {mostrarModal && (
                <div className="modal-overlay" onClick={() => setMostrarModal(false)}>
                    <div className="modal-contenido" onClick={(e) => e.stopPropagation()}>
                        <span className="modal-cerrar" onClick={() => setMostrarModal(false)}>×</span>
                        <img
                            src={`data:image/jpeg;base64,${imagenSeleccionada}`}
                            alt="Orden médica"
                            className="imagen-modal"
                        />
                    </div>
                </div>
            )}
        </div>
    )
};
export default Orders;