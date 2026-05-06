package com.load.balance.controllers;

import com.load.balance.models.Company;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @GetMapping("/{slug}")
    public List<Company> getCompanies(@RequestParam String slug, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return List.of(
                new Company(),
                new Company(),
                new Company()
        );
    }
}
