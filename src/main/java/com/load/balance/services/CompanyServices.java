package com.load.balance.services;

import com.load.balance.application.dtos.company.CreateCompanyDTO;
import com.load.balance.application.dtos.company.JoinCompanyDTO;
import com.load.balance.application.dtos.company.RemoveJoinCompanyDTO;
import com.load.balance.application.shared.SlugGenerator;
import com.load.balance.application.usecase.companies.MemberAtCompany;
import com.load.balance.application.usecase.companies.CheckUserInCompanyUseCase;
import com.load.balance.application.usecase.companies.FindCompanyOrThrowUseCase;
import com.load.balance.application.usecase.users.FindUserOrThrowUseCase;
import com.load.balance.enums.StatusCompany;
import com.load.balance.models.Companies;
import com.load.balance.models.Users;
import com.load.balance.repositories.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CompanyServices {
    private final CompanyRepository companyRepository;
    private final SlugGenerator slugGenerator;
    private final FindUserOrThrowUseCase findUserOrThrowUseCase;
    private final MemberAtCompany memberAtCompany;
    private final FindCompanyOrThrowUseCase findCompanyOrThrowUseCase;
    private final CheckUserInCompanyUseCase checkUserInCompanyUseCase;

    public CompanyServices(
            CompanyRepository companyRepository,
            SlugGenerator slugGenerator,
            FindUserOrThrowUseCase findUserOrThrowUseCase,
            MemberAtCompany memberAtCompany,
            FindCompanyOrThrowUseCase findCompanyOrThrowUseCase,
            CheckUserInCompanyUseCase checkUserInCompanyUseCase
    ) {
        this.companyRepository = companyRepository;
        this.slugGenerator = slugGenerator;
        this.findUserOrThrowUseCase = findUserOrThrowUseCase;
        this.memberAtCompany = memberAtCompany;
        this.findCompanyOrThrowUseCase = findCompanyOrThrowUseCase;
        this.checkUserInCompanyUseCase = checkUserInCompanyUseCase;
    }

    @Transactional(readOnly = true)
    public List<Companies> findBySlug(String slug, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);
        return companyRepository.companiesBySlug(slug, sessionUserId);
    }

    @Transactional()
    public Companies createCompany(CreateCompanyDTO newCompany, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        Users creator = this.findUserOrThrowUseCase.byId(sessionUserId);
        String slug = this.slugGenerator.generate(newCompany.getName());

        Companies company = Companies.builder()
                .name(newCompany.getName())
                .description(newCompany.getDescription())
                .status(StatusCompany.ACTIVE)
                .slug(slug)
                .createdBy(creator)
                .build();

        Companies createCompany =  companyRepository.save(company);

        this.memberAtCompany.add(createCompany, creator);

        return createCompany;
    }

    @Transactional()
    public void joinCompany(JoinCompanyDTO joinCompany, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        this.findUserOrThrowUseCase.byId(sessionUserId);
        Users addUser = this.findUserOrThrowUseCase.byId(joinCompany.getUserId());
        Companies company = this.findCompanyOrThrowUseCase.byId(joinCompany.getCompanyId());

        this.checkUserInCompanyUseCase.exist(sessionUserId, joinCompany.getCompanyId());
        this.checkUserInCompanyUseCase.notExist(joinCompany.getUserId(), joinCompany.getCompanyId());

        this.memberAtCompany.add(company, addUser);
    }

    @Transactional()
    public void removeCompany(RemoveJoinCompanyDTO removeCompany, HttpSession session) {
        String sessionUserIdStr = (String) session.getAttribute("userId");
        UUID sessionUserId = UUID.fromString(sessionUserIdStr);

        this.findUserOrThrowUseCase.byId(sessionUserId);

        Users removeUser = this.findUserOrThrowUseCase.byId(removeCompany.getUserId());
        Companies company = this.findCompanyOrThrowUseCase.byId(removeCompany.getCompanyId());

        this.checkUserInCompanyUseCase.exist(sessionUserId, removeCompany.getCompanyId());
        this.checkUserInCompanyUseCase.exist(removeCompany.getUserId(), removeCompany.getCompanyId());

        this.memberAtCompany.remove(company, removeUser);


    }
}

