package com.bank.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.system.entity.TransactionHistory;

import java.util.List;


public interface TransactHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

    @Query(value = "SELECT * FROM v_transaction_history WHERE user_id = :user_id",nativeQuery = true)
    List<TransactionHistory> getTransactionRecordsById(@Param("user_id")int user_id);

    @Query(value = "SELECT * FROM v_transaction_history WHERE account_id = :account_id",nativeQuery = true)
    List<TransactionHistory> getTransactionRecordsByAccountId(@Param("account_id")int account_id);

}
