package com.load.balance.repositories;

import com.load.balance.models.Users;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull Users, @NonNull UUID> {
}
