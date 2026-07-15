-- Script de inicialización de Base de Datos PostgreSQL para ms-users
-- Ejecutar: psql -U postgres -f init_database.sql

-- Crear base de datos
CREATE DATABASE ms_users;

-- Conectarse a la base de datos
\connect ms_users;

-- Tabla Persona (entidad base)
CREATE TABLE IF NOT EXISTS persona (
    id BIGSERIAL PRIMARY KEY,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

-- Tabla Cliente (hereda de Persona)
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id BIGINT PRIMARY KEY,
    contrasena VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    FOREIGN KEY (cliente_id) REFERENCES persona(id) ON DELETE CASCADE
);

-- Tabla Cuenta
CREATE TABLE IF NOT EXISTS cuenta (
    cuenta_id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL,
    estado VARCHAR(20),
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
);

-- Crear índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON persona(identificacion);
-- CREATE INDEX IF NOT EXISTS idx_cliente_codigo ON cliente(codigo_cliente);
CREATE INDEX IF NOT EXISTS idx_cuenta_numero ON cuenta(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuenta_cliente ON cuenta(cliente_id);

-- Inserts de ejemplo (comentados)
-- INSERT INTO persona (identificacion, nombre, genero, edad, direccion, telefono) 
-- VALUES ('1234567890', 'Juan Pérez', 'M', 30, 'Calle Principal 123', '1234567890');

-- INSERT INTO cliente (id, codigo_cliente, contraseña, estado)
-- VALUES (1, 'CLI001', 'password123', 'ACTIVO');

-- INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo, estado, fecha_creacion, cliente_id)
-- VALUES ('1000123456', 'AHORROS', 5000.00, 'ACTIVA', NOW(), 1);
