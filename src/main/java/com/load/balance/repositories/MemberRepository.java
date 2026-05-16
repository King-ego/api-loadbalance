package com.load.balance.repositories;

import com.load.balance.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.company.id = :companyId")
    Optional<Member> findByUserIdAndCompanyId(UUID userId, UUID companyId);
}
