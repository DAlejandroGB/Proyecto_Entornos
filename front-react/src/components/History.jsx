import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './History.css';
import { useNavigate } from 'react-router-dom';

const API_URL = 'http://localhost:8080';

const History = () => {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      const userData = JSON.parse(localStorage.getItem('userData'));
      const token = localStorage.getItem('token');

      const response = await axios.get(
        `${API_URL}/api/orden/byIdUsuario/${userData.idUsuario}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Ordenar las órdenes por fecha de creación (más recientes primero)
      const sortedOrders = (response.data.content || []).sort((a, b) => 
        new Date(b.fechaCreacion) - new Date(a.fechaCreacion)
      );
      
      setOrders(sortedOrders);
      setError(null);
    } catch (error) {
      console.error('Error loading orders:', error);
      // Solo mostrar error si no es un 404 (no hay órdenes)
      if (error.response && error.response.status !== 404) {
        setError('Error al cargar el historial de órdenes');
      }
      setOrders([]);
    }
  };

  const cancelOrder = async (orderId) => {
    try {
      const token = localStorage.getItem('token');
      
      await axios.put(
        `${API_URL}/api/orden?idOrden=${orderId}&estado=CANCELADA`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Reload orders after cancellation
      loadOrders();
      setError(null);
    } catch (error) {
      console.error('Error canceling order:', error);
      setError('Error al cancelar la orden');
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      // Convertir la fecha que viene como "YYYY-MM-DD" a un array
      const [year, month, day] = dateString.split('-');
      // Retornar en formato DD/MM/YYYY
      return `${day}/${month}/${year}`;
    } catch (error) {
      console.error('Error formatting date:', error);
      return dateString; // Retornar la fecha original si hay error
    }
  };

  const calcularTotal = (medicamentos) => {
    if (!medicamentos || medicamentos.length === 0) return 0;
    return medicamentos.reduce((total, med) => {
      return total + med.precioMedicamento;
    }, 0);
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'PENDIENTE':
        return 'status-pending';
      case 'PROCESANDO':
        return 'status-processing';
      case 'COMPLETADA':
        return 'status-completed';
      case 'CANCELADA':
        return 'status-cancelled';
      default:
        return '';
    }
  };

  const handleRegistroClick = () => {
    navigate('/Register');
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  return (
    <div className="home-container">
      <aside className="sidebar">
        <h1 className="logo">TeFaltan <span className="highlight">Pastillas?</span></h1>
        <nav>
          <ul>
            <li onClick={() => navigate('/home')}>Inicio</li>
            <li onClick={() => navigate('/perfil')}>Usuario</li>
            <li className="active">Historial</li>
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
        <h2 className="section-title">Historial de Órdenes</h2>
        {error && <div className="error-message">{error}</div>}
        
        <div className="orders-list">
          {orders && orders.length > 0 ? (
            orders.map((order, index) => (
              <div key={order.idOrden} className="order-card">
                <div className="order-header">
                  <h2>Orden #{index + 1}</h2>
                  <span className={`order-status ${getStatusClass(order.estado)}`}>
                    {order.estado}
                  </span>
                </div>
                
                <div className="order-details">
                  <p><strong>Fecha de creación:</strong> {formatDate(order.fechaCreacion)}</p>
                  {order.fechaCompletada && (
                    <p><strong>Fecha de completado:</strong> {formatDate(order.fechaCompletada)}</p>
                  )}
                  {order.fechaRechazo && (
                    <p><strong>Fecha de rechazo:</strong> {formatDate(order.fechaRechazo)}</p>
                  )}
                </div>

                <div className="order-items">
                  <h3>Medicamentos:</h3>
                  <ul>
                    {order.medicamentos.map((med) => (
                      <li key={med.idMedicamento}>
                        {med.nombreMedicamento} x {med.cantidad} - 
                        ${med.precioMedicamento}
                      </li>
                    ))}
                  </ul>
                </div>

                <div className="order-total">
                  <strong>Total de la orden:</strong> ${calcularTotal(order.medicamentos)}
                </div>

                {order.estado !== 'CANCELADA' && order.estado !== 'COMPLETADA' && (
                  <button 
                    className="cancel-button"
                    onClick={() => cancelOrder(order.idOrden)}
                  >
                    Cancelar Orden
                  </button>
                )}
              </div>
            ))
          ) : (
            <div className="no-orders-message">
              <h3>No tienes órdenes en tu historial</h3>
              <p>Cuando realices una compra, podrás ver tus órdenes aquí.</p>
              <button className="primary-button" onClick={() => navigate('/home')}>
                Ir a comprar
              </button>
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default History; 