package com.souldevec.security.entities;

import com.souldevec.security.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaRealizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignado_a_id", nullable = false)
    private User asignadoA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", nullable = false)
    private User creadoPor;
}
