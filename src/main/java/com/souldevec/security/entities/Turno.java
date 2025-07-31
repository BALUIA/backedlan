package com.souldevec.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private BigDecimal efectivo;
    private BigDecimal yape;
    private BigDecimal snacks;
    private BigDecimal ingresoInventario;
    private BigDecimal consumo; // Gastos adicionales
    private BigDecimal retiros;
    private BigDecimal dineroPancafe;
    private BigDecimal usanzaPancafe;
    private BigDecimal kw;
    private Integer usuarios; // Número de usuarios

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
