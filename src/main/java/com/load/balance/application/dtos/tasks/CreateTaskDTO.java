package com.load.balance.application.dtos.tasks;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateTaskDTO {
    private String name;

    private String description;

    private String status;

    private Integer priority;

    private UUID companyId;

    private UUID memberId;

    private int completedIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startedAt;
}
