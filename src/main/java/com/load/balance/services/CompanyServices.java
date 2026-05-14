package com.load.balance.services;

import com.load.balance.application.dtos.company.CreateCompanyDto;
import com.load.balance.application.shared.SlugGenerator;
import com.load.balance.application.usecase.companies.AddMemberAtCompany;
import com.load.balance.application.usecase.users.FindUserOrThrowUseCase;
import com.load.balance.enums.StatusCompany;
import com.load.balance.models.Company;
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
    private final AddMemberAtCompany addMemberAtCompany;

    public CompanyServices(
            CompanyRepository companyRepository,
            SlugGenerator slugGenerator,
            FindUserOrThrowUseCase findUserOrThrowUseCase,
            AddMemberAtCompany addMemberAtCompany
    ) {
        this.companyRepository = companyRepository;
        this.slugGenerator = slugGenerator;
        this.findUserOrThrowUseCase = findUserOrThrowUseCase;
        this.addMemberAtCompany = addMemberAtCompany;
    }

    @Transactional(readOnly = true)
    public List<Company> findBySlug(String slug, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return companyRepository.companiesBySlug(slug, userId);
    }

    @Transactional()
    public Company createCompany(CreateCompanyDto newCompany, HttpSession session) {
        String userIdStr = (String) session.getAttribute("userId");
        UUID userId = UUID.fromString(userIdStr);

        Users creator = this.findUserOrThrowUseCase.byId(userId);
        String slug = this.slugGenerator.generate(newCompany.getName());

        Company company = Company.builder()
                .name(newCompany.getName())
                .description(newCompany.getDescription())
                .status(StatusCompany.ACTIVE)
                .slug(slug)
                .createdBy(creator)
                .build();

        Company createCompany =  companyRepository.save(company);

        this.addMemberAtCompany.execute(createCompany, creator);

        return createCompany;
    }
}

