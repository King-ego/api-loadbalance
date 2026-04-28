package com.load.balance.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Table(name = "penalties")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
