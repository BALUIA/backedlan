package com.souldevec.security.dtos;

import com.souldevec.security.enums.MovementType;
import lombok.Data;

@Data
public class InventoryMovementDto {
    private Long productId;
    private Integer quantity;
    private MovementType type;
}
