package com.load.balance.repositories;

import com.load.balance.models.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<Penalty, UUID> {
}
