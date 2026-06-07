package com.load.balance.services;

import com.load.balance.application.dtos.tasks.CreateTaskDTO;
import com.load.balance.application.dtos.tasks.UpdateTaskDTO;
import com.load.balance.application.helpers.auth.AuthHelper;
import com.load.balance.application.shared.CompareDate;
import com.load.balance.application.usecase.companies.CheckUserInCompanyUseCase;
import com.load.balance.application.usecase.companies.FindCompanyOrThrowUseCase;
import com.load.balance.application.usecase.tasks.FindTaskOrThrowUseCase;
import com.load.balance.enums.TransactionType;
import com.load.balance.models.Member;
import com.load.balance.models.Penalty;
import com.load.balance.models.Tasks;
import com.load.balance.models.Transaction;
import com.load.balance.models.Users;
import com.load.balance.repositories.MemberRepository;
import com.load.balance.repositories.PenaltyRepository;
import com.load.balance.repositories.TaskRepository;
import com.load.balance.repositories.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TaskServices {

    private static final BigDecimal PENALTY_RATE_PER_DAY = new BigDecimal("50.00");

    private final TaskRepository taskRepository;
    private final FindCompanyOrThrowUseCase findCompanyOrThrowUseCase;
    private final CheckUserInCompanyUseCase checkUserInCompanyUseCase;
    private final FindTaskOrThrowUseCase findTaskOrThrowUseCase;
    private final AuthHelper authHelper;
    private final PenaltyRepository penaltyRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    public TaskServices(
            TaskRepository taskRepository,
            FindCompanyOrThrowUseCase findCompanyOrThrowUseCase,
            CheckUserInCompanyUseCase checkUserInCompanyUseCase,
            FindTaskOrThrowUseCase findTaskOrThrowUseCase,
            AuthHelper authHelper,
            PenaltyRepository penaltyRepository,
            MemberRepository memberRepository,
            TransactionRepository transactionRepository
    ) {
        this.taskRepository = taskRepository;
        this.findCompanyOrThrowUseCase = findCompanyOrThrowUseCase;
        this.checkUserInCompanyUseCase = checkUserInCompanyUseCase;
        this.findTaskOrThrowUseCase = findTaskOrThrowUseCase;
        this.authHelper = authHelper;
        this.penaltyRepository = penaltyRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
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
        Member member = checkUserInCompanyUseCase.getMember(user.getId(), task.getCompany().getId());

        LocalDateTime now = LocalDateTime.now();
        boolean onTime = new CompareDate().after(task.getCompletedAt(), now);

        task.setStatus("CONCLUDED");
        task.setConcludedAt(now);
        taskRepository.save(task);

        if (onTime) {
            int weight = task.getPriority() != null ? task.getPriority() : 1;
            member.setPoints(member.getPoints() + weight);
            memberRepository.save(member);
            log.info("Member {} earned {} point(s) for concluding task {} on time", member.getId(), weight, taskId);
        } else {
            applyFinancialPenalty(member, task, now);
        }
    }

    private void applyFinancialPenalty(Member member, Tasks task, LocalDateTime now) {
        long daysLate = ChronoUnit.DAYS.between(task.getCompletedAt(), now);
        if (daysLate < 1) daysLate = 1;

        BigDecimal penaltyAmount = PENALTY_RATE_PER_DAY.multiply(BigDecimal.valueOf(daysLate));
        BigDecimal balanceBefore = member.getBalance() != null ? member.getBalance() : BigDecimal.ZERO;
        BigDecimal balanceAfter = balanceBefore.subtract(penaltyAmount);

        member.setBalance(balanceAfter);
        memberRepository.save(member);

        Transaction transaction = Transaction.builder()
                .member(member)
                .type(TransactionType.DEBIT)
                .amount(penaltyAmount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(String.format("Late penalty: %d day(s) overdue on task '%s'", daysLate, task.getName()))
                .build();
        transactionRepository.save(transaction);

        Penalty penalty = Penalty.builder()
                .member(member)
                .task(task)
                .reason("Task concluded after deadline")
                .amount(penaltyAmount)
                .daysLate((int) daysLate)
                .build();
        penaltyRepository.save(penalty);

        log.info("Financial penalty of {} applied to member {} ({} day(s) late on task {})",
                penaltyAmount, member.getId(), daysLate, task.getId());
    }
}