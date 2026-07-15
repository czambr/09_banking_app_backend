package com.czambra.mscuentas.dto.movimiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoPaginadoDTO {
    private List<MovimientoResponseDTO> movimientos;
    private long total;
    private int paginaActual;
    private int totalPaginas;
}
