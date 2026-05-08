package com.load.balance.services;

import com.load.balance.application.dtos.company.CreateCompanyDto;
import com.load.balance.application.shared.SlugGenerator;
import com.load.balance.enums.StatusCompany;
import com.load.balance.models.Company;
import com.load.balance.models.Users;
import com.load.balance.repositories.CompanyRepository;
import com.load.balance.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServices {
    private final CompanyRepository companyRepository;
    private final SlugGenerator slugGenerator;
    private final UserRepository userRepository;

    public CompanyServices(
            CompanyRepository companyRepository,
            SlugGenerator slugGenerator,
            UserRepository userRepository
    ) {
        this.companyRepository = companyRepository;
        this.slugGenerator = slugGenerator;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Company> findBySlug(String slug, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return companyRepository.companiesBySlug(slug, userId);
    }

    @Transactional()
    public Company createCompany(CreateCompanyDto newCompany, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");


        Optional<Users> user = this.userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users userDetails = user.get();

        Company company = Company.builder()
                .name(newCompany.getName())
                .description(newCompany.getDescription())
                .status(StatusCompany.ACTIVE)
                .slug(slugGenerator.generate(newCompany.getName()))
                .createdBy(userDetails)
                .build();
        return companyRepository.save(company);
    }
}
