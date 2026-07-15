package com.czambra.mscuentas.repository;

import com.czambra.mscuentas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByCuentaIdOrderByFechaDesc(@Param("cuentaId") Long cuentaId);

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento ASC")
    List<Movimiento> findByCuentaIdAndFechaBetween(
            @Param("cuentaId") Long cuentaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId IN :cuentaIds AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento ASC")
    List<Movimiento> findByCuentaIdInAndFechaBetween(
            @Param("cuentaIds") List<Long> cuentaIds,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findTopByCuentaIdOrderByFechaDesc(@Param("cuentaId") Long cuentaId);
}
