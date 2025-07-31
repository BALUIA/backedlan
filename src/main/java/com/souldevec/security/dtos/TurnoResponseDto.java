package com.souldevec.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDto {

    private Long id;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private BigDecimal efectivo;
    private BigDecimal yape;
    private BigDecimal snacks;
    private BigDecimal ingresoInventario;
    private BigDecimal consumo;
    private BigDecimal retiros;
    private BigDecimal dineroPancafe;
    private BigDecimal usanzaPancafe;
    private BigDecimal kw;
    private Integer usuarios;
    private String userName;
}
