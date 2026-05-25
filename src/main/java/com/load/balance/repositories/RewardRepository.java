package com.load.balance.repositories;

import com.load.balance.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RewardRepository extends JpaRepository<Reward, UUID> {
}

