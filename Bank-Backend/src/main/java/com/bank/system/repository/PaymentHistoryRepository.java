package com.bank.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.system.entity.PaymentHistory;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory,Integer> {

    @Query(value = "SELECT * FROM v_payments WHERE user_id = :user_id",nativeQuery = true)
    List<PaymentHistory> getPaymentsRecordsById(@Param("user_id")int user_id);
}
