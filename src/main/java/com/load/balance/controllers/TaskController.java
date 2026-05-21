package com.load.balance.controllers;

import com.load.balance.services.TaskServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskServices taskServices;

    TaskController(TaskServices taskServices){
        this.taskServices = taskServices;
    }
    @PostMapping
    public String createTask() {
        return this.taskServices.createTask();
    }
}
