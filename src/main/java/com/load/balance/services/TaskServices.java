package com.load.balance.services;

import com.load.balance.application.dtos.tasks.CreateTaskDTO;
import com.load.balance.repositories.TaskRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServices {
    private final TaskRepository taskRepository;
    public TaskServices(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public String createTask(CreateTaskDTO createTaskDTO, HttpSession session) {
        return "Create Task";
    }
}
