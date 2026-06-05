package com.load.balance.services;

import com.load.balance.application.dtos.tasks.CreateTaskDTO;
import com.load.balance.application.dtos.tasks.UpdateTaskDTO;
import com.load.balance.application.helpers.auth.AuthHelper;
import com.load.balance.application.shared.CompareDate;
import com.load.balance.application.usecase.companies.CheckUserInCompanyUseCase;
import com.load.balance.application.usecase.companies.FindCompanyOrThrowUseCase;
import com.load.balance.application.usecase.tasks.FindTaskOrThrowUseCase;
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
    private final FindCompanyOrThrowUseCase findCompanyOrThrowUseCase;
    private final CheckUserInCompanyUseCase checkUserInCompanyUseCase;
    private final FindTaskOrThrowUseCase findTaskOrThrowUseCase;
    private final AuthHelper authHelper;

    public TaskServices(
            TaskRepository taskRepository,
            FindCompanyOrThrowUseCase findCompanyOrThrowUseCase,
            CheckUserInCompanyUseCase checkUserInCompanyUseCase,
            FindTaskOrThrowUseCase findTaskOrThrowUseCase,
            AuthHelper authHelper
    ) {
        this.taskRepository = taskRepository;
        this.findCompanyOrThrowUseCase = findCompanyOrThrowUseCase;
        this.checkUserInCompanyUseCase = checkUserInCompanyUseCase;
        this.findTaskOrThrowUseCase = findTaskOrThrowUseCase;
        this.authHelper = authHelper;
    }

    public String createTask(CreateTaskDTO createTaskDTO, HttpSession session) {
        /*String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        Users user = this.findUserOrThrowUseCase.byId(sessionUserId);*/
        Users creator = authHelper.getSessionUser();
        this.findCompanyOrThrowUseCase.byId(createTaskDTO.getCompanyId());

        int plusDays = Optional.of(createTaskDTO.getCompletedIn()).orElse(0);

        Member member = checkUserInCompanyUseCase.getMember(createTaskDTO.getUserId(), createTaskDTO.getCompanyId());

        LocalDateTime completedAt = createTaskDTO.getStartedAt().plusDays(plusDays);

        Tasks tasks = Tasks.builder()
                .createdBy(creator)
                .member(member)
                .name(createTaskDTO.getName())
                .description(createTaskDTO.getDescription())
                .status(createTaskDTO.getStatus())
                .priority(createTaskDTO.getPriority())
                .completedIn(createTaskDTO.getCompletedIn())
                .completedAt(completedAt)
                .build();

        taskRepository.save(tasks);

        return "Create Task";
    }

    public Tasks update(UpdateTaskDTO updateTaskDTO, HttpSession session) {
        /*String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        this.findUserOrThrowUseCase.byId(sessionUserId);*/

        authHelper.getSessionUser();

        Tasks task = findTaskOrThrowUseCase.byId(updateTaskDTO.getTaskId());

        if (updateTaskDTO.getName() != null) task.setName(updateTaskDTO.getName());
        if (updateTaskDTO.getDescription() != null) task.setDescription(updateTaskDTO.getDescription());
        if (updateTaskDTO.getStatus() != null) task.setStatus(updateTaskDTO.getStatus());
        if (updateTaskDTO.getPriority() != null) task.setPriority(updateTaskDTO.getPriority());
        if (updateTaskDTO.getCompletedIn() != null) task.setCompletedIn(updateTaskDTO.getCompletedIn());
        if (updateTaskDTO.getCompletedAt() != null) task.setCompletedAt(updateTaskDTO.getCompletedAt());

        return taskRepository.save(task);
    }

    public void concludeTask(UUID taskId) {
        Users user = authHelper.getSessionUser();
        Tasks task = findTaskOrThrowUseCase.byId(taskId);

        findCompanyOrThrowUseCase.byId(task.getCompany().getId());
        checkUserInCompanyUseCase.exist(user.getId(), task.getCompany().getId());

        boolean afterDate = new CompareDate().after(task.getCompletedAt(), LocalDateTime.now());
    }
}
