package com.czambra.mscuentas.service;

import com.czambra.mscuentas.dto.cuenta.CuentaResponseDTO;
import com.czambra.mscuentas.dto.reporte.EstadoCuentaDTO;
import com.czambra.mscuentas.entity.Cuenta;
import com.czambra.mscuentas.entity.Movimiento;
import com.czambra.mscuentas.repository.CuentaRepository;
import com.czambra.mscuentas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<EstadoCuentaDTO> generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        String nombreCliente = obtenerNombreCliente(clienteId);

        List<Cuenta> cuentasEntity = cuentaRepository.findByClienteId(clienteId);
        List<CuentaResponseDTO> cuentas = cuentasEntity.stream()
                .map(CuentaResponseDTO::fromEntity)
                .collect(Collectors.toList());

        if (cuentas.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> cuentaIds = cuentas.stream()
                .map(CuentaResponseDTO::getCuentaId)
                .collect(Collectors.toList());

        LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);

        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdInAndFechaBetween(
                cuentaIds, fechaInicioDateTime, fechaFinDateTime);

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

            if (movimientosCuenta.isEmpty()) {
                EstadoCuentaDTO reporte = new EstadoCuentaDTO();
                reporte.setFecha(fechaInicio.format(FORMATO_FECHA));
                reporte.setCliente(nombreCliente);
                reporte.setNumeroCuenta(cuenta.getNumeroCuenta());
                reporte.setTipo(cuenta.getTipoCuenta());
                reporte.setSaldoInicial(saldoInicial);
                reporte.setEstado(cuenta.getEstado());
                reporte.setMovimiento(BigDecimal.ZERO);
                reporte.setSaldoDisponible(cuenta.getSaldo());
                reportes.add(reporte);
            } else {
                BigDecimal saldoActual = saldoInicial;
                for (Movimiento mov : movimientosCuenta) {
                    BigDecimal montoMovimiento = mov.getMonto();

                    EstadoCuentaDTO reporte = new EstadoCuentaDTO();
                    reporte.setFecha(mov.getFechaMovimiento().format(FORMATO_FECHA));
                    reporte.setCliente(nombreCliente);
                    reporte.setNumeroCuenta(cuenta.getNumeroCuenta());
                    reporte.setTipo(cuenta.getTipoCuenta());
                    reporte.setSaldoInicial(saldoActual);
                    reporte.setEstado(cuenta.getEstado());
                    reporte.setMovimiento(montoMovimiento);
                    reporte.setSaldoDisponible(mov.getSaldoNuevo());
                    reportes.add(reporte);

                    saldoActual = mov.getSaldoNuevo();
                }
            }
        }

        return reportes;
    }
}
