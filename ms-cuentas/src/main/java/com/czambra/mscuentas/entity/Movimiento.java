package com.czambra.mscuentas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long movimientoId;

    @NotNull(message = "El ID de la cuenta es obligatorio")
    @Column(name = "cuenta_id", nullable = false)
    private Long cuentaId;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo de movimiento no puede exceder 20 caracteres")
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @NotNull(message = "El saldo anterior es obligatorio")
    @Column(name = "saldo_anterior", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoAnterior;

    @NotNull(message = "El saldo nuevo es obligatorio")
    @Column(name = "saldo_nuevo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoNuevo;

    @Column(name = "fecha_movimiento", nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento;

    @PrePersist
    protected void onCreate() {
        fechaMovimiento = LocalDateTime.now();
    }
}
