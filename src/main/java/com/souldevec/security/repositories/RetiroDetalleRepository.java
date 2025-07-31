package com.souldevec.security.repositories;

import com.souldevec.security.entities.RetiroDetalle;
import com.souldevec.security.entities.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetiroDetalleRepository extends JpaRepository<RetiroDetalle, Long> {
    List<RetiroDetalle> findByTurno(Turno turno);
}
