package com.czambra.mscuentas.dto.movimiento;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCreateDTO {

    private Long cuentaId;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "El tipo de movimiento debe ser DEPOSITO o RETIRO")
    private String tipoMovimiento;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;
}
