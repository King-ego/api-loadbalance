package com.load.balance.services;

import com.load.balance.models.Company;
import com.load.balance.repositories.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServices {
    private final CompanyRepository companyRepository;

    public CompanyServices(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findBySlug(String slug, HttpSession session) {
         String userId = (String) session.getAttribute("userId");
        return companyRepository.companiesBySlug(slug, userId);
    }
}
