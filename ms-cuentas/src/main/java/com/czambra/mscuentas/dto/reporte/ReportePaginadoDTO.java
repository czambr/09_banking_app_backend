package com.czambra.mscuentas.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportePaginadoDTO {
    private List<EstadoCuentaDTO> movimientos;
    private long total;
    private int paginaActual;
    private int totalPaginas;
}
