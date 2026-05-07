package com.load.balance.repositories;

import com.load.balance.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT c FROM Company c WHERE c.slug = :slug AND c.user_id = :user_id")
    List<Company> companiesBySlug(@RequestParam String slug, String user_id);
}
