package com.czambra.mscuentas.service;

import com.czambra.mscuentas.dto.cuenta.CuentaResponseDTO;
import com.czambra.mscuentas.dto.reporte.EstadoCuentaDTO;
import com.czambra.mscuentas.dto.reporte.ReportePaginadoDTO;
import com.czambra.mscuentas.entity.Cuenta;
import com.czambra.mscuentas.entity.Movimiento;
import com.czambra.mscuentas.repository.CuentaRepository;
import com.czambra.mscuentas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String obtenerNombreCliente(Long clienteId) {
        String nombre = cuentaRepository.findClienteNombreByClienteId(clienteId);
        return (nombre != null && !nombre.isEmpty()) ? nombre : "Cliente ID: " + clienteId;
    }

    public ReportePaginadoDTO generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin, int page, int size) {
        String nombreCliente = obtenerNombreCliente(clienteId);

        List<Cuenta> cuentasEntity = cuentaRepository.findByClienteId(clienteId);
        List<CuentaResponseDTO> cuentas = cuentasEntity.stream()
                .map(CuentaResponseDTO::fromEntity)
                .collect(Collectors.toList());

        if (cuentas.isEmpty()) {
            return new ReportePaginadoDTO(new ArrayList<>(), 0, 0, 0);
        }

        List<Long> cuentaIds = cuentas.stream()
                .map(CuentaResponseDTO::getCuentaId)
                .collect(Collectors.toList());

        LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);

        Pageable pageable = PageRequest.of(page, size);
        Page<Movimiento> movimientoPage = movimientoRepository.findByCuentaIdsAndFechaBetween(
                cuentaIds, fechaInicioDateTime, fechaFinDateTime, pageable);

        List<Movimiento> movimientos = movimientoPage.getContent();
        Map<Long, List<Movimiento>> movimientosPorCuenta = movimientos.stream()
                .collect(Collectors.groupingBy(Movimiento::getCuentaId));

        List<EstadoCuentaDTO> reportes = new ArrayList<>();

        for (CuentaResponseDTO cuenta : cuentas) {
            List<Movimiento> movimientosCuenta = movimientosPorCuenta.getOrDefault(cuenta.getCuentaId(), new ArrayList<>());

            BigDecimal saldoInicial = BigDecimal.ZERO;

            List<Movimiento> ultimoMovimiento = movimientoRepository.findTopByCuentaIdOrderByFechaDesc(cuenta.getCuentaId());
            if (!ultimoMovimiento.isEmpty()) {
                saldoInicial = ultimoMovimiento.get(0).getSaldoNuevo();
            }

            if (!movimientosCuenta.isEmpty()) {
                BigDecimal saldoActual = saldoInicial;
                for (Movimiento mov : movimientosCuenta) {
                    BigDecimal montoMovimiento;
                    String tipoTransaccion;

                    if (mov.getTipoMovimiento().equals("DEPOSITO")) {
                        montoMovimiento = mov.getMonto();
                        tipoTransaccion = "DEPOSITO";
                    } else {
                        montoMovimiento = mov.getMonto().negate();
                        tipoTransaccion = "RETIRO";
                    }

                    EstadoCuentaDTO reporte = new EstadoCuentaDTO();
                    reporte.setFecha(mov.getFechaMovimiento().format(FORMATO_FECHA));
                    reporte.setCliente(nombreCliente);
                    reporte.setNumeroCuenta(cuenta.getNumeroCuenta());
                    reporte.setTipo(cuenta.getTipoCuenta());
                    reporte.setSaldoInicial(saldoActual);
                    reporte.setEstado(cuenta.getEstado());
                    reporte.setTipoTransaccion(tipoTransaccion);
                    reporte.setMovimiento(montoMovimiento);
                    reporte.setSaldoDisponible(mov.getSaldoNuevo());
                    reportes.add(reporte);

                    saldoActual = mov.getSaldoNuevo();
                }
            }
        }

        return new ReportePaginadoDTO(
                reportes,
                movimientoPage.getTotalElements(),
                movimientoPage.getNumber(),
                movimientoPage.getTotalPages()
        );
    }
}
