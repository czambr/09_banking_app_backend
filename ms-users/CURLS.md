# CURLs para probar endpoints de Clientes

## Base URL
```
http://localhost:8080/clientes
```

## 1. Obtener todos los clientes (GET)
```bash
curl -X GET http://localhost:8080/clientes
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombres": "Juan Perez",
    "direccion": "Calle 123, Ciudad",
    "telefono": "0999999999",
    "estado": true
  }
]
```

## 2. Obtener cliente por ID (GET)
```bash
curl -X GET http://localhost:8080/clientes/1
```

## 3. Crear cliente (POST)
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "identificacion": "1234567890",
    "nombre": "Juan Perez",
    "genero": "MASCULINO",
    "edad": 30,
    "direccion": "Calle 123, Ciudad",
    "telefono": "0999999999",
    "contrasena": "password123",
    "estado": true
  }'
```

## 4. Actualizar cliente (PUT) - campos opcionales
```bash
# Actualizar solo el nombre
curl -X PUT http://localhost:8080/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Perez Actualizado"
  }'

# Actualizar varios campos
curl -X PUT http://localhost:8080/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Perez",
    "direccion": "Nueva Direccion",
    "telefono": "0999999999"
  }'
```

## 5. Eliminar cliente (soft delete) (DELETE)
```bash
curl -X DELETE http://localhost:8080/clientes/1
```
*Cambia el estado a false*

---

## Ejemplos con validaciones

### Crear cliente sin nombre (error de validación)
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "identificacion": "1234567890",
    "nombre": "",
    "edad": 30,
    "contrasena": "password123"
  }'
```

**Respuesta de error:**
```json
{
  "timestamp": "2026-07-15T18:30:00",
  "status": 400,
  "error": "Validación fallida",
  "message": "Error de validación",
  "details": [
    { "field": "nombre", "message": "El nombre es obligatorio" }
  ],
  "path": "/clientes"
}
```

### Crear cliente con identificación duplicada
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "identificacion": "1234567890",
    "nombre": "Otro Cliente",
    "edad": 25,
    "contrasena": "password456"
  }'
```

### Crear cliente sin contraseña
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "identificacion": "0987654321",
    "nombre": "Cliente Sin Pass",
    "edad": 25
  }'
```
