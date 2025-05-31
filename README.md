# Proyecto_Entornos
-- Eliminar y crear base de datos
DROP DATABASE IF EXISTS farmacos;
CREATE DATABASE farmacos;

-- Tabla roles
CREATE TABLE roles(
id SERIAL PRIMARY KEY,
rol VARCHAR(100),
fecha_desde DATE,
fecha_hasta DATE
);

-- Tabla usuarios
CREATE TABLE usuarios(
id SERIAL PRIMARY KEY,
nombres VARCHAR(100),
apellidos VARCHAR(150),
email VARCHAR(100) UNIQUE,
telefono VARCHAR(10),
direccion VARCHAR(200),
id_rol INT,
fecha_creacion DATE,
FOREIGN KEY (id_rol) REFERENCES roles(id)
);

-- Tabla credenciales
CREATE TABLE credenciales(
id SERIAL PRIMARY KEY,
id_usuario INT,
usuario VARCHAR(100) UNIQUE,
contrasena VARCHAR(500),
fecha_creacion DATE,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Tabla medicamentos
CREATE TABLE medicamentos(
id SERIAL PRIMARY KEY,
nombre VARCHAR(200),
precio DECIMAL(10,2),
imagen VARCHAR(500),
venta_libre INT
);

-- Tabla estados
CREATE TABLE estados(
id SERIAL PRIMARY KEY,
estado VARCHAR(100)
);

-- Tabla orden
CREATE TABLE orden(
id SERIAL PRIMARY KEY,
id_usuario INT,
id_estado INT,
fecha_creacion DATE,
fecha_completado DATE,
fecha_rechazo DATE,
fecha_modificacion DATE,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
FOREIGN KEY (id_estado) REFERENCES estados(id)
);

-- Tabla orden_medicamentos
CREATE TABLE orden_medicamentos(
id SERIAL PRIMARY KEY,
id_orden INT,
id_medicamento INT,
cantidad INT,
imagen VARCHAR(500),
FOREIGN KEY (id_orden) REFERENCES orden(id),
FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id)
);

-- Insertar datos de ejemplo
INSERT INTO roles (rol, fecha_desde) VALUES
('GERENTE', CURRENT_DATE),
('CLIENTE', CURRENT_DATE);

INSERT INTO estados (estado) VALUES
('PENDIENTE'),
('PROCESANDO'),
('COMPLETADA'),
('CANCELADA');
