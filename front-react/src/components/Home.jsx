import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';
import { useNavigate } from 'react-router-dom';

const API_URL = 'http://localhost:8080';

const Home = () => {
  const [usuario, setUsuario] = useState(null);
  const [medicamentos, setMedicamentos] = useState([]);
  const [orden, setOrden] = useState(null);
  const [error, setError] = useState(null);
  const [ordenError, setOrdenError] = useState(null);
  const navigate = useNavigate();
  const handleRegistroClick = () => {
    navigate('/Register'); // Ajusta la ruta según tu estructura
  };
  const [direccion, setDireccion] = useState('');
  const [imagenBase64, setImagenBase64] = useState('');
  const [medicamentoPendiente, setMedicamentoPendiente] = useState(null);
  const [mostrarModalImagen, setMostrarModalImagen] = useState(false);


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

  useEffect(() => {
    const usuarioData = JSON.parse(localStorage.getItem('userData'));
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
            setOrdenError('El usuario no tiene un carrito, añade un medicamento primero');
          }
        } else {
          console.error('Error al obtener la orden pendiente:', err.response || err.message);
          setOrdenError('No se pudo cargar el carrito');
        }
      });
  }, []);


  const agregarMedicamento = (med) => {
    if (!med.ventaLibre) {
      // Si no es venta libre, abrir modal para pedir imagen
      setMedicamentoPendiente(med);
      setMostrarModalImagen(true);
    } else {
      // Si es venta libre, agregar directo
      agregarMedicamentoConImagen(med, '');
    }
  };
  const agregarMedicamentoConImagen = async (med, imagen) => {
    const usuarioData = JSON.parse(localStorage.getItem('userData'));
    const token = localStorage.getItem('token');

    const base64Image = imagen ? imagen.split(",")[1] : '';

    const body = {
      idOrden: orden ? orden.idOrden : null,
      idMedicamento: med.id,
      cantidad: 1,
      imagen: base64Image,
    };

    console.log('Intentando agregar medicamento con imagen:', {
      idOrden: orden ? orden.idOrden : null,
      idMedicamento: med.id,
      cantidad: 1,
      imagen: imagen,
      token,
      idUsuario: usuarioData.idUsuario
    });

    try {
      await axios.post(`${API_URL}/api/orden/addMedicamento`, body, {
        headers: {
          Authorization: `Bearer ${token}`,
          idUsuario: usuarioData.idUsuario,
        }
      });
      // Actualizar orden
      const resOrdenActualizada = await axios.get(`${API_URL}/api/orden/ordenPendiente/${usuarioData.idUsuario}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      });
      setOrden(resOrdenActualizada.data);
      setOrdenError(null);
    } catch (error) {
      console.error('Error agregando medicamento:', error.response || error.message);
      setOrdenError('No se pudo agregar el medicamento');
    }

    setMostrarModalImagen(false);
    setMedicamentoPendiente(null);
    setImagenBase64('');
  };

  // Función para convertir archivo a base64
  const onFileChange = (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      setImagenBase64(reader.result);
    };
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
      const usuarioData = JSON.parse(localStorage.getItem('userData'));
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
            <li onClick={() => navigate('/perfil')}>Usuario</li>
            <li onClick={() => navigate('/historial')}>Historial</li>
          </ul>
        </nav>
        <div className="register-box">
          <p>¿Eres Gerente de una Farmacia?</p>
          <button onClick={handleRegistroClick}>Regístrate</button>
        </div>
        <div className="logout-box">
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
          <button className='small-btn' onClick={() => navigate('/Perfil')}>Cambiar</button>
        </div>


        <div className="cart-box">
          <h2>Tu Carrito</h2>
          {orden && orden.medicamentos && orden.medicamentos.length > 0 ? (
            <ul>
              <ul>
                {orden.medicamentos.map(med => {
                  return (
                    <li key={med.idMedicamento}>
                      {med.nombreMedicamento} x {med.cantidad} - ${med.precioMedicamento.toFixed(2)}
                      <button onClick={() => eliminarMedicamento(med)} className="remove-btn">-</button>
                    </li>
                  );
                })}

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
      {mostrarModalImagen && (
        <div className="modal">
          <h3>Este medicamento no es de venta libre, por favor cargue la orden médica</h3>
          <input type="file" accept="image/*" onChange={onFileChange} />
          <button
            disabled={!imagenBase64}
            onClick={() => agregarMedicamentoConImagen(medicamentoPendiente, imagenBase64)}
          >
            Enviar
          </button>
          <button onClick={() => setMostrarModalImagen(false)}>Cancelar</button>
        </div>
      )}
    </div>
  );
};

export default Home;
