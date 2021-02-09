package com.ibm.bank.service;

import com.ibm.bank.model.Balance;
import com.ibm.bank.model.Transaction;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface IbmBankService {
    public void saveBalance(Balance balance);
    public void saveTransaction(Transaction transaction);
    public Double getBalance(String accountNumber);
    public List<Transaction> getTransactions(String accountNumber, String type, Date startTime, Date endTime, Pageable pageable);

}
