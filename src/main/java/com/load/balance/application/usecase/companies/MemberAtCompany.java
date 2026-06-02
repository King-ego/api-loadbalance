package com.load.balance.application.usecase.companies;

import com.load.balance.enums.CompanyMemberRoles;
import com.load.balance.models.Companies;
import com.load.balance.models.Member;
import com.load.balance.models.Users;
import com.load.balance.repositories.MemberRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MemberAtCompany {
    private final MemberRepository memberRepository;

    public MemberAtCompany(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void add(Companies company, Users user, CompanyMemberRoles role) {
        Member  member = Member.builder()
                .user(user)
                .roleInCompany(role)
                .points(0)
                .company(company)
                .joinedAt(LocalDateTime.now())
                .build();

        this.memberRepository.save(member);
    }

    public void remove(Companies company, Users user) {
        this.memberRepository.deleteByCompanyAndUser(company, user);
    }
}
