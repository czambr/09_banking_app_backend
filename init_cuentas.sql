-- Script de inicialización de Base de Datos PostgreSQL para ms-cuentas
-- Ejecutar: psql -U postgres -f init_cuentas.sql
-- Este script incluye las tablas de cuenta y movimiento

-- Conectarse a la base de datos (asegurarse que ms_users existe)
\connect ms_users;

-- Tabla Cuenta
CREATE TABLE IF NOT EXISTS cuenta (
    cuenta_id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    estado BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    cliente_id BIGINT NOT NULL
);

-- Tabla Movimiento
CREATE TABLE IF NOT EXISTS movimiento (
    movimiento_id BIGSERIAL PRIMARY KEY,
    cuenta_id BIGINT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    monto NUMERIC(19, 2) NOT NULL,
    saldo_anterior NUMERIC(19, 2) NOT NULL,
    saldo_nuevo NUMERIC(19, 2) NOT NULL,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Crear índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_cuenta_numero ON cuenta(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cuenta_estado ON cuenta(estado);
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta ON movimiento(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento(fecha_movimiento);

-- Agregar FK después de crear las tablas (para evitar errores de dependencias)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_cuenta_cliente'
    ) THEN
        ALTER TABLE cuenta ADD CONSTRAINT fk_cuenta_cliente
            FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_movimiento_cuenta'
    ) THEN
        ALTER TABLE movimiento ADD CONSTRAINT fk_movimiento_cuenta
            FOREIGN KEY (cuenta_id) REFERENCES cuenta(cuenta_id) ON DELETE CASCADE;
    END IF;
END $$;

-- Inserts de ejemplo (comentados)
-- INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo, estado, fecha_creacion, cliente_id)
-- VALUES ('1000123456', 'AHORROS', 5000.00, true, NOW(), 1);

-- INSERT INTO movimiento (cuenta_id, tipo_movimiento, monto, saldo_anterior, saldo_nuevo, fecha_movimiento)
-- VALUES (1, 'DEPOSITO', 500.00, 5000.00, 5500.00, NOW());
