package com.load.balance.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "profile_members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class profileMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
