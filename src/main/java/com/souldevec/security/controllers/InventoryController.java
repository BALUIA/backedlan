package com.souldevec.security.controllers;

import com.souldevec.security.entities.InventoryMovement;
import com.souldevec.security.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryMovement>> getAllMovements() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<InventoryMovement> createMovement(@RequestBody InventoryMovement movement) {
        return ResponseEntity.ok(inventoryService.save(movement));
    }
}
