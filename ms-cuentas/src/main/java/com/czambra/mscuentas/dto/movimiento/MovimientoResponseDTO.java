package com.czambra.mscuentas.dto.movimiento;

import com.czambra.mscuentas.entity.Movimiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoResponseDTO {

    private Long movimientoId;
    private Long cuentaId;
    private String tipoMovimiento;
    private BigDecimal monto;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoNuevo;
    private LocalDateTime fechaMovimiento;

    public static MovimientoResponseDTO fromEntity(Movimiento movimiento) {
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.setMovimientoId(movimiento.getMovimientoId());
        dto.setCuentaId(movimiento.getCuentaId());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setMonto(movimiento.getMonto());
        dto.setSaldoAnterior(movimiento.getSaldoAnterior());
        dto.setSaldoNuevo(movimiento.getSaldoNuevo());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        return dto;
    }
}
