package com.czambra.msusers.repository;

import com.czambra.msusers.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.estado = true")
    List<Cliente> findAllActive();

    @Query("SELECT c FROM Cliente c WHERE c.estado = true AND c.clienteId = :id")
    Optional<Cliente> findActiveById(@Param("id") Long id);

    @Query("SELECT c FROM Cliente c WHERE c.estado = true AND c.persona.identificacion = :identificacion")
    Optional<Cliente> findActiveByIdentificacion(@Param("identificacion") String identificacion);

    @Query("SELECT c FROM Cliente c WHERE c.persona.identificacion = :identificacion")
    Optional<Cliente> findByIdentificacion(@Param("identificacion") String identificacion);

}
