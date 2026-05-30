package com.load.balance.services;

import com.load.balance.application.dtos.tasks.CreateTaskDTO;
import com.load.balance.application.dtos.tasks.UpdateTaskDTO;
import com.load.balance.application.usecase.companies.CheckUserInCompanyUseCase;
import com.load.balance.application.usecase.companies.FindCompanyOrThrowUseCase;
import com.load.balance.application.usecase.tasks.FindTaskOrThrowUseCase;
import com.load.balance.application.usecase.users.FindUserOrThrowUseCase;
import com.load.balance.models.Member;
import com.load.balance.models.Tasks;
import com.load.balance.models.Users;
import com.load.balance.repositories.TaskRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TaskServices {
    private final TaskRepository taskRepository;
    private final FindUserOrThrowUseCase findUserOrThrowUseCase;
    private final FindCompanyOrThrowUseCase findCompanyOrThrowUseCase;
    private final CheckUserInCompanyUseCase checkUserInCompanyUseCase;
    private final FindTaskOrThrowUseCase findTaskOrThrowUseCase;

    public TaskServices(
            TaskRepository taskRepository,
            FindUserOrThrowUseCase findUserOrThrowUseCase,
            FindCompanyOrThrowUseCase findCompanyOrThrowUseCase,
            CheckUserInCompanyUseCase checkUserInCompanyUseCase,
            FindTaskOrThrowUseCase findTaskOrThrowUseCase
    ) {
        this.taskRepository = taskRepository;
        this.findUserOrThrowUseCase = findUserOrThrowUseCase;
        this.findCompanyOrThrowUseCase = findCompanyOrThrowUseCase;
        this.checkUserInCompanyUseCase = checkUserInCompanyUseCase;
        this.findTaskOrThrowUseCase = findTaskOrThrowUseCase;
    }

    public String createTask(CreateTaskDTO createTaskDTO, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        Users user = this.findUserOrThrowUseCase.byId(sessionUserId);
        this.findCompanyOrThrowUseCase.byId(createTaskDTO.getCompanyId());

        int plusDays = Optional.of(createTaskDTO.getCompletedIn()).orElse(0);

        Member member = this.checkUserInCompanyUseCase.getMember(createTaskDTO.getUserId(), createTaskDTO.getCompanyId());

        LocalDateTime completedAt = createTaskDTO.getStartedAt().plusDays(plusDays);

        Tasks tasks = Tasks.builder()
                .createdBy(user)
                .member(member)
                .name(createTaskDTO.getName())
                .description(createTaskDTO.getDescription())
                .status(createTaskDTO.getStatus())
                .priority(createTaskDTO.getPriority())
                .completedIn(createTaskDTO.getCompletedIn())
                .completedAt(completedAt)
                .build();

        this.taskRepository.save(tasks);

        return "Create Task";
    }

    public Tasks update(UpdateTaskDTO updateTaskDTO, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        this.findUserOrThrowUseCase.byId(sessionUserId);

        Tasks task = this.findTaskOrThrowUseCase.byId(updateTaskDTO.getTaskId());

        if (updateTaskDTO.getName() != null) task.setName(updateTaskDTO.getName());
        if (updateTaskDTO.getDescription() != null) task.setDescription(updateTaskDTO.getDescription());
        if (updateTaskDTO.getStatus() != null) task.setStatus(updateTaskDTO.getStatus());
        if (updateTaskDTO.getPriority() != null) task.setPriority(updateTaskDTO.getPriority());
        if (updateTaskDTO.getCompletedIn() != null) task.setCompletedIn(updateTaskDTO.getCompletedIn());
        if (updateTaskDTO.getCompletedAt() != null) task.setCompletedAt(updateTaskDTO.getCompletedAt());

        return this.taskRepository.save(task);
    }
}
