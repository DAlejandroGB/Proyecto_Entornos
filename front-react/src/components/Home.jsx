import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import { useNavigate } from 'react-router-dom';

const API_URL = 'http://localhost:8080';

const Home = () => {
  const [medicamentos, setMedicamentos] = useState([]);
  const [orden, setOrden] = useState(null);
  const [error, setError] = useState(null);
  const [ordenError, setOrdenError] = useState(null);
  const navigate = useNavigate();
  const handleRegistroClick = () => {
    navigate('/Register'); // Ajusta la ruta según tu estructura
  };
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

  useEffect(() => {
    const usuarioData = JSON.parse(localStorage.getItem('usuario'));
    const token = localStorage.getItem('token');

    if (!usuarioData?.idUsuario) {
      console.log('usuarioData localStorage:', usuarioData);
      setOrdenError('Usuario no autenticado');
      return;
    }

    axios.get(`${API_URL}/api/orden/ordenPendiente/${usuarioData.idUsuario}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
      .then(res => {
        setOrden(res.data);
        console.log('Orden recibida:', res.data);
      })
      .catch(async err => {
        if (err.response && err.response.status === 404) {
          // Orden pendiente no existe, crearla
          try {
            const crearOrdenRes = await axios.post(
              `${API_URL}/api/orden/crearOrdenPendiente/${usuarioData.idUsuario}`,
              {},
              {
                headers: {
                  'Authorization': `Bearer ${token}`
                }
              }
            );
            setOrden(crearOrdenRes.data);
            setOrdenError(null);
          } catch (crearErr) {
            console.error('Error creando orden pendiente:', crearErr);
            setOrdenError('No se pudo crear el carrito');
          }
        } else {
          console.error('Error al obtener la orden pendiente:', err.response || err.message);
          setOrdenError('No se pudo cargar el carrito');
        }
      });
  }, []);


  const agregarMedicamento = async (med) => {
    try {
      const usuarioData = JSON.parse(localStorage.getItem('usuario'));
      const token = localStorage.getItem('token');

      if (!usuarioData?.idUsuario) {
        setOrdenError('Usuario no autenticado');
        return;
      }

      const body = {
        idOrden: orden ? orden.idOrden : null,
        idMedicamento: med.id,
        cantidad: 1,
        imagen: med.imagen || '',
      };

      await axios.post(`${API_URL}/api/orden/addMedicamento`, body, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'idUsuario': usuarioData.idUsuario
        }
      });

      // Luego, hacer GET para obtener la orden actualizada
      const resOrdenActualizada = await axios.get(`${API_URL}/api/orden/ordenPendiente/${usuarioData.idUsuario}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      setOrden(resOrdenActualizada.data);
      setOrdenError(null);

    } catch (error) {
      console.error('Error agregando medicamento:', error.response || error.message);
      setOrdenError('No se pudo agregar el medicamento');
    }
  };

  const calcularTotal = () => {
    if (!orden || !orden.medicamentos) return 0;
    return orden.medicamentos.reduce((total, med) => {
      const precio = med.precioMedicamento || 0;
      const cantidad = med.cantidad || 0;
      return total + precio;
    }, 0);
  };

  const eliminarMedicamento = async (med) => {
    try {
      const token = localStorage.getItem('token');

      const body = {
        idOrden: orden.idOrden,
        idMedicamento: med.idMedicamento,
        cantidad: 1,  // asumo que quieres eliminar 1 unidad
        imagen: med.imagen || '',
        nombreMedicamento: med.nombreMedicamento,
        precioMedicamento: med.precioMedicamento
      };

      await axios.delete(`${API_URL}/api/orden/deleteMedicamento`, {
        headers: {
          'Authorization': `Bearer ${token}`
        },
        data: body // En DELETE con axios, el body va en `data`
      });

      // Luego, recargar la orden actualizada para sincronizar el estado
      const usuarioData = JSON.parse(localStorage.getItem('usuario'));
      const response = await axios.get(`${API_URL}/api/orden/ordenPendiente/${usuarioData.idUsuario}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      setOrden(response.data);
      setOrdenError(null);

    } catch (error) {
      console.error('Error eliminando medicamento:', error.response || error.message);
      setOrdenError('No se pudo eliminar el medicamento');
    }
  };


  return (
    <div className="home-container">
      <aside className="sidebar">
        <h1 className="logo">TeFaltan <span className="highlight">Pastillas?</span></h1>
        <nav>
          <ul>
            <li className="active">Inicio</li>
            <li>Usuario</li>
            <li>Historial</li>
            <li>Configuración</li>
          </ul>
        </nav>
        <div className="register-box">
          <p>¿Eres Gerente de una Farmacia?</p>
          <button onClick={handleRegistroClick}>Regístrate</button>
        </div>
      </aside>

      <main className="main-content">
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
                  <button className="add-btn" onClick={() => agregarMedicamento(med)}>
                    +
                  </button>

                </div>
              );
            })}
          </div>
        )}

      </main>

      <aside className="order-sidebar">
        <div className="address-box">
          <p className="section-title">Tu dirección</p>
          <p>{direccion || 'No has configurado tu dirección'}</p>
          <button className="small-btn">Cambiar</button>
        </div>


        <div className="cart-box">
          <h2>Tu Carrito</h2>
          {orden && orden.medicamentos && orden.medicamentos.length > 0 ? (
            <ul>
              <ul>
                {orden.medicamentos.map(med => (
                  <li key={med.idMedicamento}>
                    {med.nombreMedicamento} x {med.cantidad} - ${med.precioMedicamento.toFixed(2)}
                    <button onClick={() => eliminarMedicamento(med)} className="remove-btn">-</button>
                  </li>
                ))}
              </ul>
            </ul>
          ) : (
            <p>No tienes medicamentos en tu orden pendiente.</p>
          )}
          <div className="total">
            <p>Total</p>
            <p>${calcularTotal().toFixed(2)}</p>
          </div>
          <button className="buy-btn">Comprar</button>
          {ordenError && <p className="error">{ordenError}</p>}
        </div>
      </aside>
    </div>
  );
};

export default Home;
