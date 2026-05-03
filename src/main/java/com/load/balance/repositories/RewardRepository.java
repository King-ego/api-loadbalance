package com.load.balance.repositories;

import com.load.balance.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RewardRepository extends JpaRepository<Reward, UUID> {
}
