package com.ibm.bank.repository;

import com.ibm.bank.model.Balance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BalanceRepository extends CrudRepository<Balance, Integer> {
    Balance findByAccountNumber(@Param("accountNumber") String accountNumber);
}
