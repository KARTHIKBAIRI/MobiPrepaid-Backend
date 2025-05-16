package com.mobicomm.mobilerecharge.repository;

import com.mobicomm.mobilerecharge.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}