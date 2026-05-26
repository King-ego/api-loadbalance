package com.load.balance.services;

import com.load.balance.application.dtos.tasks.CreateTaskDTO;
import com.load.balance.application.usecase.companies.CheckUserInCompanyUseCase;
import com.load.balance.application.usecase.companies.FindCompanyOrThrowUseCase;
import com.load.balance.application.usecase.users.FindUserOrThrowUseCase;
import com.load.balance.models.Companies;
import com.load.balance.models.Member;
import com.load.balance.models.Tasks;
import com.load.balance.models.Users;
import com.load.balance.repositories.TaskRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TaskServices {
    private final TaskRepository taskRepository;
    private final FindUserOrThrowUseCase findUserOrThrowUseCase;
    private final FindCompanyOrThrowUseCase findCompanyOrThrowUseCase;
    private final CheckUserInCompanyUseCase checkUserInCompanyUseCase;
    public TaskServices(TaskRepository taskRepository,  FindUserOrThrowUseCase findUserOrThrowUseCase,  FindCompanyOrThrowUseCase findCompanyOrThrowUseCase, CheckUserInCompanyUseCase checkUserInCompanyUseCase) {
        this.taskRepository = taskRepository;
        this.findUserOrThrowUseCase = findUserOrThrowUseCase;
        this.findCompanyOrThrowUseCase = findCompanyOrThrowUseCase;
        this.checkUserInCompanyUseCase = checkUserInCompanyUseCase;

    }

    public String createTask(CreateTaskDTO createTaskDTO, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        Users user = this.findUserOrThrowUseCase.byId(sessionUserId);
        this.findCompanyOrThrowUseCase.byId(createTaskDTO.getCompanyId());

        Member member = this.checkUserInCompanyUseCase.getMember(createTaskDTO.getUserId(), createTaskDTO.getCompanyId());

        Tasks tasks = Tasks.builder()
                .createdBy(user)
                .member(member)
                .name(createTaskDTO.getName())
                .description(createTaskDTO.getDescription())
                .status(createTaskDTO.getStatus())
                .priority(createTaskDTO.getPriority())
                .build();

        this.taskRepository.save(tasks);

        return "Create Task";
    }
}

