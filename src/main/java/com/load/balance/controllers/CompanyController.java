package com.load.balance.controllers;

import com.load.balance.models.Company;
import com.load.balance.services.CompanyServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyServices companyServices;

    CompanyController(CompanyServices companyServices) {
        this.companyServices = companyServices;
    }

    @GetMapping
    public List<Company> getCompanies(@RequestParam String slug, HttpSession session) {
        return this.companyServices.findBySlug(slug, session);
    }
}
