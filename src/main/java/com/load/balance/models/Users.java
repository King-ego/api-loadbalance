package com.load.balance.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;
    private String name;
    private String password;

    @Column(unique = true)
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String role;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Company> companies;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "company_members",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<Company> memberOf;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

