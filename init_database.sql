-- Script de inicialización de Base de Datos PostgreSQL
-- Ejecutar: psql -U postgres -f init_database.sql

-- Crear base de datos
CREATE DATABASE ms_users;

-- Conectarse a la base de datos
\connect ms_users;

-- ============================================
-- TABLA: persona (datos personales)
-- ============================================
CREATE TABLE IF NOT EXISTS persona (
    id BIGSERIAL PRIMARY KEY,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

-- ============================================
-- TABLA: cliente (tiene su propio ID)
-- ============================================
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id BIGSERIAL PRIMARY KEY,
    persona_id BIGINT NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
);

-- ============================================
-- TABLA: cuenta (cuentas bancarias)
-- ============================================
CREATE TABLE IF NOT EXISTS cuenta (
    cuenta_id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(10) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    estado BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id) ON DELETE CASCADE
);

-- ============================================
-- TABLA: movimiento (transacciones)
-- ============================================
CREATE TABLE IF NOT EXISTS movimiento (
    movimiento_id BIGSERIAL PRIMARY KEY,
    cuenta_id BIGINT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    monto NUMERIC(19, 2) NOT NULL,
    saldo_anterior NUMERIC(19, 2) NOT NULL,
    saldo_nuevo NUMERIC(19, 2) NOT NULL,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(cuenta_id) ON DELETE CASCADE
);

-- ============================================
-- ÍNDICES
-- ============================================
CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON persona(identificacion);
CREATE INDEX IF NOT EXISTS idx_cliente_persona ON cliente(persona_id);
CREATE INDEX IF NOT EXISTS idx_cuenta_numero ON cuenta(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cuenta_estado ON cuenta(estado);
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta ON movimiento(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento(fecha_movimiento);
