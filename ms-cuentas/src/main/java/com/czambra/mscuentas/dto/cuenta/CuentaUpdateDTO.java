package com.czambra.mscuentas.dto.cuenta;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaUpdateDTO {

    @Size(max = 50, message = "El número de cuenta no puede exceder 50 caracteres")
    private String numeroCuenta;

    @Size(max = 50, message = "El tipo de cuenta no puede exceder 50 caracteres")
    private String tipoCuenta;

    @DecimalMin(value = "0.0", message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    private Boolean estado;
}
