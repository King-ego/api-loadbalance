package com.load.balance.services;

import com.load.balance.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServices {
    private final TaskRepository taskRepository;
    public TaskServices(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public String createTask() {
        return "Create Task";
    }
}
