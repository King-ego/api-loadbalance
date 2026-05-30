package com.load.balance.application.usecase.tasks;

import com.load.balance.models.Tasks;
import com.load.balance.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Component
public class FindTaskOrThrowUseCase {
    private final TaskRepository taskRepository;

    public FindTaskOrThrowUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Tasks byId(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task by id not found: {}", taskId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
                });
    }
}
