package com.load.balance.controllers;

import com.load.balance.application.dtos.company.CreateCompanyDTO;
import com.load.balance.application.dtos.company.JoinCompanyDTO;
import com.load.balance.application.dtos.company.RemoveJoinCompanyDTO;
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

    @PostMapping
    public Company createCompany(@RequestBody CreateCompanyDTO company, HttpSession session) {
        return this.companyServices.createCompany(company, session);
    }

    @PostMapping("/join")
    public String joinCompany(@RequestBody JoinCompanyDTO joinCompany, HttpSession session) {
        this.companyServices.joinCompany(joinCompany, session);
        return "Success";
    }

    @DeleteMapping("/leave")
    public String leaveCompany(@RequestBody RemoveJoinCompanyDTO removeJoinCompanyDTO, HttpSession session) {
        this.companyServices.removeCompany(removeJoinCompanyDTO, session);
        return "Delete Success";
    }
}
