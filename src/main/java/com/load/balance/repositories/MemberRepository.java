package com.load.balance.repositories;

import com.load.balance.models.Companies;
import com.load.balance.models.Member;
import com.load.balance.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.company.id = :companyId")
    Optional<Member> findByUserIdAndCompanyId(UUID userId, UUID companyId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Member m WHERE m.company = :company AND m.user = :user")
    void deleteByCompanyAndUser(Companies company, Users user);

    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.company.id = :companyId")
    Optional<Member> findMemberByUserIdAndCompanyId(UUID userId, UUID companyId);
}