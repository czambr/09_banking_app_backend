package com.czambra.mscuentas.service;

import com.czambra.mscuentas.dto.movimiento.MovimientoCreateDTO;
import com.czambra.mscuentas.dto.movimiento.MovimientoPaginadoDTO;
import com.czambra.mscuentas.dto.movimiento.MovimientoResponseDTO;
import com.czambra.mscuentas.entity.Cuenta;
import com.czambra.mscuentas.entity.Movimiento;
import com.czambra.mscuentas.exception.SaldoNoDisponibleException;
import com.czambra.mscuentas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaService cuentaService;

    @Transactional
    public MovimientoResponseDTO crear(MovimientoCreateDTO dto) {
        Cuenta cuenta = cuentaService.getCuentaEntity(dto.getCuentaId());

        BigDecimal saldoAnterior = cuenta.getSaldo();
        BigDecimal monto = dto.getMonto();
        BigDecimal saldoNuevo;
        String tipoMovimiento = dto.getTipoMovimiento().toUpperCase();

        if (tipoMovimiento.equals("RETIRO")) {
            if (saldoAnterior.compareTo(monto) < 0) {
                throw new SaldoNoDisponibleException("Saldo no disponible");
            }
            saldoNuevo = saldoAnterior.subtract(monto);
        } else if (tipoMovimiento.equals("DEPOSITO")) {
            saldoNuevo = saldoAnterior.add(monto);
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Use DEPOSITO o RETIRO");
        }

        cuenta.setSaldo(saldoNuevo);
        cuentaService.actualizarSaldo(cuenta.getCuentaId(), saldoNuevo);

        Movimiento movimiento = new Movimiento();
        movimiento.setCuentaId(cuenta.getCuentaId());
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setMonto(monto);
        movimiento.setSaldoAnterior(saldoAnterior);
        movimiento.setSaldoNuevo(saldoNuevo);

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        return MovimientoResponseDTO.fromEntity(movimientoGuardado);
    }

    public List<MovimientoResponseDTO> obtenerPorCuentaId(Long cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId).stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MovimientoPaginadoDTO obtenerPorCuentaIdPaginado(Long cuentaId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Movimiento> movimientoPage = movimientoRepository.findByCuentaId(cuentaId, pageable);

        List<MovimientoResponseDTO> movimientos = movimientoPage.getContent().stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new MovimientoPaginadoDTO(
                movimientos,
                movimientoPage.getTotalElements(),
                page,
                movimientoPage.getTotalPages()
        );
    }
}
