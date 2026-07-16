# Banking App - Docker

Levantar la aplicación completa con Docker.

## Requisitos

- Docker
- Docker Compose

## Puertos por defecto

| Servicio   | Puerto por defecto |
| ---------- | ------------------ |
| PostgreSQL | 5432               |
| ms-users   | 8080               |
| ms-cuentas | 8081               |

## Primeros pasos

1. Copiar el archivo `.env.template` a `.env`
2. Dejar las variables por defecto del `.env.template` (Recomendado).
3. Iniciar servicios usando `bash docker compose up -d `. Esto hara lo siguiente: Levantar la BD sin
   los esquemas, levantar el micro de usuarios (`ms-users`) y el microservicios de cuentas
   (Cuentas + Movimientos) `ms-cuentas`.
4. Probar conexion hacia la BD: `banking_db`
5. Antes de probar cualquier endpoint, necesitamos definir los esquemas. Correr el script de BD
   `BasesDatos.sql` en su visualizador de BD. Comprobar la creacion de los esquemas.
6. Probar los endpoints respectivos.

## Endpoints

- **ms-users**: http://localhost:8080
- **ms-cuentas**: http://localhost:8081
