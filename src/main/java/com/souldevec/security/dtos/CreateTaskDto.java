package com.souldevec.security.dtos;

import lombok.Data;

@Data
public class CreateTaskDto {
    private String description;
    private String assignedToUserId;
}
