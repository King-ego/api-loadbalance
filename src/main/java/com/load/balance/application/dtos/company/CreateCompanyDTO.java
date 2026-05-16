package com.load.balance.application.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyDTO {
    @NonNull
    private String name;

    private String description;
}
