package com.czambra.msusers.dto;

import com.czambra.msusers.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String nombres;
    private String direccion;
    private String telefono;
    private Boolean estado;

    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getClienteId());
        dto.setNombres(cliente.getPersona().getNombre());
        dto.setDireccion(cliente.getPersona().getDireccion());
        dto.setTelefono(cliente.getPersona().getTelefono());
        dto.setEstado(cliente.getEstado());
        return dto;
    }
}
