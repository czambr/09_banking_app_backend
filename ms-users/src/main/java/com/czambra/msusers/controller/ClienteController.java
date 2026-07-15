package com.czambra.msusers.controller;

import com.czambra.msusers.dto.ClienteCreateDTO;
import com.czambra.msusers.dto.ClienteResponseDTO;
import com.czambra.msusers.dto.ClienteUpdateDTO;
import com.czambra.msusers.entity.Cliente;
import com.czambra.msusers.entity.Persona;
import com.czambra.msusers.repository.ClienteRepository;
import com.czambra.msusers.repository.PersonaRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerTodos() {
        List<ClienteResponseDTO> clientes = clienteRepository.findAllActive().stream()
                .map(ClienteResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return clienteRepository.findActiveById(id)
                .map(cliente -> ResponseEntity.ok(ClienteResponseDTO.fromEntity(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ClienteCreateDTO dto) {
        if (clienteRepository.findByIdentificacion(dto.getIdentificacion()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"La identificación ya existe\"}");
        }

        Persona persona = new Persona();
        persona.setIdentificacion(dto.getIdentificacion());
        persona.setNombre(dto.getNombre());
        persona.setGenero(dto.getGenero());
        persona.setEdad(dto.getEdad());
        persona.setDireccion(dto.getDireccion());
        persona.setTelefono(dto.getTelefono());

        Persona personaGuardada = personaRepository.save(persona);

        Cliente cliente = new Cliente();
        cliente.setPersona(personaGuardada);
        cliente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        cliente.setEstado(dto.getEstado() != null ? dto.getEstado() : true);

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.fromEntity(clienteGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDTO dto) {
        return clienteRepository.findActiveById(id)
                .map(clienteExistente -> {
                    if (dto.getIdentificacion() != null
                            && !clienteExistente.getPersona().getIdentificacion().equals(dto.getIdentificacion())
                            && clienteRepository.findActiveByIdentificacion(dto.getIdentificacion()).isPresent()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("{\"error\": \"La identificación ya existe\"}");
                    }

                    Persona persona = clienteExistente.getPersona();

                    if (dto.getIdentificacion() != null) persona.setIdentificacion(dto.getIdentificacion());
                    if (dto.getNombre() != null) persona.setNombre(dto.getNombre());
                    if (dto.getGenero() != null) persona.setGenero(dto.getGenero());
                    if (dto.getEdad() != null) persona.setEdad(dto.getEdad());
                    if (dto.getDireccion() != null) persona.setDireccion(dto.getDireccion());
                    if (dto.getTelefono() != null) persona.setTelefono(dto.getTelefono());

                    if (dto.getContrasena() != null) clienteExistente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
                    if (dto.getEstado() != null) clienteExistente.setEstado(dto.getEstado());

                    Cliente clienteActualizado = clienteRepository.save(clienteExistente);
                    return ResponseEntity.ok(ClienteResponseDTO.fromEntity(clienteActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return clienteRepository.findActiveById(id)
                .map(cliente -> {
                    cliente.setEstado(false);
                    clienteRepository.save(cliente);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
