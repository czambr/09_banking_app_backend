package com.czambra.mscuentas.dto.cuenta;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCreateDTO {

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 50, message = "El número de cuenta no puede exceder 50 caracteres")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Size(max = 50, message = "El tipo de cuenta no puede exceder 50 caracteres")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo no puede ser negativo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Min(value = 1, message = "El ID del cliente debe ser mayor a 0")
    private Long clienteId;

    private Boolean estado;
}
