package com.load.balance.application.dtos.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateTaskDTO {
    private UUID taskId;

    private String name;

    private String description;

    private String status;

    private Integer priority;

    private Integer completedIn;

    private LocalDateTime completedAt;
}
