package com.bank.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.bank.system.entity.Account;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	
    @Query(value="SELECT * FROM account WHERE user_id = :user_id",nativeQuery= true)
    List<Account> getUserAccountsById(@Param("user_id")int user_id);

    @Query(value="SELECT sum(balance) FROM account WHERE user_id = :user_id",nativeQuery= true)
    BigDecimal getTotalBalance(@Param("user_id") int user_id);

    @Query(value="SELECT balance FROM account WHERE user_id = :user_id AND account_id = :account_id", nativeQuery = true)
    Double getAccountBalance(@Param("user_id") int user_id, @Param("account_id") int account_id);

    @Modifying
    @Query(value = "UPDATE account set balance = :new_balance WHERE account_id= :account_id", nativeQuery = true)
    @Transactional
    void changeAccountsBalanceById(@Param("new_balance") double new_balance, @Param("account_id")int account_id);

    
}
