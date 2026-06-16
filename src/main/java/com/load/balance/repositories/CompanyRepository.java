package com.load.balance.repositories;

import com.load.balance.models.Companies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Companies, UUID> {
    @Query("SELECT c FROM Companies c JOIN c.members u WHERE c.slug = :slug AND u.id = :userId")
    List<Companies> companiesBySlug(@RequestParam String slug, UUID userId);

    @Query("SELECT c FROM Companies WHERE c.created_by_id = :creator_id")
    Optional<Companies> findByCreatorId(@RequestParam UUID creatorId);
}