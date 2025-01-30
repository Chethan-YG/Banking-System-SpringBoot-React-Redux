package com.bank.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.system.entity.TransactionHistory;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface TransactRepository extends JpaRepository<TransactionHistory, Integer> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO v_transaction_history(account_id, transaction_type, amount, source, status, reason_code, created_at,user_id)" +
            "VALUES(:account_id, :transaction_type, :amount, :source, :status, :reason_code, :created_at, :user_id)",nativeQuery = true)
    void logTransaction(@Param("account_id")int account_id,
                        @Param("transaction_type")String transaction_type,
                        @Param("amount")double amount,
                        @Param("source")String source ,
                        @Param("status")String status,
                        @Param("reason_code")String reason_code,
                        @Param("created_at")LocalDateTime created_at,
                        @Param("user_id")Integer user_id);
}
