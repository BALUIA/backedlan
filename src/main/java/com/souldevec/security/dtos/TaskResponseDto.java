package com.souldevec.security.dtos;

import com.souldevec.security.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {
    private Long id;
    private String description;
    private TaskStatus status;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRealizacion;
    private String asignadoA;
    private String creadoPor;
}
