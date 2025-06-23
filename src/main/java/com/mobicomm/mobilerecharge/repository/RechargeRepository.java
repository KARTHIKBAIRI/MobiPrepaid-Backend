package com.mobicomm.mobilerecharge.repository;

import com.mobicomm.mobilerecharge.model.Recharge;
import com.mobicomm.mobilerecharge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    List<Recharge> findByUser(User user);

    @Query("SELECT r FROM Recharge r WHERE r.user = :user AND r.rechargeDate = (SELECT MAX(r2.rechargeDate) FROM Recharge r2 WHERE r2.user = :user)")
    Optional<Recharge> findLatestByUser(@Param("user") User user);


    @Query("SELECT r FROM Recharge r WHERE r.expiryDate BETWEEN :start AND :end")
    List<Recharge> findExpiringRecharges(@Param("start") Date start, @Param("end") Date end);

    // New method for latest expiring recharges
    @Query("SELECT r FROM Recharge r WHERE r.rechargeDate = (SELECT MAX(r2.rechargeDate) FROM Recharge r2 WHERE r2.user = r.user) AND r.expiryDate BETWEEN :start AND :end")
    List<Recharge> findLatestExpiringRecharges(@Param("start") Date start, @Param("end") Date end);
}