import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material';
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Perfil from './components/Perfil';
import Manager from './components/Manager';
import History from './components/History';
import Orders from './components/Orders';

const theme = createTheme({
  palette: {
    primary: {
      main: '#2e7d32', // Verde que coincide con el logo
    },
    secondary: {
      main: '#f50057',
    },
  },
});

function PrivateRoute({ children }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/" replace />;
}

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          <Route
            path="/home"
            element={
              <PrivateRoute>
                <Home />
              </PrivateRoute>
            }
          />
          <Route
            path="/medicamentos"
            element={
              <PrivateRoute>
                <div>Página de Medicamentos (En desarrollo)</div>
              </PrivateRoute>
            }
          />
          <Route
            path="/perfil"
            element={
              <PrivateRoute>
                <Perfil />
              </PrivateRoute>
            }
          />
          <Route
            path="/management"
            element={
              <PrivateRoute>
                <Manager />
              </PrivateRoute>
            }
          />
          <Route
            path="/historial"
            element={
              <PrivateRoute>
                <History />
              </PrivateRoute>
            }
          />
          <Route
            path="/ordenes"
            element={
              <PrivateRoute>
                <Orders />
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}
