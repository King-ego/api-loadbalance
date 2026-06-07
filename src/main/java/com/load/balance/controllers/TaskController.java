package com.load.balance.controllers;

import com.load.balance.application.dtos.tasks.CreateTaskDTO;
import com.load.balance.application.dtos.tasks.UpdateTaskDTO;
import com.load.balance.models.Tasks;
import com.load.balance.services.TaskServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskServices taskServices;

    TaskController(TaskServices taskServices){
        this.taskServices = taskServices;
    }

    @PostMapping
    public String createTask(@RequestBody CreateTaskDTO createTaskDTO, HttpSession session) {
        return this.taskServices.createTask(createTaskDTO, session);
    }

    @PutMapping
    public Tasks updateTask(@RequestBody UpdateTaskDTO updateTaskDTO, HttpSession session) {
        return this.taskServices.update(updateTaskDTO, session);
    }

    @PatchMapping("/{taskId}/conclude")
    public void concludeTask(@PathVariable UUID taskId) {
        this.taskServices.concludeTask(taskId);
    }
}
