package com.souldevec.security.dtos;

import lombok.Data;
import java.util.List;

@Data
public class ProductHistoryDto {
    private Long productId;
    private String productName;
    private Integer currentStock;
    private List<InventoryMovementResponseDto> movements;
}
