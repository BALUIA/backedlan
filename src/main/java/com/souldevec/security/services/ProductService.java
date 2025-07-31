package com.souldevec.security.services;

import com.souldevec.security.dtos.InventoryMovementResponseDto;
import com.souldevec.security.dtos.ProductHistoryDto;
import com.souldevec.security.entities.InventoryMovement;
import com.souldevec.security.entities.Product;
import com.souldevec.security.repositories.InventoryMovementRepository;
import com.souldevec.security.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<ProductHistoryDto> getProductHistory() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductHistoryDto).collect(Collectors.toList());
    }

    private ProductHistoryDto mapToProductHistoryDto(Product product) {
        ProductHistoryDto dto = new ProductHistoryDto();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setCurrentStock(product.getStock());

        List<InventoryMovement> movements = inventoryMovementRepository.findByProductOrderByTimestampDesc(product);
        List<InventoryMovementResponseDto> movementDtos = movements.stream().map(movement -> {
            InventoryMovementResponseDto movementDto = new InventoryMovementResponseDto();
            movementDto.setProductName(movement.getProduct().getName());
            movementDto.setQuantity(movement.getQuantity());
            movementDto.setType(movement.getType());
            movementDto.setPrice(movement.getProduct().getSellingPrice());
            movementDto.setTotalPrice(movement.getProduct().getSellingPrice().multiply(new java.math.BigDecimal(movement.getQuantity())));
            movementDto.setStockAfterMovement(movement.getStockAfterMovement());
            movementDto.setTimestamp(movement.getTimestamp());
            return movementDto;
        }).collect(Collectors.toList());

        dto.setMovements(movementDtos);
        return dto;
    }
}
