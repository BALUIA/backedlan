package com.souldevec.security.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GastoResponseDto {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String userName;
}
