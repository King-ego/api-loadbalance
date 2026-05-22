package com.load.balance.application.usecase.companies;

import com.load.balance.models.Member;
import com.load.balance.repositories.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CheckUserInCompanyUseCase {
    private final MemberRepository memberRepository;

    public CheckUserInCompanyUseCase(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void exist(UUID userId, UUID memberId) {
        this.memberRepository.findByUserIdAndCompanyId(userId, memberId)
                .orElseThrow(() -> new RuntimeException("User is not a member of the company"));
    }

    public void notExist(UUID userId, UUID memberId) {
        this.memberRepository.findByUserIdAndCompanyId(userId, memberId)
                .ifPresent(member -> {
                    throw new RuntimeException("User is already a member of the company");
                });
    }
    public Member getMember(UUID userId, UUID companyId) {
        return this.memberRepository.findMemberByUserIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new RuntimeException("User is not a member of the company"));
    }
}
