package com.czambra.mscuentas.dto.cuenta;

import com.czambra.mscuentas.entity.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponseDTO {

    private Long cuentaId;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private Boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Long clienteId;

    public static CuentaResponseDTO fromEntity(Cuenta cuenta) {
        CuentaResponseDTO dto = new CuentaResponseDTO();
        dto.setCuentaId(cuenta.getCuentaId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldo(cuenta.getSaldo());
        dto.setEstado(cuenta.getEstado());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaActualizacion(cuenta.getFechaActualizacion());
        dto.setClienteId(cuenta.getClienteId());
        return dto;
    }
}
