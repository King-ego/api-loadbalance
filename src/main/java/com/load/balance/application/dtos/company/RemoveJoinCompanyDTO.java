package com.load.balance.application.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveJoinCompanyDTO {
    @NonNull
    private UUID companyId;

    @NonNull
    private UUID userId;
}
