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
    const [modalVisible, setModalVisible] = useState(false);
    const [medEdit, setMedEdit] = useState(null);
    const [nombre, setNombre] = useState('');
    const [precio, setPrecio] = useState('');
    const [imagenMed, setImagenMed] = useState('');
    const [ventaLibre, setVentaLibre] = useState(false);


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

    const deleteMedicamento = (med) => {
        const confirmado = window.confirm(`¿Estás seguro que quieres eliminar el medicamento "${med.nombre}"?`);
        if (!confirmado) return;

        const token = localStorage.getItem('token');

        axios.delete(`${API_URL}/api/medicamentos/${med.id}`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(() => {
                // Actualizar la lista después de eliminar
                setMedicamentos(prevMedicamentos => prevMedicamentos.filter(m => m.id !== med.id));
                alert('Medicamento eliminado correctamente.');
            })
            .catch(err => {
                console.error('Error al eliminar medicamento:', err);
                alert('Hubo un error al eliminar el medicamento.');
            });
    };

    const openModal = (med) => {
        setMedEdit(med); // med es null para agregar
        setModalVisible(true);
    };

    const closeModal = () => {
        setModalVisible(false);
        setMedEdit(null);
    };

    useEffect(() => {
        if (medEdit) {
            setNombre(medEdit.nombre || '');
            setPrecio(medEdit.precio || '');
            setImagenMed(medEdit.imagenMed || '');
            setVentaLibre(medEdit.ventaLibre || false);
        } else {
            setNombre('');
            setPrecio('');
            setImagenMed('');
            setVentaLibre(false);
        }
    }, [medEdit]);

    const saveMedicamento = () => {
        const token = localStorage.getItem('token');
        const data = {
            nombre,
            precio: parseFloat(precio),
            imagenMed,
            ventaLibre,
        };

        if (medEdit) {
            // PUT para actualizar
            axios.put(`${API_URL}/api/medicamentos/${medEdit.id}`, data, {
                headers: { Authorization: `Bearer ${token}` }
            })
                .then(res => {
                    // Actualizar localmente la lista
                    setMedicamentos(meds => meds.map(m => m.id === medEdit.id ? res.data : m));
                    closeModal();
                })
                .catch(err => {
                    alert('Error actualizando medicamento');
                    console.error(err);
                });
        } else {
            // POST para crear nuevo
            axios.post(`${API_URL}/api/medicamentos`, data, {
                headers: { Authorization: `Bearer ${token}` }
            })
                .then(res => {
                    setMedicamentos(meds => [...meds, res.data]);
                    closeModal();
                })
                .catch(err => {
                    alert('Error agregando medicamento');
                    console.error(err);
                });
        }
    };

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

                                    <button className="btn-edit" onClick={() => openModal(med)}>
                                        ✏️
                                    </button>
                                    <button className="btn-delete" onClick={() => deleteMedicamento(med)}>
                                        🗑️
                                    </button>

                                </div>
                            );
                        })}
                        <div className="product-card add-new" onClick={() => openModal(null)}>
                            +
                        </div>
                    </div>

                )}
                {/* Modal */}
                {modalVisible && (
                    <div className="modal-backdrop" onClick={closeModal}>
                        <div className="modal-content" onClick={e => e.stopPropagation()}>
                            {/* Agrego un botón de cerrar con la clase para estilos */}
                            <button className="modal-close-btn" onClick={closeModal} aria-label="Cerrar modal">&times;</button>

                            <h2 className="modal-header">{medEdit ? 'Editar Medicamento' : 'Agregar Medicamento'}</h2>

                            <div className="modal-body">
                                <label>
                                    Nombre:
                                    <input type="text" value={nombre} onChange={e => setNombre(e.target.value)} />
                                </label>
                                <label>
                                    Precio:
                                    <input type="number" value={precio} onChange={e => setPrecio(e.target.value)} />
                                </label>
                                <label>
                                    URL Imagen:
                                    <input type="text" value={imagenMed} onChange={e => setImagenMed(e.target.value)} />
                                </label>
                                <label>
                                    Venta Libre:
                                    <input type="checkbox" checked={ventaLibre} onChange={e => setVentaLibre(e.target.checked)} />
                                </label>
                            </div>

                            <div className="modal-actions">
                                <button className="btn-primary" onClick={saveMedicamento}>Guardar</button>
                                <button className="btn-secondary" onClick={closeModal}>Cancelar</button>
                            </div>
                        </div>
                    </div>
                )}
            </main>

        </div>
    );
};

export default Manager;
