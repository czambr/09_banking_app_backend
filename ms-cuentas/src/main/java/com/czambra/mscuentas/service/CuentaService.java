package com.czambra.mscuentas.service;

import com.czambra.mscuentas.dto.cuenta.CuentaCreateDTO;
import com.czambra.mscuentas.dto.cuenta.CuentaResponseDTO;
import com.czambra.mscuentas.dto.cuenta.CuentaUpdateDTO;
import com.czambra.mscuentas.entity.Cuenta;
import com.czambra.mscuentas.exception.RecursoNoEncontradoException;
import com.czambra.mscuentas.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<CuentaResponseDTO> obtenerTodas() {
        return cuentaRepository.findAllActive().stream()
                .map(CuentaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CuentaResponseDTO> obtenerPorClienteId(Long clienteId) {
        return cuentaRepository.findActiveByClienteId(clienteId).stream()
                .map(CuentaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public CuentaResponseDTO obtenerPorId(Long id) {
        return cuentaRepository.findActiveById(id)
                .map(CuentaResponseDTO::fromEntity)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));
    }

    @Transactional
    public CuentaResponseDTO crear(CuentaCreateDTO dto) {
        if (cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta()).isPresent()) {
            throw new IllegalArgumentException("El número de cuenta ya existe");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldo(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : java.math.BigDecimal.ZERO);
        cuenta.setClienteId(dto.getClienteId());
        cuenta.setEstado(dto.getEstado() != null ? dto.getEstado() : true);

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return CuentaResponseDTO.fromEntity(cuentaGuardada);
    }

    @Transactional
    public CuentaResponseDTO actualizar(Long id, CuentaUpdateDTO dto) {
        Cuenta cuenta = cuentaRepository.findActiveById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));

        if (dto.getNumeroCuenta() != null && !cuenta.getNumeroCuenta().equals(dto.getNumeroCuenta())) {
            if (cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta()).isPresent()) {
                throw new IllegalArgumentException("El número de cuenta ya existe");
            }
            cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        }

        if (dto.getTipoCuenta() != null) {
            cuenta.setTipoCuenta(dto.getTipoCuenta());
        }

        if (dto.getSaldo() != null) {
            cuenta.setSaldo(dto.getSaldo());
        }

        if (dto.getEstado() != null) {
            cuenta.setEstado(dto.getEstado());
        }

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return CuentaResponseDTO.fromEntity(cuentaActualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        Cuenta cuenta = cuentaRepository.findActiveById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));

        cuenta.setEstado(false);
        cuentaRepository.save(cuenta);
    }

    public Cuenta getCuentaEntity(Long id) {
        return cuentaRepository.findActiveById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));
    }

    @Transactional
    public CuentaResponseDTO actualizarSaldo(Long id, java.math.BigDecimal nuevoSaldo) {
        Cuenta cuenta = cuentaRepository.findActiveById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));

        cuenta.setSaldo(nuevoSaldo);
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return CuentaResponseDTO.fromEntity(cuentaActualizada);
    }
}
