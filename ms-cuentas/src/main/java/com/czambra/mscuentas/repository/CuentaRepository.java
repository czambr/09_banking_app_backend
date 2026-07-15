package com.czambra.mscuentas.repository;

import com.czambra.mscuentas.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.estado = true")
    List<Cuenta> findAllActive();

    @Query("SELECT c FROM Cuenta c WHERE c.estado = true AND c.cuentaId = :id")
    Optional<Cuenta> findActiveById(@Param("id") Long id);

    @Query("SELECT c FROM Cuenta c WHERE c.estado = true AND c.clienteId = :clienteId")
    List<Cuenta> findActiveByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT c FROM Cuenta c WHERE c.clienteId = :clienteId")
    List<Cuenta> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT c FROM Cuenta c WHERE c.numeroCuenta = :numeroCuenta")
    Optional<Cuenta> findByNumeroCuenta(@Param("numeroCuenta") String numeroCuenta);

    @Query("SELECT c FROM Cuenta c WHERE c.estado = true AND c.numeroCuenta = :numeroCuenta")
    Optional<Cuenta> findActiveByNumeroCuenta(@Param("numeroCuenta") String numeroCuenta);

    @Query(value = "SELECT p.nombre FROM persona p INNER JOIN cliente c ON c.persona_id = p.id WHERE c.cliente_id = :clienteId", nativeQuery = true)
    String findClienteNombreByClienteId(@Param("clienteId") Long clienteId);
}
