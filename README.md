# Proyecto_Entornos

CREATE DATABASE Farmacos;
USE Farmacos;

CREATE TABLE Usuarios (  
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    nombre VARCHAR(100) NOT NULL,  
    apellido VARCHAR(100) NOT NULL,  
    email VARCHAR(255) UNIQUE NOT NULL,  
    telefono VARCHAR(20),  
    direccion TEXT,  
    rol ENUM('gerente', 'cliente') NOT NULL,  
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP  
);  

CREATE TABLE Credenciales (  
    credencial_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    usuario_id BIGINT NOT NULL,  
    usuario VARCHAR(100) UNIQUE NOT NULL,  
    contrasena TEXT NOT NULL,  
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE  
);  

CREATE TABLE Medicamentos (  
    medicamento_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    nombre VARCHAR(255) NOT NULL,  
    precio DECIMAL(10,2) NOT NULL,  
    imagen_med LONGBLOB,
    tipo ENUM('recetado', 'venta libre') NOT NULL  
);  

CREATE TABLE Ordenes (  
    orden_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    usuario_id BIGINT NOT NULL,  
    estado ENUM('pendiente', 'procesando', 'completada', 'cancelada') DEFAULT 'pendiente',  
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE  
);  

CREATE TABLE OrdenesMedicamentos (  
    orden_medicamento_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    orden_id BIGINT NOT NULL,  
    medicamento_id BIGINT NOT NULL,  
    cantidad INT NOT NULL CHECK (cantidad > 0),  
    imagen_orden LONGBLOB,  
    FOREIGN KEY (orden_id) REFERENCES Ordenes(orden_id) ON DELETE CASCADE,  
    FOREIGN KEY (medicamento_id) REFERENCES Medicamentos(medicamento_id) ON DELETE CASCADE  
);  

CREATE TABLE Pagos (  
    pago_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    orden_id BIGINT NOT NULL,  
    monto DECIMAL(10,2) NOT NULL,  
    comprobante VARCHAR(255) NOT NULL,  
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (orden_id) REFERENCES Ordenes(orden_id) ON DELETE CASCADE  
);  

CREATE TABLE HistorialCompras (  
    historial_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    usuario_id BIGINT NOT NULL,  
    orden_id BIGINT NOT NULL,  
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE,  
    FOREIGN KEY (orden_id) REFERENCES Ordenes(orden_id) ON DELETE CASCADE  
);  

CREATE TABLE Notificaciones (  
    notificacion_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    usuario_id BIGINT NOT NULL,  
    mensaje TEXT NOT NULL,  
    estado ENUM('leído', 'no leído') DEFAULT 'no leído',  
    fecha_notificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE  
);  

CREATE TABLE Ventas (  
    venta_id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    medicamento_id BIGINT NOT NULL,  
    cantidad INT NOT NULL CHECK (cantidad > 0),  
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (medicamento_id) REFERENCES Medicamentos(medicamento_id) ON DELETE CASCADE  
);
