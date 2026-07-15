package com.czambra.msusers.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testClienteCreation() {
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setNombre("Juan Perez");

        Cliente cliente = new Cliente();
        cliente.setClienteId(100L);
        cliente.setPersona(persona);
        cliente.setContrasena("password123");
        cliente.setEstado(true);

        assertEquals(100L, cliente.getClienteId());
        assertEquals("password123", cliente.getContrasena());
        assertTrue(cliente.getEstado());
        assertEquals("Juan Perez", cliente.getPersona().getNombre());
    }
}
