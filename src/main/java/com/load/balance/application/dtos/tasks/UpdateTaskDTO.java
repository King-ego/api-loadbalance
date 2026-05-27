package com.load.balance.application.dtos.tasks;

import java.time.LocalDateTime;

public class UpdateTaskDTO {
    private String name;

    private String description;

    private String status;

    private Integer priority;

    private int completedIn;
    private LocalDateTime completedAt;
}
