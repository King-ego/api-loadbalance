package com.load.balance.controllers;

import com.load.balance.models.Company;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @GetMapping()
    public List<Company> getCompanies(HttpSession session) {
        return List.of(
                new Company(),
                new Company(),
                new Company()
        );
    }
}
