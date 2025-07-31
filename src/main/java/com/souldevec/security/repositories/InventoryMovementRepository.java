package com.souldevec.security.repositories;

import com.souldevec.security.entities.InventoryMovement;
import com.souldevec.security.entities.Product;
import com.souldevec.security.entities.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findByTurno(Turno turno);
    List<InventoryMovement> findByProductOrderByTimestampDesc(Product product);
}
