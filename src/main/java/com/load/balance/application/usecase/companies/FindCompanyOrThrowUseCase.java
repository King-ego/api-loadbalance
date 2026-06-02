package com.load.balance.application.usecase.companies;

import com.load.balance.models.Companies;
import com.load.balance.repositories.CompanyRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindCompanyOrThrowUseCase {
    private final CompanyRepository companyRepository;
    private FindCompanyOrThrowUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Companies byId(UUID companyId) {
        return this.companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
}