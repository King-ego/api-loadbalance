package com.load.balance.models;

import com.load.balance.enums.StatusCompany;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "companies")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private Users createdBy;

    @ManyToMany(mappedBy = "memberOf", fetch = FetchType.LAZY)
    private List<Users> members;

    private String name;

    private String description;

    private String slug;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private StatusCompany status;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}