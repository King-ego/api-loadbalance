package com.load.balance.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_admins")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class profileAdmins {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}
