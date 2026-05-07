package com.load.balance.services;

import com.load.balance.application.dtos.company.CreateCompanyDto;
import com.load.balance.enums.StatusCompany;
import com.load.balance.models.Company;
import com.load.balance.repositories.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyServices {
    private final CompanyRepository companyRepository;

    public CompanyServices(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<Company> findBySlug(String slug, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return companyRepository.companiesBySlug(slug, userId);
    }

    @Transactional()
    public Company createCompany(CreateCompanyDto newCompany, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        Company company = Company.builder()
                .name(newCompany.getName())
                .description(newCompany.getDescription())
                .status(StatusCompany.ACTIVE)
                .build();
        return companyRepository.save(company);
    }
}
