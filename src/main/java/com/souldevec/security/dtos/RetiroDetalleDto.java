package com.souldevec.security.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RetiroDetalleDto {
    private String description;
    private BigDecimal amount;
}
