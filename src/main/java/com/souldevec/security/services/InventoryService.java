package com.souldevec.security.services;

import com.souldevec.security.entities.InventoryMovement;
import com.souldevec.security.repositories.InventoryMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    public List<InventoryMovement> findAll() {
        return inventoryMovementRepository.findAll();
    }

    public InventoryMovement save(InventoryMovement movement) {
        return inventoryMovementRepository.save(movement);
    }
}
