package com.load.balance.application.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinCompanyDTO {
    @NonNull
    private UUID userId;
    @NonNull
    private String companyId;
}
